package org.twdata.maven.colorizer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wraps Maven
 */
public class App
{
    private static final String mainClass = "org.codehaus.classworlds.Launcher";

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException
    {
        System.out.println("Colorizing console...");
        LexingOutputStream out = new LexingOutputStream();
        System.setOut(new PrintStream(out));

        Class cls = Class.forName(mainClass);
        Method main = cls.getMethod("main", String[].class);
        main.invoke(null, new Object[]{args});
    }

    private static class LexingOutputStream extends OutputStream
    {
        private OutputLexer lexer;

        public LexingOutputStream()
        {
            lexer = new OutputLexer(new StringReader(""));
        }

        public void write(int i) throws IOException
        {
            lexer.yyreset(new InputStreamReader(new ByteArrayInputStream(new byte[i])));
            lexer.yylex();
        }

        @Override
        public void write(byte[] bytes, int pos, int len) throws IOException
        {
            lexer.yyreset(new InputStreamReader(new ByteArrayInputStream(bytes, pos, len)));
            lexer.yylex();
        }


    }
}
