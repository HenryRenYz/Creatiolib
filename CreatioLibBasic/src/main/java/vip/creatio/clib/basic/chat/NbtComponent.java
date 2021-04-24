package vip.creatio.clib.basic.chat;

import vip.creatio.clib.basic.util.BukkitUtil;
import net.minecraft.server.ChatComponentNBT;
import org.bukkit.NamespacedKey;

public abstract class NbtComponent extends Component {

    NbtComponent(ChatComponentNBT component) {
        super(component);
    }

    public String getNbtPath() {
        return ((ChatComponentNBT) component).h();
    }

    public boolean isInterpreting() {
        return ((ChatComponentNBT) component).i();
    }

    @Override
    public ChatComponentNBT unwrap() {
        return ((ChatComponentNBT) component);
    }

    @Override
    public abstract NbtComponent plainCopy();

    public static class Entity extends NbtComponent {

        Entity(ChatComponentNBT.b raw) {
            super(raw);
        }

        public Entity(String nbtPath, boolean interpreting, String selector) {
            super(new ChatComponentNBT.b(nbtPath, interpreting, selector));
        }

        public Entity(String nbtPath, String selector) {
            this(nbtPath, false, selector);
        }

        public String getSelector() {
            return ((ChatComponentNBT.b) component).j();
        }

        @Override
        public Entity plainCopy() {
            return new Entity(((ChatComponentNBT.b) component).g());
        }
    }

    public static class Block extends NbtComponent {

        Block(ChatComponentNBT.a raw) {
            super(raw);
        }

        public Block(String nbtPath, boolean interpreting, String coords) {
            super(new ChatComponentNBT.a(nbtPath, interpreting, coords));
        }

        public Block(String nbtPath, String coords) {
            this(nbtPath, false, coords);
        }

        public String getPos() {
            return ((ChatComponentNBT.a) component).j();
        }

        @Override
        public Block plainCopy() {
            return new Block(((ChatComponentNBT.a) component).g());
        }
    }

    public static class Storage extends NbtComponent {

        Storage(ChatComponentNBT.c raw) {
            super(raw);
        }

        public Storage(String nbtPath, boolean interpreting, NamespacedKey namespace) {
            super(new ChatComponentNBT.c(nbtPath, interpreting, BukkitUtil.toNms(namespace)));
        }

        public Storage(String nbtPath, NamespacedKey namespace) {
            this(nbtPath, false, namespace);
        }

        public NamespacedKey getKey() {
            return BukkitUtil.toBukkit(((ChatComponentNBT.c) component).j());
        }

        @Override
        public Storage plainCopy() {
            return new Storage(((ChatComponentNBT.c) component).g());
        }
    }
}
