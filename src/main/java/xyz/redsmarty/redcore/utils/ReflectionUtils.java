package xyz.redsmarty.redcore.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Taken from https://github.com/TheDutchMC/BukkitReflectionUtil/blob/master/src/main/java/dev/array21/bukkitreflectionlib/ReflectionUtil.java and modified
 * Licensed under MIT
 */
public class ReflectionUtils {

    public static String SERVER_VERSION;
    private static boolean useNewSpigotPackaging;
    private static int majorVersion;

    /**
     * Maps classes from 1.17 class names to old class names
     */
    private static Map<String, String> classMap = new HashMap<>();

    /**
     * Map that stores cache of classes to improve performance
     */
    private static final Map<String, Class<?>> classCache = new HashMap<>();

    /**
     * Table that stores cache of fields to improve performance
     */
    private static final Table<Class<?>, String, Field> fieldCache = HashBasedTable.create();

    /**
     * Table that stores cache of methods to improve performance
     */
    private static final Table<Class<?>, String, Method> methodCache = HashBasedTable.create();

    static {
        try {
            Class<?> bukkitClass = Class.forName("org.bukkit.Bukkit");
            Object serverObject = getMethod(bukkitClass, "getServer").invoke(null);
            String serverPackageName = serverObject.getClass().getPackage().getName();

            SERVER_VERSION = serverPackageName.substring(serverPackageName.lastIndexOf('.') +1);

            String major = SERVER_VERSION.split("_")[1];

            majorVersion = Integer.parseInt(major);
            useNewSpigotPackaging = majorVersion >= 17;

        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the new way of packaging Spigot is used<br>
     * For >=1.17 this will be true, for =<1.16 this will be false.<br>
     *
     * This dictates if you should use {@link #getNmsClassOld(String)} (<=1.16) or {@link #getNmsClassNew(String)} (>=1.17).
     * @return Returns true if it is, false if it is now
     */
    public static boolean isUseNewSpigotPackaging() {
        return useNewSpigotPackaging;
    }

    /**
     * Get the current major Minecraft version.
     *
     * E.g for Minecraft 1.18 this is 18.
     * @return The current major Minecraft version
     */
    public static int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Get a Class from the org.bukkit.craftbukkit.SERVER_VERSION. package
     * @param className The name of the class
     * @return Returns the Class
     * @throws ClassNotFoundException Thrown when the Class was not found
     */
    public static Class<?> getBukkitClass(String className) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + "." + className);
    }

    /**
     * <strong>1.16 and older only</strong>
     *
     * Get a Class from the net.minecraft.server.SERVER_VERSION. package
     * @param className The name of the class
     * @return Returns the Class
     * @throws ClassNotFoundException Thrown when the Class was not found
     */
    public static Class<?> getNmsClassOld(String className) throws ClassNotFoundException {
        if (classCache.containsKey(className)) {
            return classCache.get(className);
        }
        Class<?> clazz = Class.forName("net.minecraft.server." + SERVER_VERSION + "." + className);
        classCache.put(className, clazz);
        return clazz;
    }

    /**
     * <strong>1.17 and newer only</strong>
     *
     * Get a Class from the net.minecraft package
     * @param className The name of the class
     * @return Returns the class
     * @throws ClassNotFoundException Thrown when the Class was not found
     */
    public static Class<?> getNmsClassNew(String className) throws ClassNotFoundException {
        if (classCache.containsKey(className)) {
            return classCache.get(className);
        }
        Class<?> clazz = Class.forName("net.minecraft." + className);
        classCache.put(className, clazz);
        return clazz;
    }

    /**
     * Get a class from the net.minecraft in any version
     * @param oldClassName The name of the class prior to 1.17
     * @param newClassName The name of the class in and after 1.17
     */
    public static Class<?> getMinecraftClass(String oldClassName, String newClassName) throws ClassNotFoundException {
        if (majorVersion < 17) {
            return getNmsClassOld(oldClassName);
        } else {
            return getNmsClassNew(newClassName);
        }
    }

    /**
     * Get a class from the net.minecraft in any version. Use 1.17 compatible names. If using any version prior to 1.17, the name after the last "." will be used
     * @param className The name of the for 1.17
     */
    public static Class<?> getMinecraftClass(String className) throws ClassNotFoundException {
        if (majorVersion < 17) {
            return getNmsClassOld(className.split("\\.")[className.split("\\.").length - 1]);
        } else {
            return getNmsClassNew(className);
        }
    }

    /**
     * Get a class from the net.minecraft in any version
     * Class should be registered in the map
     * @param className The name of the class
     */
    public static Class<?> getMinecraftClassFromMap(String className) throws ClassNotFoundException {
        if (!classMap.containsKey(className)) {
            throw new RuntimeException("Class entry not found in class map");
        }
        return getMinecraftClass(className, classMap.get(className));
    }

    /**
     * Get the Constructor of a Class
     * @param clazz The Class in which the Constructor is defined
     * @param args Arguments taken by the Constructor
     * @return Returns the Constructor
     * @throws NoSuchMethodException Thrown when no Constructor in the Class was found with the provided combination of arguments
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) throws NoSuchMethodException {
        Constructor<?> con = clazz.getConstructor(args);
        con.setAccessible(true);

        return con;
    }

    /**
     * Get an Enum from an Enum constant
     * @param clazz The Class in which the Enum is defined
     * @param constant The name of the Enum Constant
     * @return Returns the Enum or null if the Enum does not have a member called <i>constant</i>
     */
    public static Enum<?> getEnum(Class<?> clazz, String constant) {
        Enum<?>[] constants = (Enum<?>[]) clazz.getEnumConstants();

        for(Enum<?> e : constants) {
            if(e.name().equalsIgnoreCase(constant)) {
                return e;
            }
        }

        return null;
    }

    /**
     * Get an Enum constant by it's name and constant
     * @param clazz The Class in which the Enum is defined
     * @param enumname The name of the Enum
     * @param constant The name of the Constant
     * @return Returns the Enum or null if the Enum does not have a member called <i>constant</i>
     * @throws ClassNotFoundException If the Class does not have an Enum called <i>enumname</i>
     */
    public static Enum<?> getEnum(Class<?> clazz, String enumname, String constant) throws ClassNotFoundException {
        Class<?> c = Class.forName(clazz.getName() + "$" + enumname);
        return getEnum(c, constant);
    }

    /**
     * Get a Field
     * @param clazz The Class in which the Field is defined
     * @param fieldName The name of the Field
     * @return Returns the Field
     * @throws NoSuchFieldException Thrown when the Field was not present in the Class
     */
    public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        if (fieldCache.contains(clazz, fieldName)) {
            return fieldCache.get(clazz, fieldName);
        }
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        fieldCache.put(clazz, fieldName, f);
        return f;
    }

