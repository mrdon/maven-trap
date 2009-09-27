package org.twdata.maven.trap.offline;

import junit.framework.TestCase;

import java.util.List;
import java.util.Arrays;

/**
 *
 */
public class TestMavenAlwaysOfflineInterceptor extends TestCase {
    public void testDefault() {
        MavenAlwaysOfflineInterceptor inter = new MavenAlwaysOfflineInterceptor();
        List<String> result = Arrays.asList(inter.onBefore(new String[] {}));
        assertTrue(result.contains("\"-o\""));
    }

    public void testOnline() {
        MavenAlwaysOfflineInterceptor inter = new MavenAlwaysOfflineInterceptor();
        List<String> result = Arrays.asList(inter.onBefore(new String[] {"\"-o\""}));
        assertFalse(result.contains("\"-o\""));
    }

    public void testSpecialCases() {
        MavenAlwaysOfflineInterceptor inter = new MavenAlwaysOfflineInterceptor();
        assertFalse(Arrays.asList(inter.onBefore(new String[] {"\"deploy\""})).contains("\"-o\""));
        assertFalse(Arrays.asList(inter.onBefore(new String[] {"\"release:perform\""})).contains("\"-o\""));
        assertFalse(Arrays.asList(inter.onBefore(new String[] {"\"dependency:resolve\""})).contains("\"-o\""));
        assertFalse(Arrays.asList(inter.onBefore(new String[] {"\"-U\""})).contains("\"-o\""));
    }
}
