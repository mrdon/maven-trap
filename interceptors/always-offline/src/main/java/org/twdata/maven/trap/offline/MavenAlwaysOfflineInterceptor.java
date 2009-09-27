package org.twdata.maven.trap.offline;

import org.twdata.maven.trap.EnvMavenInterceptor;

import java.util.*;

public class MavenAlwaysOfflineInterceptor extends EnvMavenInterceptor
{
    private static final Set<String> ONLINE_ARGS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
            "\"release:perform\"",
            "\"deploy\"",
            "\"dependency:resolve\"",
            "\"-U\""
    )));
    private static final String OFFLINE = "\"-o\"";

    public String[] onBefore(String[] args)
    {
        final List<String> argsList = new ArrayList<String>(Arrays.asList(args));
        if (argsList.contains(OFFLINE) || !Collections.disjoint(ONLINE_ARGS, argsList))
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
