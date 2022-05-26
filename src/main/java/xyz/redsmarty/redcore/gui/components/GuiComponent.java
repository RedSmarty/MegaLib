package xyz.redsmarty.redcore.gui.components;

import org.apache.commons.lang.Validate;
import xyz.redsmarty.redcore.gui.Page;


public abstract class GuiComponent extends AbstractComponent {

    public GuiComponent(int x, int y, int length, int height) {
        super(x, y, length, height);
    }
    public void render(Page page) {
        Validate.isTrue((x + length < 9) || (y + height < page.getRows()), "Component position and scale is out of bounds of the inventory");
    }
}
