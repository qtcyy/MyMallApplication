package org.example.mymallapplication.common;

/**
 * @author chengyiyang
 */
public class BaseContext {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static String getCurrentId() {
        return threadLocal.get();
    }

    public static void setCurrentId(String id) {
        threadLocal.set(id);
    }

    public static void clear() {
        threadLocal.remove();
    }
}
