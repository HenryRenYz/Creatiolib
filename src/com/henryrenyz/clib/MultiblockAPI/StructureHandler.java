package com.henryrenyz.clib.MultiblockAPI;

import com.henryrenyz.clib.MultiblockAPI.EventRegister.MultiblockAPIStructureInteractEvent;
import com.henryrenyz.clib.MultiblockAPI.EventRegister.MultiblockAPIStructureDestoryEvent;
import com.henryrenyz.clib.MultiblockAPI.EventRegister.MultiblockAPIStructureUnloadEvent;
import com.henryrenyz.clib.MultiblockAPI.MachineAPI.MachineClickable;
import com.henryrenyz.clib.modules.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.henryrenyz.clib.Creatio.getInstance;

public class StructureHandler extends StructureManager {

    //Multiblock Structure ticker.
    public static void StructureTick1S() {
        if (getMultiblockStructures().size() > 0) {
            for (MultiblockStructure m : getMultiblockStructures()) {
                //multiblock structure do




                //active structure do
                if (getActiveStructures().contains(m)) {
                    List<Location> collection = LocationUtil.drawCube(m.getCorner().getLocation(), m.getEndCorner().getLocation(), 0.5f, true);

                    for (Location l : collection) {
                        if (l.getWorld() != null) l.getWorld().spawnParticle(Particle.FLAME, l.getX(), l.getY(), l.getZ(), 2, 0, 0, 0, 0);
                    }

                    //tick machine
                    if (m.getHookedMachine() != null) {
                        m.getHookedMachine().Tick();
                    }
                }
            }
        }
    }

