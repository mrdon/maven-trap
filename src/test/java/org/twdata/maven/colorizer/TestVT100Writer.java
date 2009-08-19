package org.twdata.maven.colorizer;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.twdata.maven.colorizer.VT100Writer.*;

public class TestVT100Writer extends TestCase
{
    public void testRedFg()
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        new VT100Writer(new PrintStream(bout)).fg(Color.RED);
        assertEquals(ESC + "31m", new String(bout.toByteArray()));
    }

    public void testBold()
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        new VT100Writer(new PrintStream(bout)).mod(CharacterModifier.BOLD);
        assertEquals(ESC + "1m", new String(bout.toByteArray()));
    }

    public void testRedFgBold()
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        new VT100Writer(new PrintStream(bout)).fg(Color.RED, CharacterModifier.BOLD);
        assertEquals(ESC + "1;31m", new String(bout.toByteArray()));
    }
}
