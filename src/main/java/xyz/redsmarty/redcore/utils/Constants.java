package xyz.redsmarty.redcore.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import xyz.redsmarty.redcore.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;

public class Constants {
    public static final String SERVER_VERSION;
    public static final int SERVER_VERSION_MAJOR;
    public static final String SERVER_VERSION_MINOR;
    public static final int SERVER_DATA_VERSION;


    static {
        String serverPackageName = Bukkit.getServer().getClass().getPackage().getName();

        SERVER_VERSION = serverPackageName.substring(serverPackageName.lastIndexOf('.') +1);

        String[] splitVersion = SERVER_VERSION.split("_");

        SERVER_VERSION_MAJOR = Integer.parseInt(splitVersion[1]);
        SERVER_VERSION_MINOR = splitVersion[2];

        InputStream stream = Bukkit.class.getClassLoader().getResourceAsStream("version.json");
        if (stream == null) {
            throw new RuntimeException("Cannot find version.json");
        }
        JsonObject versionObject = null;
        try {
            versionObject = new JsonParser().parse(StringUtils.toString(stream)).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SERVER_DATA_VERSION = versionObject.getAsJsonPrimitive("world_version").getAsInt();
    }

}
