package vip.creatio.clib.basic.nbt;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTList;

import java.util.AbstractList;

/** S - the storaged raw type, W - the wrapper interface */
@SuppressWarnings("unchecked")
public abstract class CollectionTag<W extends NBTTag<? extends NBTBase>>
extends AbstractList<W>
implements NBTTag<NBTList<? extends NBTBase>>
{

    protected NBTList<NBTBase> list;

    CollectionTag(NBTList<? extends NBTBase> list) {
        this.list = (NBTList<NBTBase>) list;
    }

    @Override
    public abstract W remove(int index);

    @Override
    public abstract W set(int index, W item);

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public void add(int index, W item) {
        list.add(index, item.unwrap());
    }

    @Override
    public boolean add(W item) {
        list.add(item.unwrap());
        return true;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public abstract W get(int index);

    @Override
    public abstract NBTType getType();

    public abstract NBTType getElementType();

    @Override
    public NBTList<? extends NBTBase> unwrap() {
        return list;
    }

    @Override
    public Class<? extends NBTList<? extends NBTBase>> wrappedClass() {
        return (Class) list.getClass();
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @Override
    public int hashCode() {
        return list.hashCode() * 31;
    }
}
