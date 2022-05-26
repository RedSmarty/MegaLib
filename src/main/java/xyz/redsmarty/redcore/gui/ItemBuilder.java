package xyz.redsmarty.redcore.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.redsmarty.redcore.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class ItemBuilder {
    private Material type = Material.AIR;
    private int amount = 1;
    private Function<ItemMeta, ItemMeta> callBack = Function.identity();
    private String displayName;
    private List<String> lore = new ArrayList<>();
    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private final List<ItemFlag> flags = new ArrayList<>();
    private boolean unbreakable = false;

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(type, amount);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack;
        if (displayName != null) {
            meta.setDisplayName(displayName);
        }
        meta.setLore(lore);
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            meta.addEnchant(entry.getKey(), entry.getValue(), true);
        }
        for (ItemFlag flag : flags) {
            meta.addItemFlags(flag);
        }
        meta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(callBack.apply(meta));
        return itemStack;
    }

    public ItemBuilder setType(Material type) {
        this.type = type;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setCallBack(Function<ItemMeta, ItemMeta> callBack) {
        this.callBack = callBack;
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        this.flags.addAll(Arrays.asList(flags));
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public static ItemBuilder builder() {
        return new ItemBuilder();
    }

    public static ItemStack createGuiItem(Material material, String name, String... lore) {
        return builder()
                .setType(material)
                .setAmount(1)
                .setDisplayName(name)
                .setLore(lore)
                .build();
    }

    public static ItemStack createFireworkStar(String name, Color[] colors, String... lore) {
        return builder()
                .setType(Material.FIREWORK_STAR)
                .setDisplayName(name)
                .setLore(lore)
                .setCallBack(itemMeta -> {
                    FireworkEffectMeta meta = (FireworkEffectMeta) itemMeta;
                    meta.setEffect(FireworkEffect.builder().withColor(colors).build());
                    return meta;
                }).build();
    }

    public static ItemStack createSkull(String name, String value, String... lore) {
        return builder()
                .setType(Material.PLAYER_HEAD)
                .setDisplayName(name)
                .setLore(lore)
                .setCallBack(itemMeta -> {
                    SkullMeta meta = (SkullMeta) itemMeta;
                    GameProfile profile = new GameProfile(UUID.randomUUID(), "");
                    profile.getProperties().put("textures", new Property("textures", value));
                    Field field = null;
                    try {
                        field = ReflectionUtils.getField(meta.getClass(), "profile");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    try {
                        field.set(meta, profile);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return meta;
                }).build();
    }

    public static ItemStack createSkull(String name, URL url, String... lore) {
        return createSkull(name, Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"%s\"}}}", url.toString()).getBytes()));
    }
}
