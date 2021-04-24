package vip.creatio.clib.basic.packet.out;

import net.minecraft.server.PacketPlayOutTabComplete;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import vip.creatio.clib.basic.cmd.suggestion.Suggestions;
import vip.creatio.clib.basic.packet.Packet;

public class TabCompletePacket extends Packet<PacketPlayOutTabComplete> {

    private static final Var<Integer> TID = Reflection.field(PacketPlayOutTabComplete.class, 0);
    private static final Var<com.mojang.brigadier.suggestion.Suggestions> SUG = Reflection.field(PacketPlayOutTabComplete.class, 1);

    private final int transactionId;
    private final Suggestions suggestions;

    TabCompletePacket(PacketPlayOutTabComplete nms) {
        super(nms);
        this.transactionId = TID.getInt(nms);
        this.suggestions = new Suggestions(SUG.get(nms));
    }

    public TabCompletePacket(int transactionId, Suggestions sug) {
        super(new PacketPlayOutTabComplete(transactionId, sug.unwrap()));
        this.transactionId = transactionId;
        this.suggestions = sug;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public Suggestions getSuggestions() {
        return suggestions;
    }
}
