package org.twdata.maven.offline;

import org.twdata.maven.interceptor.MavenInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class MavenAlwaysOfflineInterceptor implements MavenInterceptor
{
    private static final String OFFLINE = "\"-o\"";

    public String[] before(String[] args)
    {
        final List<String> argsList = new ArrayList<String>(Arrays.asList(args));
        if (argsList.contains(OFFLINE))
        {
            argsList.remove(OFFLINE);
        }
        else
        {
            argsList.add(OFFLINE);
        }
        return argsList.toArray(new String[argsList.size()]);
    }

    public void after(int exitCode)
    {
    }
}
