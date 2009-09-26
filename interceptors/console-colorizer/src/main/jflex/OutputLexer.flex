package org.twdata.maven.trap.colorizer;

import java.io.*;
import static org.twdata.maven.trap.colorizer.VT100Writer.CharacterModifier.*;
import static org.twdata.maven.trap.colorizer.Color.*;


/**
 * This class is a simple example lexer.
 */
%%

%class OutputLexer
%unicode

%{
    private final PrintStream out;
    private final VT100Writer term;
    private final StringBuffer lineBuffer = new StringBuffer();

    private void print(Color color, String text, VT100Writer.CharacterModifier... mods) {
        term.fg(color, mods);
        out.print(text);
        term.clear();
    }
%}

%init{
this.out = System.out;
this.term = new VT100Writer(this.out);
%init}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

%state LOGLINE
%state FAILEDTESTS
%integer

%%

/* keywords */
<YYINITIAL> .*"<<< FAILURE!"      { print(RED, yytext(), BOLD);  }

<YYINITIAL> ^"[ERROR]" { term.fg(RED); out.print(yytext()); yybegin(LOGLINE); }
<YYINITIAL> ^"[DEBUG]" { term.fg(WHITE); out.print(yytext()); yybegin(LOGLINE); }
<YYINITIAL> ^"[WARNING]" { term.fg(YELLOW); out.print(yytext()); yybegin(LOGLINE); }
<YYINITIAL> ^"[INFO]" { out.print(yytext()); yybegin(LOGLINE); }

<YYINITIAL> ^"Tests run:".* { term.bg(BLACK); term.fg(WHITE);  out.print(yytext()); term.clear(); }
<YYINITIAL> ^"Failed tests:" { print(RED, yytext()); yybegin(FAILEDTESTS); term.fg(RED, BOLD); }

<LOGLINE> {
  "BUILD FAILURE"      { term.bg(RED); term.fg(BLACK);  out.print(yytext()); term.clear(); }
  "BUILD SUCCESSFUL"   { term.bg(GREEN); term.fg(BLACK);  out.print(yytext()); term.clear(); }
  {InputCharacter}     { out.print(yytext()); }
  {LineTerminator}     { out.print(yytext()); term.clear(); yybegin(YYINITIAL); }
}

<FAILEDTESTS> {
  ^[^ ]                { out.print(yytext()); term.clear(); yybegin(YYINITIAL); }
  .|\n                 { out.print(yytext()); }
}

/* fallback */
.|\n                             { out.print(yytext()); }