    /**
     * Get a Method
     * @param clazz The Class in which the Method is defined
     * @param methodName The name of the method
     * @param args The argument types the method takes
     * @return Returns the Method
     * @throws NoSuchMethodException
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... args) throws NoSuchMethodException {
        if (methodCache.contains(clazz, methodName)) {
//            return methodCache.get(clazz, methodName);
        }
        Method m = clazz.getDeclaredMethod(methodName, args);
        m.setAccessible(true);
        methodCache.put(clazz, methodName, m);
        return m;
    }

    /**
     * Invoke a Method which takes no arguments. The Class in which the Method is defined is derived from the provided Object
     * @param obj The object to invoke the method on
     * @param methodName The name of the Method
     * @return Returns the result of the method, can be null if the method returns void
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(Object obj, String methodName) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return invokeMethod(obj.getClass(), obj, methodName);
    }

    /**
     * Invoke a Method where the argument types are derived from the provided arguments. The Class in which the Method is defined is derived from the provided Object
     * @param obj The object to invoke the method on
     * @param methodName The name of the Method
     * @param args The arguments to pass to the Method
     * @return Returns the result of the method, can be null if the method returns void
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(Object obj, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return invokeMethod(obj.getClass(), obj, methodName, args);
    }

    /**
     * Invoke a Method where the argument types are explicitly given (Helpful when working with primitives). The Class in which the Method is defined is derived from the provided Object.
     * @param obj The Object to invoke the method on
     * @param methodName The name of the Method
     * @param argTypes The types of arguments as a Class array
     * @param args The arguments as an object array
     * @return Returns the result of the method, can be null if the method returns void
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeMethodExplicit(Class<?> clazz, Object obj, String methodName, Class<?>[] argTypes, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return invokeMethod(clazz, obj, methodName, argTypes, args);
    }
    /**
     * Invoke a Method where the argument types are explicitly given (Helpful when working with primitives). The Class in which the Method is defined is derived from the provided Object.
     * @param obj The Object to invoke the method on
     * @param methodName The name of the Method
     * @return Returns the result of the method, can be null if the method returns void
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeMethodExplicit(Class<?> clazz, Object obj, String methodName) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return invokeMethod(clazz, obj, methodName, new Class[] {}, new Object[] {});
    }

    /**
     * Invoke a Method where the Class where to find the method is explicitly given (Helpful if the method is located in a superclass). The argument types are derived from the provided arguments
     * @param clazz The Class where the method is located
     * @param obj The Object to invoke the method on
     * @param methodName The name of the method
     * @param args The arguments to be passed to the method
     * @return Returns the result of the method, can be null if the method returns void
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(Class<?> clazz, Object obj, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?>[] argTypes = new Class<?>[args.length];
        for(int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }

        return invokeMethod(clazz, obj, methodName, argTypes, args);
    }

    /**
     * Invoke a Method where the Class where the Method is defined is explicitly given, and the argument types are explicitly given
     * @param clazz The Class in which the Method is located
     * @param obj The Object on which to invoke the Method
     * @param methodName The name of the Method
     * @param argTypes Argument types
     * @param args Arguments to pass to the method
     * @return Returns the result of the method, can be null if the method returns void
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(Class<?> clazz, Object obj, String methodName, Class<?>[] argTypes, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method m = getMethod(clazz, methodName, argTypes);
        return m.invoke(obj, args);
    }

    /**
     * Invoke a Method where the Class where to find the method is explicitly given (Helpful if the method is located in a superclass). The argument types are derived from the provided arguments
     * @param clazz The Class where the method is located
     * @param methodName The name of the method
     * @param args The arguments to be passed to the method
     * @return Returns the result of the method, can be null if the method returns void
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeStaticMethod(Class<?> clazz, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?>[] argTypes = new Class<?>[args.length];
        for(int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }

        return invokeStaticMethodExplicit(clazz, methodName, argTypes, args);
    }

    /**
     * Invoke a static Method in a Class where method arguments types are explicitly given
     * @param clazz The Class where the method is located
     * @param methodName The name of the method
     * @param args The arguments to be passed to the method
     * @return Returns the result of the method, can be null if the method returns void
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeStaticMethodExplicit(Class<?> clazz, String methodName, Class<?>[] argTypes, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return invokeMethod(clazz, null, methodName, argTypes, args);
    }

    /**
     * Get the value of a Field, where the Class in which the field is defined is derived from the provided Object
     * @param obj The object in which the field is located, and from which to get the value
     * @param name The name of the Field to get the value from
     * @return Returns the value of the Field
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getObject(Object obj, String name) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        return getObject(obj.getClass(), obj, name);
    }

    /**
     * Get the value of a Field, where the Class in which the Field is defined is explicitly given. (Helpful when the Field is in a superclass)
     * @param obj The Object to get the value from
     * @param clazz The Class in which the Field is defined
     * @param name The name of the Field
     * @return Returns the value of the Field
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @deprecated Use {@link #getObject(Class, Object, String)} instead
     */
    @Deprecated
    public static Object getObject(Object obj, Class<?> clazz, String name) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        return getObject(clazz, obj, name);
    }

    /**
     * Get the value of a Field, where the Class in which the Field is defined is explicitly given. (Helpful when the Field is in a superclass)
     * @param clazz The Class in which the Field is defined
     * @param obj The Object to get the value from
     * @param name The name of the Field
     * @return Returns the value of the Field
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getObject(Class<?> clazz, Object obj, String name) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field f = getField(clazz, name);
        f.setAccessible(true);
        return f.get(obj);
    }

    /**
     * Invoke a Class' constructor. The argument types are derived from the provided arguments
     * @param clazz The Class in which the Constructor is defined
     * @param args The arguments to pass to the Constructor
     * @return Returns an instance of the provided Class in which the Constructor is located
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeConstructor(Class<?> clazz, Object... args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?>[] argTypes = new Class<?>[args.length];
        for(int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }

        return invokeConstructorExplicit(clazz, argTypes, args);
    }

    /**
     * Invoke a Class' Constructor, where the argument types are explicitly given (Helpful when working with primitives)
     * @param clazz The Class in which the Constructor is defined
     * @param argTypes The argument types
     * @param args The arguments to pass to the constructor
     * @return Returns an instance of the provided Class in which the Constructor is located
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeConstructorExplicit(Class<?> clazz, Class<?>[] argTypes, Object... args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = getConstructor(clazz, argTypes);
        return con.newInstance(args);
    }

    public static void registerInClassMap(String newName, String oldName) {
        classMap.put(newName, oldName);
    }

    public static void registerInClassMap(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            registerInClassMap(entry.getKey(), entry.getValue());
        }
    }

    // This method does not throw exceptions as it will never fail
    public static List<String> getEnumValues(Class<?> clazz) {
        if (!clazz.isEnum()) return Collections.emptyList();
        List<String> list = new ArrayList<>();
        try {
            Object[] values = clazz.getEnumConstants();
            for (Object value : values) {
                list.add((String) invokeMethod(clazz, value, "name"));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Object getMinecraftServer() {
        try {
            return invokeMethodExplicit(getBukkitClass("CraftServer"), Bukkit.getServer(), "getServer");
        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
