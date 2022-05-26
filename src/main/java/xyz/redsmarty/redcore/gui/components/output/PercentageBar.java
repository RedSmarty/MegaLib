package xyz.redsmarty.redcore.gui.components.output;

import org.apache.commons.lang.Validate;
import xyz.redsmarty.redcore.gui.Page;
import xyz.redsmarty.redcore.gui.components.GuiComponent;
import xyz.redsmarty.redcore.gui.components.Orientable;
import xyz.redsmarty.redcore.gui.components.Orientation;
import xyz.redsmarty.redcore.gui.item.GuiItem;

import java.util.function.BiFunction;

public class PercentageBar extends GuiComponent implements Orientable {
    private double percentage = 0;
    private GuiItem fillItem;
    private GuiItem backgroundItem = GuiItem.BACKGROUND_ITEM;
    private Orientation orientation = Orientation.HORIZONTAL;
    private BiFunction<Integer, Boolean, GuiItem> itemCallback = (x, y) -> y ? fillItem : backgroundItem;

    public PercentageBar(int x, int y, int length, int height) {
        super(x, y, length, height);
    }

    @Override
    public void render(Page page) {
        super.render(page);
        int index = 0;
        for (int l = 0; l < length; l++) {
            for (int h = 0; h < height; h++) {
                if (orientation == Orientation.HORIZONTAL) {
                    int slotsToFill = (int) Math.round(percentage * length);
                    GuiItem itemToFill = itemCallback.apply(index, slotsToFill > l);
                    page.setItem(h + y, l + x, itemToFill);
                } else {
                    int slotsToFill = (int) Math.round(percentage * height);
                }
                index++;
            }
        }
    }

    private void updateItem() {

    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        Validate.isTrue(percentage <= 1.0 && percentage >= 0.0, "Percentage must be between 0 and 1");
        this.percentage = percentage;
    }

    public GuiItem getFillItem() {
        return fillItem;
    }

    public void setFillItem(GuiItem fillItem) {
        this.fillItem = fillItem;
    }

    public GuiItem getBackgroundItem() {
        return backgroundItem;
    }

    public void setBackgroundItem(GuiItem backgroundItem) {
        this.backgroundItem = backgroundItem;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void setItemCallback(BiFunction<Integer, Boolean, GuiItem> itemCallback) {
        this.itemCallback = itemCallback;
    }
}
