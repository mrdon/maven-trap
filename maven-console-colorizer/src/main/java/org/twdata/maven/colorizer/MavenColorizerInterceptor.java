package org.twdata.maven.colorizer;

import org.twdata.maven.interceptor.MavenInterceptor;
import org.twdata.maven.interceptor.EnvMavenInterceptor;

import java.io.*;

public class MavenColorizerInterceptor extends EnvMavenInterceptor
{
    public String[] onBefore(String[] args)
    {
        System.out.println("Colorizing console...");
        System.setOut(new PrintStream(new LexingOutputStream()));

        return args;
    }

    public void onAfter(int exitCode)
    {
        // no-op
    }

    protected String getEnvironmentVariableName()
    {
        return "MAVEN_COLOR";
    }

    private static class LexingOutputStream extends OutputStream
    {
        private final OutputLexer lexer;
        private int lastState;

        public LexingOutputStream()
        {
            lexer = new OutputLexer(new StringReader(""));
            lastState = lexer.yystate();
        }

        public void write(int i) throws IOException
        {
            lexer.yyreset(new InputStreamReader(new ByteArrayInputStream(new byte[i])));
            lexer.yybegin(lastState);
            lexer.yylex();
            lastState = lexer.yystate();

        }

        @Override
        public void write(byte[] bytes, int pos, int len) throws IOException
        {
            lexer.yyreset(new InputStreamReader(new ByteArrayInputStream(bytes, pos, len)));
            lexer.yybegin(lastState);
            lexer.yylex();
            lastState = lexer.yystate();
        }


    }
}
