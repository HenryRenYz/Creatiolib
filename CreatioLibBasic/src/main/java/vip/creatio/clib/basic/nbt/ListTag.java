package vip.creatio.clib.basic.nbt;

import vip.creatio.clib.basic.chat.Component;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTList;
import net.minecraft.server.NBTTagList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * Wrapper class of NBTTagList
 *
 * I add generics to it because NBTTagList actually behaves like normal List
 * @param <W> the type of element;
 */
@SuppressWarnings("unchecked")
public class ListTag<W extends NBTTag<? extends NBTBase>>
extends CollectionTag<W> {

    /** Since NBTTagList doesn't have generic, so it needs to be converted */
    @SuppressWarnings("unchecked")
    private static <S extends NBTBase> NBTList<S> convert0(NBTBase list) {
        return (NBTList<S>) list;
    }

    public ListTag(NBTTagList list) {
        super(convert0(list));
    }

    public ListTag(@NotNull ListTag<W> clone) {
        super(convert0(clone.list.clone()));
    }

    public ListTag() {
        super(convert0(new NBTTagList()));
    }

    @SafeVarargs
    public ListTag(W... elements) {
        this();
        this.addAll(Arrays.asList(elements));
    }
    
    public ListTag(Collection<W> elements) {
        this();
        this.addAll(elements);
    }

    @Override
    public NBTType getElementType() {
        return NBTType.getType(list.d_());
    }

    @Override
    public NBTType getType() {
        return NBTType.LIST;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ListTag)
            return list.equals(((ListTag<?>) obj).list);
        return false;
    }


    //returns the NBTCompound at the specific index, or returns a new compound if the index is out of range
    public CompoundTag getCompound(int index) {
        return new CompoundTag(toList().getCompound(index));
    }

    //returns the NBTList at the specific index, same as the one above.
    public ListTag<?> getList(int index) {
        return new ListTag<>(toList().b(index));
    }

    //returns the number of specific type at specific index, return 0 if the index is out of range
    public short getShort(int index) {                                              //Get Short
        return toList().d(index);
    }

    public int getInt(int index) {                                                  //Get Integer
        return toList().e(index);
    }

    public int[] getIntArray(int index) {                                           //Get Int Array
        return toList().f(index);
    }

    public double getDouble(int index) {                                            //Get Double
        return toList().h(index);
    }

    public float getFloat(int index) {                                              //Get Float
        return toList().i(index);
    }

    public String getString(int index) {                                            //Get String
        return toList().getString(index);
    }


    public NBTTagList toList() {
        return (NBTTagList) list;
    }

    @Override
    public W remove(int index) {
        return NBTTag.wrap(list.remove(index));
    }

    @Override
    public W set(int index, W item) {
        return NBTTag.wrap(list.set(index, item.unwrap()));
    }

    @Override
    public W get(int index) {
        return NBTTag.wrap(list.get(index));
    }

    public static ListTag<StringTag> toList(Collection<Component> c) {
        ListTag<StringTag> tag = new ListTag<>();
        for (Component comp : c) {
            if (comp != null)
                tag.add(new StringTag(comp.toJson()));
        }
        return tag;
    }
}
