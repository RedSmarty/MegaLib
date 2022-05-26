package xyz.redsmarty.redcore.nbt.tag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

public abstract class NbtArray<T extends Number> implements NbtBase {
    protected List<T> data = new ArrayList<>();

    @Override
    public JsonArray toJson() {
        JsonArray array = new JsonArray();
        for (T entry : this.data) {
            array.add(entry);
        }
        return array;
    }


    public String toJsonString(boolean pretty) {
        GsonBuilder builder = new GsonBuilder();
        if (pretty) {
            builder.setPrettyPrinting();
        }
        Gson gson = builder.create();
        return gson.toJson(toJson());
    }

    public String toString() {
        return toJsonString(false);
    }
}
