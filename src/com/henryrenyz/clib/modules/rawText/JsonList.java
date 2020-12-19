package com.henryrenyz.clib.modules.rawText;

import com.henryrenyz.clib.modules.exception.JsonParseException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JsonList implements JsonBase {

    private List<JsonBase> element = new ArrayList<>();

    public JsonList(@NotNull String... rawText) {
        for (String raw : rawText) {
            String[] d;
            if (raw.charAt(0) == '"' && raw.charAt(1) == '[') {
                d = raw.substring(2, raw.length() - 2).split(",");

            } else if (raw.charAt(0) == '[') {
                d = raw.substring(1, raw.length() - 1).split(",");
            } else throw new JsonParseException("Invalid list signature!");
            for (String s : d) {
                element.add(JsonBase.toBase(s));
            }
        }
    }

    JsonList() {}
    public JsonList(@NotNull JsonBase... list) {
        element = Arrays.asList(list);
    }
    public JsonList(@NotNull Collection<JsonBase> list) {
        element = (List<JsonBase>) list;
    }

    JsonList(@NotNull List<Object> rawList) {
        for (Object o : rawList) {
            if (o instanceof Map) element.add(Json.deserialize((Map<String, Object>) o));
            else if (o instanceof List) element.add(new JsonList((List<Object>) o));
            else element.add(JsonBase.toBase((String) o));
        }
    }

    String singleLine(TextColor... inherit) {
        StringBuilder s = new StringBuilder();
        TextColor[] inh = inherit;
        for (JsonBase b : element) {
            s.append(b.toSingleLine(inh));
            inh = b.getInherited();
        }
        return s.toString();
    }

    public JsonList add(JsonBase... element) {
        this.element.addAll(Arrays.asList(element));
        return this;
    }
    public JsonList add(Collection<JsonBase> element) {
        this.element.addAll(element);
        return this;
    }

    public JsonList add(int index, JsonBase element) {
        this.element.add(index, element);
        return this;
    }

    public JsonList add(JsonBase element) {
        this.element.add(this.element.size(), element);
        return this;
    }

    public JsonList set(JsonBase... element) {
        this.element = Arrays.asList(element);
        return this;
    }
    public JsonList set(Collection<JsonBase> element) {
        this.element = (List<JsonBase>) element;
        return this;
    }

    public JsonList remove(JsonBase... element) {
        this.element.removeAll(Arrays.asList(element));
        return this;
    }
    public JsonList remove(Collection<JsonBase> element) {
        this.element.removeAll(element);
        return this;
    }

    public JsonList remove(int index) {
        this.element.remove(index);
        return this;
    }

    public void clear() {
        this.element.clear();
    }

    @Override
    public String getJSON() {
        StringBuilder b = new StringBuilder().append('[');
        int k = 0;
        for (JsonBase e : element) {
            if (k > 0) b.append(',');
            b.append(e.getJSON());
            k++;
        }
        b.append(']');
        return b.toString();
    }

    public boolean isEmpty() {
        return element.isEmpty();
    }

    public String toString() {
        return getJSON();
    }

    @Override
    public JsonType getType() {
        return JsonType.List;
    }

    @Override
    public TextColor[] getInherited() {
        return element.get(size() - 1).getInherited();
    }

    public JsonBase get(int index) {
        return element.get(index);
    }

    public List<JsonBase> listAll() {
        return element;
    }

    @Override
    public JsonList clone() {
        try {
            return (JsonList) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int size() {
        return element.size();
    }
}
