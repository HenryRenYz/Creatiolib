package com.henryrenyz.clib.MultiblockAPI;

import com.henryrenyz.clib.modules.Message;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RegisteredStructure implements ConfigurationSerializable {

    /**
     @param name_space the system name of structure
     @param name the name of structure
     @param center the center block
     @param unstable unregister when structure is damaged
     @param load_chunk keep chunk loaded
     @param flippable let the structure flippable
     @param corner the local coords of corner block from center block
     @param pattern the structure pattern, ench element in the list refers
     to a layer, each layer build up by a list of blockdata collection.
     */


    private final String name_space;
    private final String name;
    private final Material center;
    private final boolean unstable;
    private final boolean load_chunk;
    private final int load_chunk_radius;
    private final boolean flippable;
    private final boolean overlappable;
    private final boolean require_update;
    private final Class<?> hooked_machine_class;

    private final Integer[] corner = new Integer[3];
    private final List<List<Object[][]>> pattern;
    private final Map<String, Object> material_code;


    public RegisteredStructure(String name_space, String name, Material center, boolean unstable, boolean load_chunk,
                               int load_chunk_radius, boolean flippable, boolean overlappable, boolean require_update, Class<?> hooked_machine,
                               List<Integer> corner, List<List<String>> pattern, Map<String, Object> material_code) {
        //Anti duplicate name space checker
        if (StructureManager.getRegisteredStructures() != null) {
            for (RegisteredStructure r : StructureManager.getRegisteredStructures()) {
                if (r.getNameSpace().equalsIgnoreCase(name_space)) {
                    StructureManager.getRegisteredStructures().remove(r);
                }
                break;
            }
        }

        this.name_space = name_space;
        this.name = Message.charReplace(name);
        this.center = center;
        this.unstable = unstable;
        this.load_chunk = load_chunk;
        this.load_chunk_radius = load_chunk_radius;
        this.flippable = flippable;
        this.require_update = require_update;
        this.overlappable = overlappable;
        this.hooked_machine_class = hooked_machine;
        this.material_code = material_code;

        for (int i = 0; i < corner.size(); i++) {
            this.corner[i] = corner.get(i);
        }

        this.pattern = decodePattern(pattern, material_code);
        StructureManager.getRegisteredStructures().add(this);
    }


    private List<List<Object[][]>> decodePattern(List<List<String>> pattern, Map<String, Object> material) {

        //Get password Map of pattern
        Map<String, Object[]> passwordMap = new HashMap<>();
        for (String key : material.keySet()) {
            String[] t = material.get(key).toString().split(" ");
            Object[] l = new Object[t.length];
            l[0] = Material.getMaterial(t[0]);
            System.arraycopy(t, 1, l, 1, l.length - 1);
            passwordMap.put(key, l);
        }

        //Decode pattern
        List<List<Object[][]>> cube = new ArrayList<>();
        for (List<String> list : pattern) {
            List<Object[][]> layer = new ArrayList<>();
            for (String code : list) {
                Object[][] line = new Object[code.length()][];
                String c;
                for (int i = 0; i < code.length(); i++) {
                    c = Character.toString(code.charAt(i));
                    if (c.equalsIgnoreCase(" ")) {
                        line[i] = null;
                    } else {
                        line[i] = passwordMap.get(c);
                    }
                }
                layer.add(line);
            }
            cube.add(layer);
        }
        return cube;
    }

    //Serialization Section
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map0 = new HashMap<>(), map = new HashMap<>(), map1 = new HashMap<>();
        map0.put("id", this.name_space);

        map.put("NAME", this.name);
        map.put("CENTER_BLOCK", this.center);
        map.put("UNSTABLE", this.unstable);
        map.put("LOAD_CHUNK", this.load_chunk);
        map.put("LOAD_CHUNK_RADIUS", this.load_chunk_radius);
        map.put("CORNER", Arrays.asList(this.corner));
        map.put("FLIPPABLE", this.flippable);
        map.put("OVERLAPPABLE", this.overlappable);
        map.put("REQUIRE_UPDATE", this.require_update);
        map.put("HOOKED_MACHINE", this.hooked_machine_class.getSimpleName());
        map1.put("PATTERN", this.pattern);
        map1.put("BLOCKS", this.material_code);
        map.put("MAP", map1);

        map0.put("data" ,map);
        return map0;
    }

    public static RegisteredStructure deserialize(Map<String, Object> map) {
        Map<String, Object> map0 = (map.get("data") != null ? (Map<String, Object>) map.get("data") : null);
        Map<String, Object> map1 = (map0.get("MAP") != null ? (Map<String, Object>) map0.get("MAP") : null);
        return new RegisteredStructure(
                (map.get("id") != null ? (String) map.get("id") : null),
                (map0.get("NAME") != null ? (String) map0.get("NAME") : "Noname"),
                (map0.get("CENTER_BLOCK") != null ? Material.getMaterial(map0.get("CENTER_BLOCK").toString()) : null),
                (map0.get("UNSTABLE") != null ? (Boolean) map0.get("UNSTABLE") : true),
                (map0.get("LOAD_CHUNK") != null ? (Boolean) map0.get("LOAD_CHUNK") : false),
                (map0.get("LOAD_CHUNK_RADIUS") != null ? (Integer) map0.get("LOAD_CHUNK_RADIUS") : 16),
                (map0.get("FLIPPABLE") != null ? (Boolean) map0.get("FLIPPABLE") : false),
                (map0.get("OVERLAPPABLE") != null ? (Boolean) map0.get("OVERLAPPABLE") : false),
                (map0.get("REQUIRE_UPDATE") != null ? (Boolean) map0.get("REQUIRE_UPDATE") : false),
                (map0.get("HOOKED_MACHINE") != null ? StructureManager.getMachineClass((String) map0.get("HOOKED_MACHINE")) : null),
                (map0.get("CORNER") != null ? (List<Integer>) map0.get("CORNER") : null),
                (map1.get("PATTERN") != null ? (List<List<String>>) map1.get("PATTERN") : null),
                (map1.get("BLOCKS") != null ? (Map<String, Object>) map1.get("BLOCKS") : null)
                );
    }


    public String getNameSpace() {
        return this.name_space;
    }

    public String getName() {
        return this.name;
    }

    public Material getCenter() {
        return this.center;
    }

    public boolean isUnstable() {
        return this.unstable;
    }

    public boolean isFlippable() {
        return this.flippable;
    }

    public boolean isRequireUpdate() {
        return this.require_update;
    }

    public boolean isOverlappable() {
        return this.overlappable;
    }

    public boolean loadChunk() {
        return this.load_chunk;
    }

    public int getLoadRadius() {
        return this.load_chunk_radius;
    }

    public Integer[] getCorner() {
        return this.corner;
    }

    public Class<?> getMachineClass() {
        return this.hooked_machine_class;
    }

    public List<List<Object[][]>> getPattern() {
        return this.pattern;
    }

    public Map<String, Object> getMaterialCode() {
        return this.material_code;
    }
}
