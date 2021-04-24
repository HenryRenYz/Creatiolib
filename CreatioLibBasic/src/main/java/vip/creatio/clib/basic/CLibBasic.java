package vip.creatio.clib.basic;

import vip.creatio.clib.basic.internal.CLibBasicBootstrap;
import vip.creatio.clib.basic.packet.out.BlockBreakingPacket;
import vip.creatio.clib.basic.packet.out.ChatPacket;
import vip.creatio.clib.basic.tools.*;
import vip.creatio.clib.basic.tools.loader.DelegatedPlugin;
import vip.creatio.clib.basic.packet.PacketManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CLibBasic extends DelegatedPlugin {

    private static CLibBasic        instance;

    private PacketManager           packetManager;

    protected CLibBasic(JavaPlugin bootstrap) {
        super(bootstrap);
        instance = this;
        this.packetManager = new PacketManager();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        msgSender = new MsgManager("&6&l[&aClib&2Basic&6&l]");

        packetListener.register(BlockBreakingPacket.class, (p, player) -> {
            System.out.println("Player: " + player);
            System.out.println("Block: " + p.getBlock());
            return true;
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static void intern(String msg) {
        instance.msgSender.intern(msg);
    }

    public static void log(String msg) {
        instance.msgSender.log(msg);
    }

    public static MsgSender getSender() {
        return instance.msgSender;
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public GlobalTaskExecutor getTaskExecutor() {
        return getBootstrap().getTaskExecutor();
    }

    public ListenerRegister getListenerManager() {
        return getBootstrap().getListenerManager();
    }

    public static CLibBasic getInstance() {
        return instance;
    }

    public CLibBasicBootstrap getBootstrap() {
        return (CLibBasicBootstrap) bootstrap;
    }

}
