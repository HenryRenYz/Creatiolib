package vip.creatio.clib.basic.nbt;

import vip.creatio.accessor.Func;
import vip.creatio.accessor.Reflection;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MojangsonParser;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CompoundTag
extends AbstractMap<String, NBTTag<?>>
implements NBTTag<NBTTagCompound> {

    private NBTTagCompound compound;

    public CompoundTag(@NotNull String nbt) {
        try {
            this.compound = MojangsonParser.parse(nbt);
        } catch (CommandSyntaxException e) {
            System.err.println("Invalid nbt!: \n" + nbt);
            throw new RuntimeException(e);
        }
    }

    public CompoundTag() {
        this.compound = new NBTTagCompound();
    }

    public CompoundTag(@NotNull NBTTagCompound compound) {
        this.compound = compound;
    }

    /** Clone a compound */
    public CompoundTag(@NotNull CompoundTag clone) {
        this.compound = clone.compound.clone();
    }

    public static CompoundTag create() {
        return new CompoundTag();
    }

    public static CompoundTag of(@NotNull String nbt) {
        return new CompoundTag(nbt);
    }

    //Set Original NBTTagCompound Object
    public void setCompound(@NotNull NBTTagCompound compound) {
        this.compound = compound;
    }

    public @NotNull NBTTagCompound getCompound() {
        return this.compound;
    }

    @Override
    public @NotNull NBTTagCompound unwrap() {
        return getCompound();
    }

    @Override
    public Class<? extends NBTTagCompound> wrappedClass() {
        return NBTTagCompound.class;
    }

    @Override
    public boolean isEmpty() {
        return compound.isEmpty();
    }

    private static final Func<Map<String, NBTBase>> ENTRIES = Reflection.method(NBTTagCompound.class, "h");
    @NotNull
    public Set<Entry<String, NBTTag<?>>> entrySet() {
        Map<String, NBTBase> map = ENTRIES.invoke(compound);
        Map<String, NBTTag<?>> newMap = new HashMap<>();
        map.forEach((s, b) -> newMap.put(s, NBTTag.wrap(b)));

        return newMap.entrySet();
    }

    //Get NBTBase from vanilla nbtPath
    public @Nullable NBTTag<?> path(@NotNull String nbtPath) {
        //Convert nbtPath to nbtSectionNode
        Queue<Node> node = Node.fromPath(nbtPath);
        try {
            NBTTag<?> item = Objects.requireNonNull(node.poll()).get(this);
            while (!node.isEmpty()) {
                if (item instanceof CompoundTag) {
                    item = node.poll().get((CompoundTag) item);
                }
            }
            return item;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public boolean hasNBT(String rawNbt) {
        return contains(new CompoundTag(rawNbt));
    }

    public boolean contains(CompoundTag base) {
        try {
            for (String key : base.keySet()) {
                NBTTag<?> item = (NBTTag<?>) base.get(key);
                switch (item.getType()) {
                    case INT:
                        if (((IntTag) item).asInt() != getInt(key)) return false;
                        break;
                    case BYTE:
                        if (((ByteTag) item).asByte() != getByte(key)) return false;
                        break;
                    case LONG:
                        if (((LongTag) item).asLong() != getLong(key)) return false;
                        break;
                    case FLOAT:
                        if (((FloatTag) item).asFloat() != getFloat(key)) return false;
                        break;
                    case DOUBLE:
                        if (((DoubleTag) item).asDouble() != getDouble(key)) return false;
                        break;
                    case SHORT:
                        if (((ShortTag) item).asShort() != getShort(key)) return false;
                        break;
                    case STRING:
                        String s = item.toString();
                        if (!s.substring(1, s.length() - 1).equals(getString(key))) return false;
                        break;
                    case INTARRAY:
                        if (((IntArrayTag) item).size() == getIntArray(key).length) {
                            int[] array = getIntArray(key);
                            for (int i = 0; i < ((IntArrayTag) item).size(); i++) {
                                if (((IntArrayTag) item).get(i).asInt() != array[i]) return false;
                            }
                        } else return false;
                        break;
                    case BYTEARRAY:
                        if (((IntArrayTag) item).size() == getByteArray(key).length) {
                            byte[] array = getByteArray(key);
                            for (int i = 0; i < ((IntArrayTag) item).size(); i++) {
                                if (((IntArrayTag) item).get(i).asByte() != array[i]) return false;
                            }
                        } else return false;
                        break;
                    case LONGARRAY:
                        if (((IntArrayTag) item).size() == getLongArray(key).length) {
                            long[] array = getLongArray(key);
                            for (int i = 0; i < ((IntArrayTag) item).size(); i++) {
                                if (((IntArrayTag) item).get(i).asLong() != array[i]) return false;
                            }
                        } else return false;
                        break;
                    case LIST:
                        for (NBTTag<?> b : (ListTag<?>) item) {
                            if (b instanceof CompoundTag) {
                                if (!getCompound(key).contains((CompoundTag) b)) return false;
                            }
                        }
                        break;
                    case COMPOUND:
                        if (!getCompound(key).contains((CompoundTag) item)) return false;
                        break;
                }
            }
        } catch (NullPointerException ignored) {
            return false;
        }
        return true;
    }

    //Get a collection of keys
    @Override
    public @NotNull Set<String> keySet() {
        return compound.getKeys();
    }

    //Get the size of the map above
    @Override
    public int size() {
        return compound.e();
    }

    //Set values
    public CompoundTag putByte(String key, byte value) {                    //Byte
        compound.setByte(key, value);
        return this;
    }
    public CompoundTag putShort(String key, short value) {                  //Short
        compound.setShort(key, value);
        return this;
    }
    public CompoundTag putInt(String key, int value) {                      //Integer
        compound.setInt(key, value);
        return this;
    }
    public CompoundTag putLong(String key, long value) {                    //Long
        compound.setLong(key, value);
        return this;
    }
    public CompoundTag putFloat(String key, float value) {                  //Float
        compound.setFloat(key, value);
        return this;
    }
    public CompoundTag putDouble(String key, double value) {                //Double
        compound.setDouble(key, value);
        return this;
    }
    public CompoundTag putString(String key, String value) {                 //String
        compound.setString(key, value);
        return this;
    }
    public CompoundTag putByteArray(String key, byte... values) {            //Byte Array
        compound.setByteArray(key, values);
        return this;
    }
    public CompoundTag putIntArray(String key, int... values) {              //Integer Array
        compound.setIntArray(key, values);
        return this;
    }
    public CompoundTag putIntArray(String key, List<Integer> values) {      //Integer List
        compound.b(key, values);
        return this;
    }
    public CompoundTag putLongArray(String key, long... values) {            //Long Array
        compound.a(key, values);
        return this;
    }
    public CompoundTag putLongArray(String key, List<Long> values) {        //Long List
        compound.c(key, values);
        return this;
    }
    public CompoundTag putUUID(String key, UUID value) {                    //UUID
        compound.a(key, value);
        return this;
    }
    public CompoundTag putBoolean(String key, boolean value) {              //Boolean
        compound.setBoolean(key, value);
        return this;
    }
    @NotNull
    public CompoundTag put(String key, NBTTag<?> value) {                   //General Set
        compound.set(key, value.unwrap());
        return this;
    }

    //Get value
    public byte getByte(String key) {                                       //Get Byte
        return compound.getByte(key);
    }
    public short getShort(String key) {                                     //Get Short
        return compound.getShort(key);
    }
    public int getInt(String key) {                                         //Get Integer
        return compound.getInt(key);
    }
    public long getLong(String key) {                                       //Get Long
        return compound.getLong(key);
    }
    public float getFloat(String key) {                                     //Get Float
        return compound.getFloat(key);
    }
    public double getDouble(String key) {                                   //Get Double
        return compound.getDouble(key);
    }
    public @NotNull String getString(String key) {                                   //Get String
        return compound.getString(key);
    }
    public byte @NotNull [] getByteArray(String key) {                                //Get Byte Array
        return compound.getByteArray(key);
    }
    public int @NotNull [] getIntArray(String key) {                                  //Get Int Array
        return compound.getIntArray(key);
    }
    public long @NotNull [] getLongArray(String key) {                                //Get Long Array
        return compound.getLongArray(key);
    }
    public @NotNull CompoundTag getCompound(String key) {                       //Get NBTCompound
        return new CompoundTag(compound.getCompound(key));
    }
    public @NotNull ListTag<?> getList(String key, NBTType type) {                 //Get NBTList of specific type
        return new ListTag<>(compound.getList(key, type.getId()));
    }
    public @NotNull UUID getUUID(String key) {                                       //Get UUID
        return compound.a(key);
    }
    @Override
    public @Nullable NBTTag<?> get(Object key) {                                //Get NBTBase of any element
        return NBTTag.wrap(compound.get((String) key));
    }

    @Override
    public NBTTag<?> remove(Object key) {
        NBTTag<?> removed = get(key);
        compound.remove((String) key);
        return removed;
    }

    @Override
    public void clear() {
        for (String key : keySet()) {
            remove(key);
        }
    }


    //Contains
    @Override
    public boolean containsKey(Object key) {                                //If NBT has a key
        return compound.hasKey((String) key);
    }

    public boolean containsKeyOfType(String key, NBTType type) {                 //If NBT has a key of a type
        return compound.hasKeyOfType(key, type.getId());
    }

    public CompoundTag merge(final CompoundTag tag) {
        return new CompoundTag(compound.a(((CompoundTag) tag).compound));
    }

    /** Check if value of a key represents UUID */
    public boolean isUUID(String key) {
        return compound.b(key);
    }

    public NBTType getTagType(String key) {
        return NBTType.getType(compound.d(key));
    }



    public String getNBT() {
        return toString();
    }

    @Override
    public String toString() {
        return compound.toString();
    }

    @Override
    public int hashCode() {
        return compound.hashCode() * 31;
    }

    @Override
    public CompoundTag clone() {
        return new CompoundTag(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CompoundTag) {
            return ((CompoundTag) o).compound.equals(this.compound);
        }
        return false;
    }



    @Override
    public NBTType getType() {
        return NBTType.COMPOUND;
    }
}
