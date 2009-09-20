package org.twdata.maven.colorizer;

import java.util.HashMap;
import java.util.Map;
import java.io.PrintStream;

/**
 *
 */
public class VT100Writer
{
    public static enum CharacterModifier
    {
        CLEAR('0'),
        BOLD('1'),
        UNDERSCORE('4'),
        BLINK('5'),
        REVERSE('7');

        private char mod;

        CharacterModifier(char mod)
        {
            this.mod = mod;
        }

        public char getMod()
        {
            return mod;
        }

    }

    static final char CHAR_END = 'm'; // end of the attribute sequence

    static final String ESC = "\033[";

    static final Map<Color, String> foregroundColorCodes = new HashMap<Color, String>();

    static final Map<Color, String> backgroundColorCodes = new HashMap<Color, String>();

    static
    {

        foregroundColorCodes.put(Color.BLACK, "30");
        foregroundColorCodes.put(Color.RED, "31");
        foregroundColorCodes.put(Color.GREEN, "32");
        foregroundColorCodes.put(Color.YELLOW, "33");
        foregroundColorCodes.put(Color.BLUE, "34");
        foregroundColorCodes.put(Color.MAGENTA, "35");
        foregroundColorCodes.put(Color.CYAN, "36");
        foregroundColorCodes.put(Color.WHITE, "37");

        backgroundColorCodes.put(Color.BLACK, "40");
        backgroundColorCodes.put(Color.RED, "41");
        backgroundColorCodes.put(Color.GREEN, "42");
        backgroundColorCodes.put(Color.YELLOW, "43");
        backgroundColorCodes.put(Color.BLUE, "44");
        backgroundColorCodes.put(Color.MAGENTA, "45");
        backgroundColorCodes.put(Color.CYAN, "46");
        backgroundColorCodes.put(Color.WHITE, "47");
    }

    private final PrintStream out;

    public VT100Writer(PrintStream out)
    {
        this.out = out;
    }

    public VT100Writer fg(Color color, CharacterModifier... mods)
    {
        color(foregroundColorCodes.get(color), mods);
        return this;
    }

    public VT100Writer bg(Color color, CharacterModifier... mods)
    {
        color(backgroundColorCodes.get(color), mods);
        return this;
    }

    public VT100Writer mod(CharacterModifier mod)
    {
        out.print(ESC);
        out.print(mod.getMod());
        out.print(CHAR_END);
        return this;
    }

    public VT100Writer clear()
    {
        out.print(ESC);
        out.print(CHAR_END);
        return this;
    }

    VT100Writer color(String colorCode, CharacterModifier... mods)
    {
        out.print(ESC);
        for (int x = 0; x < mods.length; x++)
        {
            out.print(mods[x].getMod());
            if (mods.length != x - 1 || colorCode != null)
            {
                out.print(';');
            }
        }
        if (colorCode != null)
        {
            out.print(colorCode);
        }
        out.print(CHAR_END);
        return this;
    }

}
