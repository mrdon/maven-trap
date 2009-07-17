package org.twdata.maven.coloriser;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wraps Maven
 */
public class App 
{
    private static final String mainClass = "org.codehaus.classworlds.Launcher";
    public static void main( String[] args ) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException
    {
        final PipedOutputStream pout = new PipedOutputStream();
        final PipedInputStream pin = new PipedInputStream(pout, 1);

        Thread t = new Thread(new Runnable() {

            public void run()
            {
                System.out.println("Colorising console...");
                OutputLexer lexer = new OutputLexer(pin);
                System.setOut(new PrintStream(pout));
                try
                {
                    lexer.yylex();
                }
                catch (IOException e)
                {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
        t.start();
        Class cls = Class.forName(mainClass);
        Method main = cls.getMethod("main", String[].class);
        main.invoke(null, new Object[]{args});
    }
}
