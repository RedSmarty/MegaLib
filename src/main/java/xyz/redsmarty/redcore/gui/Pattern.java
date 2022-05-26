package xyz.redsmarty.redcore.gui;

import org.apache.commons.lang.Validate;
import xyz.redsmarty.redcore.gui.item.GuiItem;

import java.util.HashMap;
import java.util.Map;

public class Pattern {
    private final Map<Character, GuiItem> items = new HashMap<>();
    private final String[] pattern;

    public Pattern(String... pattern) {
        for (String s : pattern) {
            Validate.isTrue(s.length() == 9, "length of pattern must be 9");
        }
        this.pattern = pattern;
    }

    public void bindItem(char c, GuiItem item) {
        items.put(c, item);
    }

    public void clearBindings() {
        items.clear();
    }

    public String[] getPattern() {
        return pattern;
    }

    public GuiItem getItem(char c) {
        return items.get(c);
    }
}
