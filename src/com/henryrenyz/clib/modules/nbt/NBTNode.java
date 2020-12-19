package com.henryrenyz.clib.modules.nbt;

import com.henryrenyz.clib.modules.exception.MapParseException;
import com.henryrenyz.clib.modules.util.StringUtil;
import org.jetbrains.annotations.Nullable;;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NBTNode {

    private String key;
    private Object value;
    private Type type;

    private static final Pattern branketIndex = Pattern.compile("\\[(-?[0-9]+?)]");
    private static final Pattern branketAll = Pattern.compile("\\[]");
    private static final Pattern branketItem = Pattern.compile("\\[(\\{.*}?)]");
    private static final Pattern branketNBT = Pattern.compile("(\\{.*})");

    public NBTNode(String node) {
        if (node.lastIndexOf(']') == node.length() - 1) {
            Matcher regList = branketIndex.matcher(node);
            if (regList.find()) {
                int index = Integer.parseInt(regList.group(1));
                this.key = regList.replaceAll("");
                this.value = index;
                this.type = Type.INDEX;
                return;
            } else if (node.lastIndexOf('[') == node.length() - 2) {
                Matcher regAll = branketAll.matcher(node);
                if (regAll.find()) {
                    this.key = regAll.replaceAll("");
                    type = Type.ALL;
                    return;
                }
            } else if (node.lastIndexOf('}') == node.length() - 2) {
                Matcher regItem = branketItem.matcher(node);
                if (regItem.find()) {
                    this.value = StringUtil.parseMap(regItem.group(1));
                    this.key = regItem.replaceAll("");
                    this.type = Type.ITEM;
                    return;
                }
            }
            throw new MapParseException("Invalid NBT signature: " + node);
        } else if (node.lastIndexOf('}') == node.length() - 1) {
            Matcher regNBT = branketNBT.matcher(node);
            if (regNBT.find()) {
                this.value = StringUtil.parseMap(regNBT.group(1));
                this.key = regNBT.replaceAll("");
                this.type = Type.NBT;
                return;
            }
            throw new MapParseException("Invalid NBT signature: " + node);
        }
        this.key = node;
        this.type = Type.NONE;
    }

    public static Queue<NBTNode> fromPath(String nbtPath) {
        List<String> list = StringUtil.splitJson(nbtPath, '.');
        Queue<NBTNode> l = new LinkedList<>();
        for (String s : list) {
            l.add(new NBTNode(s));
        }
        return l;
    }

    public @Nullable NBTBase get(NBTCompound element) {
        NBTBase base = element.get(this.key);
        switch (this.type) {
            case NONE:
                return base;
            case NBT:
                if (base instanceof NBTCompound) {
                    boolean t = true;
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
                        if (!match(((NBTCompound) base).get(entry.getKey()), entry.getValue())) t = false;
                    }
                    return (t) ? base : null;
                }
                break;
            case ALL:
                if (base instanceof NBTArray) {
                    return base;
                }
                break;
            case INDEX:
                if (base instanceof NBTArray) {
                    int v = (Integer) this.value;
                    if (v < 0) v = ((NBTArray) base).size() + v;
                    return ((NBTArray<NBTBase>) base).get(v);
                }
                break;
            case ITEM:
                if (base instanceof NBTArray) {
                    for (NBTBase e : (NBTArray<NBTBase>) base) {
                        if (e instanceof NBTCompound) {
                            for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
                                if (match(((NBTCompound) e).get(entry.getKey()), entry.getValue())) {
                                    return e;
                                }
                            }
                        }
                    }
                }
                break;
        }
        return null;
    }

    private static @Nullable boolean match(NBTBase base, Object value) {
        if (value == null) return true;
        if (base != null) {
            if (base instanceof NBTNumber) {
                return base.toString().equalsIgnoreCase((String) value);
            } else if (base instanceof NBTCompound) {
                if (value instanceof Map) {
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
                        if (!match(((NBTCompound) base).get(entry.getKey()), entry.getValue())) return false;
                    }
                    return true;
                }
            } else if (base instanceof NBTArray) {
                if (value instanceof List) {
                    NBTArray<?> ar = (NBTArray<?>) base;
                    int match = 0;
                    for (Object l : (List<?>) value) {
                        for (NBTBase nbtBase : ar) {
                            if (match(nbtBase, l)) match++;
                        }
                    }
                    return match == ((List<?>) value).size();
                }
            } else if (base instanceof NBTString) {
                if (value instanceof String) {
                    return base.toString().equals(value);
                }
            }
        }
        return false;
    }

    public String toString() {
        return '{' + this.key + '=' + this.value + '(' + this.type + ')' + '}';
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }
}

enum Type {
    NONE,   //None
    INDEX,  //Element index
    ITEM,   //First element with specific tag
    ALL,    //All elements in a list
    NBT;    //NBTCompound with specific tag
}
