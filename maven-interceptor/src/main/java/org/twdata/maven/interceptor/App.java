package org.twdata.maven.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.LinkedList;
import java.io.*;
import java.security.Permission;
import java.net.InetAddress;

/**
 * Wrap the maven main method for interception
 */
public class App
{
    private static final String MAVEN_MAIN_CLASS = "org.codehaus.classworlds.Launcher";
    private static final String INTERCEPTORS_RESOURCE = "/org/twdata/maven/interceptor/interceptors";

    public static void main(String[] args) throws Exception
    {
        final List<MavenInterceptor> interceptors = getMavenInterceptors();

        // sets the security manager that will call the interceptor after
        final MavenInterceptorSecurityManager securityManager = new MavenInterceptorSecurityManager(interceptors);
        System.setSecurityManager(securityManager);

        try
        {
            final String[] updatedArgs = callInterceptorsBefore(interceptors, args);
            invokeMavenMainMethod(updatedArgs);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.exit(securityManager.exitCodeSet ? securityManager.exitCode : 0);
        }
    }

    private static List<MavenInterceptor> getMavenInterceptors() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        final List<MavenInterceptor> interceptors = new LinkedList<MavenInterceptor>();
        final BufferedReader reader = getInterceptorsResourceBufferedReader();
        String line = reader.readLine();
        while (line != null)
        {
            interceptors.add((MavenInterceptor) getInstanceFromName(line));
            line = reader.readLine();
        }
        return interceptors;
    }

    private static Object getInstanceFromName(String line) throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        return getClassFromName(line).newInstance();
    }

    private static Class<?> getClassFromName(String line) throws ClassNotFoundException
    {
        return Class.forName(line.trim());
    }

    private static BufferedReader getInterceptorsResourceBufferedReader()
    {
        return new BufferedReader(new InputStreamReader(getInterceptorResourceAsStream()));
    }

    private static InputStream getInterceptorResourceAsStream()
    {
        return App.class.getClass().getResourceAsStream(INTERCEPTORS_RESOURCE);
    }

    private static String[] callInterceptorsBefore(List<MavenInterceptor> interceptors, String[] args)
    {
        String[] updatedArgs = args;
        for (MavenInterceptor interceptor : interceptors)
        {
            updatedArgs = interceptor.before(updatedArgs);
        }
        return updatedArgs;
    }

    private static void callInterceptorsAfter(List<MavenInterceptor> interceptors, int exitCode)
    {
        for (MavenInterceptor interceptor : interceptors)
        {
            interceptor.after(exitCode);
        }
    }

    private static void invokeMavenMainMethod(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException
    {
        getMavenMainMethod().invoke(null, new Object[]{args});
    }

    private static Method getMavenMainMethod() throws NoSuchMethodException, ClassNotFoundException
    {
        return getMavenMainClass().getMethod("main", String[].class);
    }

    private static Class<?> getMavenMainClass() throws ClassNotFoundException
    {
        return Class.forName(MAVEN_MAIN_CLASS);
    }

    private static class MavenInterceptorSecurityManager extends SecurityManager
    {
        private final List<MavenInterceptor> interceptors;
        private boolean exitCodeSet = false;
        private int exitCode;

        public MavenInterceptorSecurityManager(List<MavenInterceptor> interceptors)
        {
            this.interceptors = interceptors;
        }

        @Override
        public void checkExit(int status)
        {
            exitCode = status;
            exitCodeSet = true;
            callInterceptorsAfter(interceptors, status);
        }

        @Override
        public void checkPermission(Permission permission)
        {
        }

        @Override
        public void checkPermission(Permission permission, Object o)
        {
        }

        @Override
        public void checkCreateClassLoader()
        {
        }

        @Override
        public void checkAccess(Thread thread)
        {
        }

        @Override
        public void checkAccess(ThreadGroup threadGroup)
        {
        }

        @Override
        public void checkExec(String s)
        {
        }

        @Override
        public void checkLink(String s)
        {
        }

        @Override
        public void checkRead(FileDescriptor fileDescriptor)
        {
        }

        @Override
        public void checkRead(String s)
        {
        }

        @Override
        public void checkRead(String s, Object o)
        {
        }

        @Override
        public void checkWrite(FileDescriptor fileDescriptor)
        {
        }

        @Override
        public void checkWrite(String s)
        {
        }

        @Override
        public void checkDelete(String s)
        {
        }

        @Override
        public void checkConnect(String s, int i)
        {
        }

        @Override
        public void checkConnect(String s, int i, Object o)
        {
        }

        @Override
        public void checkListen(int i)
        {
        }

        @Override
        public void checkAccept(String s, int i)
        {
        }

        @Override
        public void checkMulticast(InetAddress inetAddress)
        {
        }

        @Override
        public void checkPropertiesAccess()
        {
        }

        @Override
        public void checkPropertyAccess(String s)
        {
        }

        @Override
        public boolean checkTopLevelWindow(Object o)
        {
            return true;
        }

        @Override
        public void checkPrintJobAccess()
        {
        }

        @Override
        public void checkSystemClipboardAccess()
        {
        }

        @Override
        public void checkAwtEventQueueAccess()
        {
        }

        @Override
        public void checkPackageAccess(String s)
        {
        }

        @Override
        public void checkPackageDefinition(String s)
        {
        }

        @Override
        public void checkSetFactory()
        {
        }

        @Override
        public void checkMemberAccess(Class<?> aClass, int i)
        {
        }

        @Override
        public void checkSecurityAccess(String s)
        {
        }
    }
}
