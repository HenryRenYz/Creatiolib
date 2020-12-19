package com.henryrenyz.clib.modules.nbt;

import com.henryrenyz.clib.modules.reflection.ReflectionClass;
import com.henryrenyz.clib.modules.reflection.ReflectionConstructor;
import com.henryrenyz.clib.modules.reflection.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;;

import java.util.*;
import java.util.regex.Pattern;

import static com.henryrenyz.clib.modules.reflection.ReflectionMethod.*;

@SuppressWarnings("unchecked")
public class NBTCompound implements NBTBase{

    private Object compound;
    private static final ReflectionClass clazz = ReflectionClass.NBTTagCompound;

    public NBTCompound(@Nullable String nbt) throws Exception {
        if (nbt != null) {
            this.compound = ReflectionUtils.getNMSNBT(nbt);
        }
    }

    NBTCompound(@NotNull Object nbtTagCompound) {
        this.compound = nbtTagCompound;
    }

    public NBTCompound(@NotNull NBTCompound clone) {
        this.compound = run(clazz, "clone", clone.getRaw());
    }

    public NBTCompound() {
        this.compound = ReflectionConstructor.run(clazz.c);
    }

    //Set Original NBTTagCompound Object
    public void setCompound(@NotNull Object compound) {
        this.compound = compound;
    }

