package vip.creatio.clib.basic.packet.in;

import net.minecraft.server.PacketPlayInTabComplete;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import vip.creatio.clib.basic.packet.Packet;

public class OnTabCompletePacket extends Packet<PacketPlayInTabComplete> {

    private static final Var<Integer> TID = Reflection.field(PacketPlayInTabComplete.class, 0);
    private static final Var<String> TEXT = Reflection.field(PacketPlayInTabComplete.class, 1);

    OnTabCompletePacket(PacketPlayInTabComplete nms) {
        super(nms);
    }

    public int getTransactionID() {
        return original.b /* getTransactionID */ ();
    }

    /** The string behind cursor, does not include "/" */
    public String getContent() {
        return original.c /* getContent */ ();
    }

    public void setTransactionID(int id) {
        TID.setInt(original, id);
    }

    public void setContent(String str) {
        TEXT.set(original, str);
    }

}
