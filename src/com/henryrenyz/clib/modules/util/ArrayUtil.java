package com.henryrenyz.clib.modules.util;

import java.util.List;

/**
 * Array Utilities, for faster array processing.
 *
 * For the speed, there's no security check.
 */
public class ArrayUtil {

    public static char[] arrayCopy(char[] src, int srcPos, char[] dest, int destPos, int length) {
        for (int i = 0; i < length; i++) {
            dest[destPos + i] = src[srcPos + i];
        }
        return dest;
    }

    public static int[] arrayCopy(int[] src, int srcPos, int[] dest, int destPos, int length) {
        for (int i = 0; i < length; i++) {
            dest[destPos + i] = src[srcPos + i];
        }
        return dest;
    }

    public static char[] subArray(char[] src, int start, int end) {
        return arrayCopy(src, start, new char[end - start], 0, end - start);
    }

    public static int[] subArray(int[] src, int start, int end) {
        return arrayCopy(src, start, new int[end - start], 0, end - start);
    }

    public static char[] resize(char[] old, int size) {
        return arrayCopy(old, 0, new char[size], 0, Math.min(size, old.length));
    }

    public static int[] resize(int[] old, int size) {
        return arrayCopy(old, 0, new int[size], 0, Math.min(size, old.length));
    }

    public static <T> T get(List<T> list, int index) {
        return (list.size() - 1 <= index) ? list.get(index) : null;
    }

    public static <T> T get(T[] array, int index) {
        return (array.length > index) ? array[index] : null;
    }
}
