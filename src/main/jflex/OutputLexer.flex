package org.twdata.maven.colorizer;

import java.io.*;
import static org.twdata.maven.colorizer.VT100Writer.CharacterModifier.*;
import static org.twdata.maven.colorizer.Color.*;


/**
 * This class is a simple example lexer.
 */
%%

%class OutputLexer
%unicode

%{
    private final PrintStream out;
    private final VT100Writer term;

    private void printLog(Color color, String text) {
        if (color != null)
            term.fg(color, BOLD);
        else
            term.mod(BOLD);
        int pos = text.indexOf(']');
        out.print(text.substring(0, pos+1));
        if (color != null)
            term.fg(color, CLEAR);
        else
            term.mod(CLEAR);
        out.print(text.substring(pos+1));
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

%state STRING
%integer

%%

/* keywords */
<YYINITIAL> .*"BUILD FAILURE".*      { term.fg(RED, BOLD, BLINK); out.print(yytext()); term.clear(); }
<YYINITIAL> .*"<<< FAILURE!"      { term.fg(RED, BOLD, BLINK); out.print(yytext()); term.clear(); }

<YYINITIAL> ^"[ERROR]".* { printLog(RED, yytext()); }
<YYINITIAL> ^"[DEBUG]".* { printLog(GRAY, yytext()); }
<YYINITIAL> ^"[INFO]".* { printLog(null, yytext()); }
<YYINITIAL> ^"[WARNING]".* { printLog(YELLOW, yytext()); }

/* error fallback */
.|\n                             { out.print(yytext()); }
