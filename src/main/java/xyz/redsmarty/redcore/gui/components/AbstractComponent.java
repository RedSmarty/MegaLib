package xyz.redsmarty.redcore.gui.components;

public abstract class AbstractComponent {
    protected final int x;
    protected final int y;
    protected final int length;
    protected final int height;
    protected Runnable updateCallback = () -> {};

    public AbstractComponent(int x, int y, int length, int height) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.height = height;
    }

    protected void update() {
        updateCallback.run();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }


    public Runnable getUpdateCallback() {
        return updateCallback;
    }

    public void setUpdateCallback(Runnable updateCallback) {
        this.updateCallback = updateCallback;
    }
}
