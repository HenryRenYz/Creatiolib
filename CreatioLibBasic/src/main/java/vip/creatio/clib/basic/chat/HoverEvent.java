package vip.creatio.clib.basic.chat;

import com.google.gson.JsonElement;
import vip.creatio.clib.basic.tools.Wrapper;
import vip.creatio.clib.basic.nbt.CompoundTag;
import vip.creatio.clib.basic.util.BukkitUtil;
import vip.creatio.clib.basic.util.EntityUtil;
import vip.creatio.clib.basic.util.ItemUtil;
import vip.creatio.accessor.Func;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import net.minecraft.server.ChatHoverable;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.Item;
import net.minecraft.server.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class HoverEvent implements Wrapper<ChatHoverable> {

    private final ChatHoverable event;
    private final HoverAction<?> action;
    private final Wrapper<?> value;

    @SuppressWarnings("unchecked")
    public <T extends Wrapper<?>> HoverEvent(HoverAction<T> action, T value) {
        this.event = new ChatHoverable(
                (ChatHoverable.EnumHoverAction<Object>) action.unwrap(),
                value.unwrap());
        this.action = action;
        this.value = value;
    }

    private static final Var<Object> VALUE = Reflection.field(ChatHoverable.class, "c");
    HoverEvent(ChatHoverable event) {
        this.event = event;
        this.action = HoverAction.getByName(event.a().b());
        this.value = HoverAction.wrap(VALUE.get(event));
    }

    public HoverAction<?> getAction() {
        return action;
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T extends Wrapper<?>> T getValue(HoverAction<T> action) {
        if (this.action == action) {
            return (T)((HoverAction<Wrapper<?>>) action).cast(this.value);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof HoverEvent) {
            return event.equals(((HoverEvent) o).event);
        }
        return false;
    }

    @Override
    public String toString() {
        return event.toString();
    }

    @Override
    public int hashCode() {
        return event.hashCode() * 31;
    }

    @Override
    public ChatHoverable unwrap() {
        return event;
    }

    @Override
    public Class<? extends ChatHoverable> wrappedClass() {
        return ChatHoverable.class;
    }

    public static HoverEvent showText(String text) {
        return showText(Component.of(text));
    }

    public static HoverEvent showText(Component component) {
        return new HoverEvent(HoverAction.SHOW_TEXT, component);
    }

    public static HoverEvent showItem(Material mat, int count, @Nullable CompoundTag tag) {
        return new HoverEvent(HoverAction.SHOW_ITEM, new ItemStackInfo(mat, count, tag));
    }

    public static HoverEvent showItem(Material mat, int count) {
        return showItem(mat, count, null);
    }

    public static HoverEvent showItem(Material mat) {
        return showItem(mat, 1, null);
    }

    public static HoverEvent showItem(ItemStack item) {
        return new HoverEvent(HoverAction.SHOW_ITEM, new ItemStackInfo(item));
    }

    public static HoverEvent showEntity(EntityType type, UUID uuid, @Nullable Component component) {
        return new HoverEvent(HoverAction.SHOW_ENTITY, new EntityTooltipInfo(type, uuid, component));
    }

    public static HoverEvent showEntity(EntityType type, UUID uuid) {
        return showEntity(type, uuid, null);
    }

    public static HoverEvent showEntity(EntityType type) {
        return showEntity(type, UUID.randomUUID(), null);
    }

    public static HoverEvent showEntity(Entity entity) {
        return new HoverEvent(HoverAction.SHOW_ENTITY, new EntityTooltipInfo(entity));
    }


    public static class ItemStackInfo implements Wrapper<ChatHoverable.c /* ItemStackInfo */> {

        private static final Var<Item> ITEM_VAR = Reflection.field(ChatHoverable.c.class, "a");
        private static final Var<Integer> COUNT_VAR = Reflection.field(ChatHoverable.c.class, "b");
        private static final Var<NBTTagCompound> TAG_VAR = Reflection.field(ChatHoverable.c.class, "c");

        private final ChatHoverable.c info;
        private final Material mat;
        private final int count;
        @Nullable
        private final CompoundTag tag;

        ItemStackInfo(ChatHoverable.c info) {
            this.info = info;
            this.mat = ItemUtil.toBukkit(ITEM_VAR.get(info));
            this.count = COUNT_VAR.get(info);
            this.tag = new CompoundTag(TAG_VAR.get(info));
        }

        private static final Func<ChatHoverable.c> CONST =
                Reflection.constructor(ChatHoverable.c.class, Item.class, int.class, NBTTagCompound.class);

        public ItemStackInfo(Material mat, int count, @Nullable CompoundTag tag) {
            this.info = CONST.invoke(ItemUtil.toNms(mat), count, tag == null ? null : tag.unwrap());
            this.mat = mat;
            this.count = count;
            this.tag = tag;
        }

        public ItemStackInfo(ItemStack itemStack) {
            this.info = new ChatHoverable.c(ItemUtil.toNms(itemStack));
            this.mat = itemStack.getType();
            this.count = itemStack.getAmount();
            this.tag = ItemUtil.getTag(itemStack);
        }

        public Material getMat() {
            return mat;
        }

        public int getCount() {
            return count;
        }

        public @Nullable CompoundTag getTag() {
            return tag;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ItemStackInfo) {
                return info.equals(((ItemStackInfo) o).info);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return info.hashCode() * 31;
        }

        private static final Func<ChatHoverable.c> CREATE_1 =
                Reflection.method(ChatHoverable.c.class, "b", JsonElement.class);
        public @NotNull ItemStackInfo create(JsonElement json) {
            return new ItemStackInfo(CREATE_1.invoke(json));
        }

        private static final Func<ChatHoverable.c> CREATE_2 =
                Reflection.method(ChatHoverable.c.class, "b", IChatBaseComponent.class);
        public @Nullable ItemStackInfo create(Component component) {
            ChatHoverable.c raw = CREATE_2.invoke(component.unwrap());
            return raw == null ? null : new ItemStackInfo(raw);
        }

        @Override
        public ChatHoverable.c unwrap() {
            return info;
        }

        @Override
        public Class<? extends ChatHoverable.c> wrappedClass() {
            return ChatHoverable.c.class;
        }
    }

    public static class EntityTooltipInfo implements Wrapper<ChatHoverable.b /* EntityTooltipInfo */> {

        private final ChatHoverable.b info;
        private final EntityType type;
        private final UUID uuid;
        @Nullable
        private final Component component;

        EntityTooltipInfo(ChatHoverable.b info) {
            this.info = info;
            this.type = BukkitUtil.toBukkitEntityType(info.a);
            this.uuid = info.b;
            this.component = Component.wrap(info.c);
        }

        public EntityTooltipInfo(EntityType type, UUID uuid, @Nullable Component component) {
            this.info = new ChatHoverable.b(BukkitUtil.toNmsEntityType(type), uuid, component == null ? null : component.unwrap());
            this.type = type;
            this.uuid = uuid;
            this.component = component;
        }

        public EntityTooltipInfo(Entity entity) {
            this.component = EntityUtil.getDisplayName(entity);
            this.uuid = entity.getUniqueId();
            this.type = entity.getType();
            this.info = new ChatHoverable.b(BukkitUtil.toNmsEntityType(type), uuid, component.unwrap());
        }

        @Nullable
        public static EntityTooltipInfo create(JsonElement json) {
            ChatHoverable.b raw = ChatHoverable.b.a(json);
            return raw == null ? null : new EntityTooltipInfo(raw);
        }

        @Nullable
        public static EntityTooltipInfo create(Component component) {
            ChatHoverable.b raw = ChatHoverable.b.a(component.unwrap());
            return raw == null ? null : new EntityTooltipInfo(raw);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof EntityTooltipInfo) {
                return info.equals(((EntityTooltipInfo) o).info);
            }
            return false;
        }

        public EntityType getType() {
            return type;
        }

        public UUID getUniqueId() {
            return uuid;
        }

        public @Nullable Component getComponent() {
            return component;
        }

        @Override
        public int hashCode() {
            return info.hashCode() * 31;
        }

        @Override
        public ChatHoverable.b unwrap() {
            return info;
        }

        @Override
        public Class<? extends ChatHoverable.b> wrappedClass() {
            return ChatHoverable.b.class;
        }
    }

}
