package xyz.redsmarty.redcore.gui.components;

import xyz.redsmarty.redcore.gui.PaginatedGui;

public abstract class PaginationComponent extends AbstractComponent {
    public PaginationComponent(int x, int y, int length, int height) {
        super(x, y, length, height);
    }

    abstract public void render(PaginatedGui gui);
}
