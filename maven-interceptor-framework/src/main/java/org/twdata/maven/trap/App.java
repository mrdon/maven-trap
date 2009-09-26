package org.twdata.maven.trap;

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
    public static void main(String[] args) throws Exception
    {
        Dispatcher.main(args);
    }
}
