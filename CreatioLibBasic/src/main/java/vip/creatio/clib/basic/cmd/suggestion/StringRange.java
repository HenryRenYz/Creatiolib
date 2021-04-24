package vip.creatio.clib.basic.cmd.suggestion;

import vip.creatio.clib.basic.tools.Wrapper;

public class StringRange implements Wrapper<com.mojang.brigadier.context.StringRange> {

    private final com.mojang.brigadier.context.StringRange range;

    StringRange(com.mojang.brigadier.context.StringRange raw) {
        this.range = raw;
    }

    public StringRange(int start, int end) {
        this(new com.mojang.brigadier.context.StringRange(start, end));
    }

    public static StringRange at(int pos) {
        return new StringRange(pos, pos);
    }

    public static StringRange between(int start, int end) {
        return new StringRange(start, end);
    }

    public static StringRange encompassing(StringRange a, StringRange b) {
        return new StringRange(Math.min(a.getStart(), b.getStart()), Math.max(a.getEnd(), b.getEnd()));
    }

    public int getStart() {
        return range.getStart();
    }

    public int getEnd() {
        return range.getEnd();
    }

    public String get(String string) {
        return string.substring(getStart(), getEnd());
    }

    public boolean isEmpty() {
        return range.isEmpty();
    }

    public int getLength() {
        return range.getLength();
    }

    public boolean equals(Object o) {
        if (o instanceof StringRange) {
            return range.equals(((StringRange) o).unwrap());
        }
        return false;
    }

    public int hashCode() {
        return range.hashCode() * 31;
    }

    public String toString() {
        return range.toString();
    }

    @Override
    public com.mojang.brigadier.context.StringRange unwrap() {
        return range;
    }

    @Override
    public Class<? extends com.mojang.brigadier.context.StringRange> wrappedClass() {
        return com.mojang.brigadier.context.StringRange.class;
    }
}
