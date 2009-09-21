package org.twdata.maven.offline;

import org.twdata.maven.interceptor.MavenInterceptor;
import org.twdata.maven.interceptor.EnvMavenInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class MavenAlwaysOfflineInterceptor extends EnvMavenInterceptor
{
    private static final String OFFLINE = "\"-o\"";

    public String[] onBefore(String[] args)
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

    public void onAfter(int exitCode)
    {
    }

    protected String getEnvironmentVariableName()
    {
        return "MAVEN_ALWAYS_OFFLINE";
    }
}
