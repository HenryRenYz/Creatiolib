package vip.creatio.clib.basic.tools.loader;

import org.bukkit.plugin.java.JavaPlugin;
import vip.creatio.clib.basic.packet.ChannelPacketListener;
import vip.creatio.clib.basic.packet.PacketListener;
import vip.creatio.clib.basic.tools.MsgManager;
import vip.creatio.clib.basic.tools.MsgSender;

/**
 * A delegated plugin class that contains basic plugin services
 */
public abstract class DelegatedPlugin implements PluginInterface {

    protected final JavaPlugin bootstrap;

    // Set it to FormatMsgManager in initNmsLoader if you want
    // yml message support.
    /** Custom message sending service */
    protected MsgSender msgSender;

    /** Packet listening service */
    protected PacketListener packetListener;

    protected DelegatedPlugin(JavaPlugin bootstrap) {
        this.bootstrap = bootstrap;
        msgSender = new MsgManager();
        packetListener = new ChannelPacketListener(bootstrap.getName() + "_listener");
    }

    @Override
    public void onEnable() {
        PacketListener.registerListener(bootstrap, packetListener);
    }

    @Override
    public void onDisable() {
        PacketListener.unregisterListeners(bootstrap);
    }

    public MsgSender getMsgSender() {
        return msgSender;
    }

    public PacketListener getPacketListener() {
        return packetListener;
    }
}
