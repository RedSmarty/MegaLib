package xyz.redsmarty.redcore;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.redsmarty.redcore.gui.GuiListener;

public final class RedCore extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
    }
}
