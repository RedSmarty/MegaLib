package xyz.redsmarty.redcore.gui;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import xyz.redsmarty.redcore.gui.components.GuiComponent;
import xyz.redsmarty.redcore.gui.components.PaginationComponent;
import xyz.redsmarty.redcore.gui.item.GuiItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class PaginatedGui implements Gui {
    private final int rows;
    private final String title;
    private final Inventory inventory;
    private int currentPageIndex;
    private Page currentPage;
    private PaginationComponent paginationComponent;
    private Consumer<InventoryCloseEvent> closeCallback = closeEvent -> {};
    private final List<Page> pages = new ArrayList<>();


    public PaginatedGui(int rows, String title) {
        this.rows = rows;
        this.title = title;
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        addPage();
        openPage(0);
    }

    public void next() {
        openPage(currentPageIndex + 1);
    }

    public void previous() {
        openPage(currentPageIndex - 1);
    }

    public void openPage(int index) {
//        Validate.isTrue(currentPageIndex - 1 >= 0 && currentPageIndex + 1 < pages.size(), String.format("Page index out of bounds, min 0, max %d, got %d", pages.size() - 1, index));
        currentPage = pages.get(index);
        currentPageIndex = index;
        currentPage.update();
        if (paginationComponent != null) {
            paginationComponent.render(this);
        }
    }

    public Page addPage(int index) throws IndexOutOfBoundsException {
        Page page = createPage();
        pages.add(index, page);
        return page;
    }

    public Page addPage() {
        Page page = createPage();
        pages.add(page);
        return page;
    }

    public void removePage(int index) {
        Validate.isTrue(index < pages.size(), "Cannot remove page " + index + ". There are only " + pages.size() + " in this Gui");
        pages.remove(index);
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public Page getPage(int index) {
        Validate.isTrue(index < pages.size(), "Cannot get page " + index + ". There are only " + pages.size() + " in this Gui");
        return pages.get(index);
    }

    public List<Page> getPages() {
        return Collections.unmodifiableList(pages);
    }

    @Override
    public void setItem(int i, GuiItem item) {
        currentPage.setItem(i, item);
    }

    @Override
    public void setItem(int row, int column, GuiItem item) {
        currentPage.setItem(row, column, item);
    }

    @Override
    public void addComponent(GuiComponent component) {
        currentPage.addComponent(component);
    }

    public void setPaginationComponent(PaginationComponent component) {
        this.paginationComponent = component;
        paginationComponent.render(this);
    }

    @Override
    public void fillOutline(GuiItem item) {
        currentPage.fillOutline(item);
    }

    @Override
    public void fillBackground(GuiItem item) {
        currentPage.fillBackground(item);
    }

    @Override
    public void fromPattern(Pattern pattern) {
        currentPage.fromPattern(pattern);
    }

    @Override
    public void show(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public void click(InventoryClickEvent e) {
        currentPage.click(e);
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        closeCallback.accept(e);
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getSize() {
        return rows * 9;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void setCloseCallback(Consumer<InventoryCloseEvent> callback) {
        this.closeCallback = callback;
    }

    private Page createPage() {
        return new Page(rows) {
            @Override
            public void update() {
                if (this == currentPage) {
                    for (int i = 0; i < contents.length; i++) {
                        inventory.setItem(i, contents[i] == null ? null : contents[i].getItem());
                    }
                }
            }
        };
    }
}
