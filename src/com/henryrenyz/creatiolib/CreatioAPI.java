package com.henryrenyz.creatiolib;

import com.henryrenyz.creatiolib.packets.Packets;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CreatioAPI {

    public static Number APIVersion = 1.0;

    public static void SendPacket(Player[] p, Object packet) {
        Packets.sendPacket(p, packet);
    }

    public static Object PlayOutWorldParticles(Location l, String p, float ofx, float ofy, float ofz, float ex, int n, boolean i, Object[] d) {
        return Packets.PlayOutWorldParticles(l, p, ofx, ofy, ofz, ex, n, i, d);
    }
}
