package org.twdata.maven.interceptor;

public interface MavenInterceptor
{
    /**
     * Called before the maven {@code main(String[]args)} is called.
     *
     * @param args the arguments passed to the maven command line.
     * @return the args to pass to the maven command line, if the interceptor doesn't modify the arguments passed to
     *         maven simply return {@code args}.
     */
    String[] before(String[] args);

    /**
     * Called after the maven {@code main(String[]args)} is called. This is garanteed to be called.
     *
     * @param exitCode the exit code of the maven command line.
     */
    void after(int exitCode);
}