    public static void StructureTick3S() {
        getActiveStructures().clear();
        for (MultiblockStructure m : getMultiblockStructures()) {
            if (getActiveStructures().size() >= getInstance().maxActiveStructure()) break;
            if (m.isRequireUpdate() && m.isEnabled()) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getWorld() == m.getCore().getWorld()) {
                        if (m.getCore().getLocation().distance(p.getLocation()) <= getInstance().structureCheckRadius()) {
                            getActiveStructures().add(m);
                        }
                    }
                }
            }
        }
    }


    public static Boolean ifStructureChanged(MultiblockStructure m, Block b) {
        Location loc = b.getLocation();
        if (loc.distance(m.getCore().getLocation()) <= getInstance().structureCheckRadius()) {
            if (b.equals(m.getCore())) {
                return true;
            }
            if (m.isUnstable() && LocationUtil.inArea(loc, m.getCorner().getLocation(), m.getEndCorner().getLocation())) {
                return m.getBlockList().contains(b);
            }
        }
        return false;

    }

    //Implements of on Structure Destroy Event - player break.
    public static void onBlockBreakStructureCheck(BlockBreakEvent event) {
        List<MultiblockStructure> l = new ArrayList<>();

        for (MultiblockStructure m : getMultiblockStructures()) {
            if (ifStructureChanged(m, event.getBlock())) {
                MultiblockAPIStructureDestoryEvent e = new MultiblockAPIStructureDestoryEvent(Collections.singletonList(event.getBlock()), event.getPlayer(), m);
                Bukkit.getServer().getPluginManager().callEvent(e);
                if (e.isCancelled()) {
                    event.setCancelled(true);
                    return;
                }
                l.add(m);
            }
        }
        for (MultiblockStructure m : l) {
            m.deleteStructure(event);
        }
    }

    //Implements of on Structure Destroy Event - piston extend.
    public static void onPistonExtendStructureCheck(BlockPistonExtendEvent event) {
        for (MultiblockStructure m : getMultiblockStructures()) {
            for (Block b : event.getBlocks()) {
                if (ifStructureChanged(m, b)) {event.setCancelled(true);}
            }
        }
    }

    //Implements of on Structure Destroy Event - piston retract.
    public static void onPistonRetractStructureCheck(BlockPistonRetractEvent event) {
        for (MultiblockStructure m : getMultiblockStructures()) {
            for (Block b : event.getBlocks()) {
                if (ifStructureChanged(m, b)) {event.setCancelled(true);}
            }
        }
    }

    //Implements of on Structure Destroy Event - block explode.
    public static void onBlockExplodeStructureCheck(BlockExplodeEvent event) {
        List<MultiblockStructure> l = new ArrayList<>();

        for (MultiblockStructure m : getMultiblockStructures()) {
            if (event.getBlock().getLocation().distance(m.getCore().getLocation()) <= getInstance().structureCheckRadius()) {
                for (Block b : event.blockList()) {
                    if (ifStructureChanged(m, b)) {
                        MultiblockAPIStructureDestoryEvent e = new MultiblockAPIStructureDestoryEvent(event.blockList(), null, m);
                        Bukkit.getServer().getPluginManager().callEvent(e);
                        if (e.isCancelled()) {
                            event.setCancelled(true);
                            return;
                        }
                        l.add(m);
                    }
                }
            }
        }
        for (MultiblockStructure m : l) {
            m.deleteStructure(event);
        }
    }

    //Implements of on Structure Destroy Event - entity explode.
    public static void onEntityExplodeStructureCheck(EntityExplodeEvent event) {
        List<MultiblockStructure> l = new ArrayList<>();

        for (MultiblockStructure m : getMultiblockStructures()) {
            if (event.getEntity().getLocation().distance(m.getCore().getLocation()) <= getInstance().structureCheckRadius()) {
                for (Block b : event.blockList()) {
                    if (ifStructureChanged(m, b)) {
                        MultiblockAPIStructureDestoryEvent e = new MultiblockAPIStructureDestoryEvent(event.blockList(), event.getEntity(), m);
                        Bukkit.getServer().getPluginManager().callEvent(e);
                        if (e.isCancelled()) {
                            event.setCancelled(true);
                            return;
                        }
                        l.add(m);
                    }
                }
            }
        }
        for (MultiblockStructure m : l) {
            m.deleteStructure(event);
        }
    }

    //Implements of on Structure Destroy Event.
    public static void onBlockPlaceStructureCheck(BlockPlaceEvent event) {
        List<MultiblockStructure> l = new ArrayList<>();

        for (MultiblockStructure m : getMultiblockStructures()) {
            if (ifStructureChanged(m, event.getBlock())) {
                MultiblockAPIStructureDestoryEvent e = new MultiblockAPIStructureDestoryEvent(Collections.singletonList(event.getBlock()), event.getPlayer(), m);
                Bukkit.getServer().getPluginManager().callEvent(e);
                if (e.isCancelled()) {
                    event.setCancelled(true);
                    return;
                }
                l.add(m);
            }
        }
        for (MultiblockStructure m : l) {
            m.deleteStructure(event);
        }
    }

    //Check if a structure is unloaded
    public static void onChunkUnloadCheck(ChunkUnloadEvent event) {
        List<MultiblockStructure> l = new ArrayList<>();

        for (MultiblockStructure m : getActiveStructures()) {
            if (m.getCore().getChunk() == event.getChunk()) {
                MultiblockAPIStructureUnloadEvent e = new MultiblockAPIStructureUnloadEvent(m, event.getChunk());
                Bukkit.getServer().getPluginManager().callEvent(e);
                l.add(m);
            }
        }
        getActiveStructures().removeAll(l);
    }

    //Implements of on Structure Click Event.
    public static void onPlayerInteractCheck(PlayerInteractEvent event) {
        for (MultiblockStructure m : getActiveStructures()) {
            if (event.hasBlock()) {
                if (m.getBlockList().contains(event.getClickedBlock())) {
                    MultiblockAPIStructureInteractEvent e;
                    if (m.getCore() == event.getClickedBlock()) {
                        e = new MultiblockAPIStructureInteractEvent(event.getPlayer(), event.getItem(), event.getAction(), event.getClickedBlock(), m, true);
                    } else {
                        e = new MultiblockAPIStructureInteractEvent(event.getPlayer(), event.getItem(), event.getAction(), event.getClickedBlock(), m, false);
                    }
                    Bukkit.getServer().getPluginManager().callEvent(e);

                    if (e.isCancelled()) {
                        event.setCancelled(true);
                        return;
                    }

                    if (m.getHookedMachine() != null) {
                        if (m.getHookedMachine() instanceof MachineClickable) {
                            ((MachineClickable) m.getHookedMachine()).onClick(event);
                        }
                    }
                }
            }
        }
    }


}
