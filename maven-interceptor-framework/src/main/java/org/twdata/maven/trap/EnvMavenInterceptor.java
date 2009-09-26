package org.twdata.maven.trap;

/**
 * A maven interceptor that is enabled by an environement variable.
 */
public abstract class EnvMavenInterceptor implements MavenInterceptor
{
    public final String[] before(String[] args)
    {
        if (isEnabled())
        {
            return onBefore(args);
        }
        else
        {
            return args;
        }
    }

    protected abstract String[] onBefore(String[] args);

    public final void after(int exitCode)
    {
        if (isEnabled())
        {
            onAfter(exitCode);
        }
    }

    protected abstract void onAfter(int exitCode);

    private boolean isEnabled()
    {
        return System.getenv(getEnvironmentVariableName()) != null;
    }

    protected abstract String getEnvironmentVariableName();
}
