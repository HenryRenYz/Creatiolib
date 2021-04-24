package vip.creatio.clib.basic.packet.out;

import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.PacketPlayOutBlockBreakAnimation;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import vip.creatio.clib.basic.packet.Packet;
import vip.creatio.clib.basic.chat.Component;
import net.minecraft.server.ChatMessageType;
import net.minecraft.server.PacketPlayOutChat;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ChatPacket extends Packet<PacketPlayOutChat> {

    private static final Var<IChatBaseComponent> MSG = Reflection.field(PacketPlayOutChat.class, "a");
    private static final Var<UUID> SENDER = Reflection.field(PacketPlayOutChat.class, "c");

    private final Component component;
    private final UUID uuid;

    ChatPacket(PacketPlayOutChat nms) {
        super(nms);
        this.component = Component.wrap(MSG.get(nms));
        this.uuid = SENDER.get(nms);
    }

    public ChatPacket(String json, @Nullable UUID senderUuid) {
        this(Component.fromJson(json), senderUuid);
    }

    public ChatPacket(Component chat, @Nullable UUID senderUuid) {
        super(new PacketPlayOutChat(chat.unwrap(), ChatMessageType.SYSTEM, senderUuid));
        component = chat;
        uuid = senderUuid;
    }

    public ChatPacket(String json) {
        this(json, null);
    }

    public ChatPacket(Component chat) {
        this(chat, null);
    }

    public UUID getSenderUUID() {
        return this.uuid;
    }

    public Component getComponent() {
        return this.component;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Chat{content=").append(component.getString());
        if (uuid != null) b.append(",uuid=").append(uuid);
        b.append('}');
        return  b.toString();
    }
}
