package com.example.aics.context;

public class BaseContext {
    private static final ThreadLocal<Long> userIdThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> roleThreadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        userIdThreadLocal.set(id);
    }

    public static Long getCurrentId() {
        return userIdThreadLocal.get();
    }

    public static void setCurrentRole(String role) {
        roleThreadLocal.set(role);
    }

    public static String getCurrentRole() {
        return roleThreadLocal.get();
    }

    public static void remove() {
        userIdThreadLocal.remove();
        roleThreadLocal.remove();
    }
}
