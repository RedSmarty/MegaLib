package xyz.redsmarty.redcore.nbt;

public class NbtOptions {
    private static char PATH_SEPARATOR = '.';

    public static char getPathSeparator() {
        return PATH_SEPARATOR;
    }

    public static void setPathSeparator(char pathSeparator) {
        PATH_SEPARATOR = pathSeparator;
    }

}
