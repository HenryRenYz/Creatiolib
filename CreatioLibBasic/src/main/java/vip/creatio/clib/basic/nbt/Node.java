package vip.creatio.clib.basic.nbt;

import vip.creatio.common.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Node {

    private final String key;
    private Object value;
    private final Type type;

    private static final Pattern branketIndex = Pattern.compile("\\[(-?[0-9]+?)]");
    private static final Pattern branketAll = Pattern.compile("\\[]");
    private static final Pattern branketItem = Pattern.compile("\\[(\\{.*}?)]");
    private static final Pattern branketNBT = Pattern.compile("(\\{.*})");

    public Node(String node) {
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
            throw new RuntimeException("Invalid NBT signature: " + node);
        } else if (node.lastIndexOf('}') == node.length() - 1) {
            Matcher regNBT = branketNBT.matcher(node);
            if (regNBT.find()) {
                this.value = StringUtil.parseMap(regNBT.group(1));
                this.key = regNBT.replaceAll("");
                this.type = Type.NBT;
                return;
            }
            throw new RuntimeException("Invalid NBT signature: " + node);
        }
        this.key = node;
        this.type = Type.NONE;
    }

    public static Queue<Node> fromPath(String nbtPath) {
        List<String> list = StringUtil.splitJson(nbtPath, '.');
        Queue<Node> l = new LinkedList<>();
        for (String s : list) {
            l.add(new Node(s));
        }
        return l;
    }

    @SuppressWarnings("unchecked")
    public @Nullable NBTTag<?> get(CompoundTag element) {
        NBTTag<?> base = element.get(this.key);
        switch (this.type) {
            case NONE:
                return base;
            case NBT:
                if (base instanceof CompoundTag) {
                    boolean t = true;
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
                        if (!match(((CompoundTag) base).get(entry.getKey()), entry.getValue())) t = false;
                    }
                    return (t) ? base : null;
                }
                break;
            case ALL:
                if (base instanceof CollectionTag) {
                    return base;
                }
                break;
            case INDEX:
                if (base instanceof CollectionTag) {
                    int v = (Integer) this.value;
                    if (v < 0) v = ((CollectionTag<?>) base).size() + v;
                    return (NBTTag<?>) ((CollectionTag<?>) base).get(v);
                }
                break;
            case ITEM:
                if (base instanceof CollectionTag) {
                    for (NBTTag<?>e : (CollectionTag<?>) base) {
                        if (e instanceof CompoundTag) {
                            for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
                                if (match(((CompoundTag) e).get(entry.getKey()), entry.getValue())) {
                                    return (NBTTag<?>) e;
                                }
                            }
                        }
                    }
                }
                break;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static boolean match(NBTTag<?> base, Object value) {
        if (value == null) return true;
        if (base != null) {
            if (base instanceof NumbericTag) {
                return base.toString().equalsIgnoreCase((String) value);
            } else if (base instanceof CompoundTag) {
                if (value instanceof Map) {
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
                        if (!match(((CompoundTag) base).get(entry.getKey()), entry.getValue())) return false;
                    }
                    return true;
                }
            } else if (base instanceof CollectionTag) {
                if (value instanceof List) {
                    CollectionTag<?> ar = (CollectionTag<?>) base;
                    int match = 0;
                    for (Object l : (List<?>) value) {
                        for (NBTTag<?> nbtBase : ar) {
                            if (match(nbtBase, l)) match++;
                        }
                    }
                    return match == ((List<?>) value).size();
                }
            } else if (base instanceof StringTag) {
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

    enum Type {
        NONE,   //None
        INDEX,  //Element index
        ITEM,   //First element with specific tag
        ALL,    //All elements in a list
        NBT;    //NBTCompound with specific tag
    }
}