    //Get NBTBase from vanilla nbtPath
    public @Nullable NBTBase path(@NotNull String nbtPath) {
        //Convert nbtPath to nbtSectionNode
        Queue<NBTNode> node = NBTNode.fromPath(nbtPath);
        try {
            NBTBase item = Objects.requireNonNull(node.poll()).get(this);
            while (!node.isEmpty()) {
                if (item instanceof NBTCompound) {
                    item = node.poll().get((NBTCompound) item);
                }
            }
            return item;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public boolean hasNBT(String rawNbt) {
        try {
            return contains(new NBTCompound(rawNbt));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean contains(NBTCompound base) {
        try {
            for (String key : base.getKeys()) {
                NBTBase item = base.get(key);
                switch (item.getType()) {
                    case INT:
                        if (((NBTNumber) item).asInt() != getInt(key)) return false;
                        break;
                    case BYTE:
                        if (((NBTNumber) item).asByte() != getByte(key)) return false;
                        break;
                    case LONG:
                        if (((NBTNumber) item).asLong() != getLong(key)) return false;
                        break;
                    case FLOAT:
                        if (((NBTNumber) item).asFloat() != getFloat(key)) return false;
                        break;
                    case DOUBLE:
                        if (((NBTNumber) item).asDouble() != getDouble(key)) return false;
                        break;
                    case SHORT:
                        if (((NBTNumber) item).asShort() != getShort(key)) return false;
                        break;
                    case STRING:
                        String s = item.toString();
                        if (!s.substring(1, s.length() - 1).equals(getString(key))) return false;
                        break;
                    case INTARRAY:
                        if (((NBTIntArray) item).size() == getIntArray(key).length) {
                            int[] array = getIntArray(key);
                            for (int i = 0; i < ((NBTIntArray) item).size(); i++) {
                                if (((NBTIntArray) item).get(i).asInt() != array[i]) return false;
                            }
                        } else return false;
                        break;
                    case BYTEARRAY:
                        if (((NBTIntArray) item).size() == getByteArray(key).length) {
                            byte[] array = getByteArray(key);
                            for (int i = 0; i < ((NBTIntArray) item).size(); i++) {
                                if (((NBTIntArray) item).get(i).asByte() != array[i]) return false;
                            }
                        } else return false;
                        break;
                    case LONGARRAY:
                        if (((NBTIntArray) item).size() == getLongArray(key).length) {
                            long[] array = getLongArray(key);
                            for (int i = 0; i < ((NBTIntArray) item).size(); i++) {
                                if (((NBTIntArray) item).get(i).asLong() != array[i]) return false;
                            }
                        } else return false;
                        break;
                    case LIST:
                        for (NBTBase b : (NBTList) item) {
                            if (b instanceof NBTCompound) {
                                if (!getCompound(key).contains((NBTCompound) b)) return false;
                            }
                        }
                        break;
                    case COMPOUND:
                        if (!getCompound(key).contains((NBTCompound) item)) return false;
                        break;
                }
            }
        } catch (NullPointerException ignored) {
            return false;
        }
        return true;
    }

    //Get a collection of keys
    public Set<String> getKeys() {
        if (compound != null) {
            return (Set<String>) run(clazz, "getKeys", compound);
        }
        return new HashSet<>();
    }

    //Get the map of compound (cannot be modify directly)
    public Map<String, Object> getMap() {
        if (compound != null) {
            return (Map<String, Object>) run(clazz, "h", compound);
        }
        return new HashMap<>();
    }

    //Get the size of the map above
    public int getMapSize() {
        if (compound != null) {
            return (int) run(clazz, "e", compound);
        }
        return 0;
    }

    //Set values
    public NBTCompound setByte(String key, byte value) {                    //Byte
        run(clazz, "setByte", compound, key, value);
        return this;
    }
    public NBTCompound setShort(String key, short value) {                  //Short
        run(clazz, "setShort", compound, key, value);
        return this;
    }
    public NBTCompound setInt(String key, int value) {                      //Integer
        run(clazz, "setInt", compound, key, value);
        return this;
    }
    public NBTCompound setLong(String key, long value) {                    //Long
        run(clazz, "setLong", compound, key, value);
        return this;
    }
    public NBTCompound setFloat(String key, float value) {                  //Float
        run(clazz, "setFloat", compound, key, value);
        return this;
    }
    public NBTCompound setDouble(String key, double value) {                //Double
        run(clazz, "setDouble", compound, key, value);
        return this;
    }
    public NBTCompound setString(String key, String value) {                //String
        run(clazz, "setString", compound, key, value);
        return this;
    }
    public NBTCompound setByteArray(String key, byte... values) {            //Byte Array
        run(clazz, "setByteArray", compound, key, values);
        return this;
    }
    public NBTCompound setIntArray(String key, int... values) {              //Integer Array
        run(clazz, "setIntArray", compound, key, values);
        return this;
    }
    public NBTCompound setIntArray(String key, List<Integer> values) {      //Integer List
        run(clazz, "b", compound, key, values);
        return this;
    }
    public NBTCompound setLongArray(String key, long... values) {            //Long Array
        run(clazz, "a", compound, key, values);
        return this;
    }
    public NBTCompound setLongArray(String key, List<Long> values) {        //Long List
        run(clazz, "c", compound, key, values);
        return this;
    }
    public NBTCompound setUUID(String key, UUID value) {                    //UUID
        run(clazz, "a", compound, key, value);
        return this;
    }
    public NBTCompound setBoolean(String key, boolean value) {              //Boolean
        run(clazz, "setBoolean", compound, key, value);
        return this;
    }
    public NBTCompound set(String key, NBTBase value) {                     //General Set
        run(NBTTagCompound_set.m,compound, key, value.getRaw());
        return this;
    }

    //Get value
    public byte getByte(String key) {                                       //Get Byte
        return (byte) run(clazz, "getByte", compound, key);
    }
    public short getShort(String key) {                                     //Get Short
        return (short) run(clazz, "getShort", compound, key);
    }
    public int getInt(String key) {                                         //Get Integer
        return (int) run(clazz, "getInt", compound, key);
    }
    public long getLong(String key) {                                       //Get Long
        return (long) run(clazz, "getLong", compound, key);
    }
    public float getFloat(String key) {                                     //Get Float
        return (float) run(clazz, "getFloat", compound, key);
    }
    public double getDouble(String key) {                                   //Get Double
        return (double) run(clazz, "getDouble", compound, key);
    }
    public String getString(String key) {                                   //Get String
        return (String) run(clazz, "getString", compound, key);
    }
    public byte[] getByteArray(String key) {                                //Get Byte Array
        return (byte[]) run(clazz, "getByteArray", compound, key);
    }
    public int[] getIntArray(String key) {                                  //Get Int Array
        return (int[]) run(clazz, "getIntArray", compound, key);
    }
    public long[] getLongArray(String key) {                                //Get Long Array
        return (long[]) run(clazz, "getLongArray", compound, key);
    }
    public NBTCompound getCompound(String key) {                            //Get NBTCompound
        return new NBTCompound(run(clazz, "getCompound", compound, key));
    }
    public NBTList getList(String key, NBTType type) {                      //Get NBTList of specific type
        return new NBTList(run(clazz, "getList", compound, key, (int) type.getId()));
    }
    public UUID getUUID(String key) {                                       //Get UUID
        return (UUID) run(clazz, "a", compound, key);
    }
    public NBTBase get(String key) {                                        //Get NBTBase of any element
        return NBTBase.toBase(run(clazz, "get", compound, key));
    }


    //Contains
    public boolean hasKey(String key) {                                     //If NBT has a key
        return (boolean) run(NBTTagCompound_hasKey, compound, key);
    }
    public boolean hasKeyOfType(String key, NBTType type) {                 //If NBT has a key of a type
        return (boolean) run(clazz, "hasKeyOfType", compound, key, (int) type.getId());
    }



    public String getNBT() {
        return toString();
    }

    @Override
    public Object getRaw() {
        return this.compound;
    }

    @Override
    public String toString() {
        if (compound != null) {
            return (String) run(clazz, "toString", compound);
        }
        return "{}";
    }

    @Override
    public NBTType getType() {
        return NBTType.COMPOUND;
    }

    @Override
    public NBTBase clone() {
        return new NBTCompound(run(clazz, "clone", compound));
    }
}
