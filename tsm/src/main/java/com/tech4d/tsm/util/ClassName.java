package com.tech4d.tsm.util;

public class ClassName {
    public static String getClassName(Object obj) {
        String className = obj.getClass().getName();
        String[] tokens = className.split("\\.");
        int lastToken = tokens.length - 1;
        return tokens[lastToken];
    }

}
