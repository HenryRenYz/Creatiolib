package com.henryrenyz.clib.MultiblockAPI;

import com.henryrenyz.clib.MultiblockAPI.MachineAPI.Machine;
import com.henryrenyz.clib.MultiblockAPI.EventRegister.MultiblockAPIStructureCreateEvent;
import com.henryrenyz.clib.modules.util.LocationUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.henryrenyz.clib.Creatio.getInstance;

@SuppressWarnings("unchecked")
public class MultiblockStructure implements ConfigurationSerializable {

    //Register Element
    public MultiblockStructure instance;
    private DisabledReason disable_reason = null;
    private List<List<Object[][]>> pattern;
    private Material center;

    //Attributes
    private UUID uuid;
    private String name_space;
    private boolean flipped = false;
    private BlockFace facing;
    private Location corner, edcorner, core;
    private boolean enabled = true;
    private List<Block> block_list = new ArrayList<>();           //block_list all the blocks contained in the structure

    //Ticking Element
    private String name;
    private boolean unstable;
    private boolean load_chunk;
    private int load_chunk_radius;
    private boolean overlappable;
    private boolean require_update;

    //Temporarily
    private List<Chunk> chunk = new ArrayList<>();
    private Machine hooked_machine;

    public MultiblockStructure(String name_space, Block center, BlockFace facing) {
        //Check validation
        if (!checkLimitValidation(center)) {
            return;
        }

        //Get vars from registered structures
        boolean d = false;
        for (RegisteredStructure e : StructureManager.getRegisteredStructures()) {
            if (e.getNameSpace().equalsIgnoreCase(name_space)) {
                this.facing = facing;
                this.uuid = UUID.randomUUID();
                this.name_space = e.getNameSpace();
                this.name = e.getName();
                this.center = e.getCenter();
                this.unstable = e.isUnstable();
                this.load_chunk = e.loadChunk();
                this.load_chunk_radius = e.getLoadRadius();
                this.overlappable = e.isOverlappable();
                this.require_update = e.isRequireUpdate();
                if (e.getMachineClass() != null) {
                    try {
                        e.getMachineClass().getConstructor(MultiblockStructure.class).newInstance(this);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException n) {
                        n.printStackTrace();
                    }
                }
                this.pattern = e.getPattern();

                if (center.getType() != this.center) {
                    delete();
                    this.disable_reason = DisabledReason.CENTER_MISMATCH;
                    return;
                }

                Integer[] cor = e.getCorner();
                this.core = LocationUtil.SetDirection(center.getLocation(), facing);
                this.corner = LocationUtil.LocalBlockCoords(this.core, (double) cor[0], (double) cor[1], (double) cor[2]);
                //System.out.println(cor[0] +" " + cor[1] +" "+ cor[2] +" - "+this.core + this.corner + this.center);
                int mismatch = checkStructure(true, false, true);

                if (mismatch > 0 && e.isFlippable()) {
                    this.corner = LocationUtil.LocalBlockCoords(this.core, ((double) cor[0]) * -1, (double) cor[1], (double) cor[2]);
                    this.flipped = true;
                    this.block_list.clear();
                    mismatch = checkStructure(true, true, true);
                }

                if (mismatch > 0) {
                    //System.out.println("DEBUG: STRUCTURE CONSRUCTING FAILED: "+ mismatch);
                    delete();
                    this.disable_reason = DisabledReason.MISMATCH;
                    return;
                }

                d = true;
                break;
            }
        }
        if (this.name_space == null) {
            delete();
            this.disable_reason = DisabledReason.NANESPACE_NOT_EXIST;
        }

        if (!d) {
            delete();
            this.disable_reason = DisabledReason.NOT_REGISTERED;
        }

        //Check for structure overlap
        if (StructureManager.getMultiblockStructures() != null && !overlappable) {
            for (MultiblockStructure m : StructureManager.getMultiblockStructures()) {
                if (m == this.instance) continue;
                if (checkCorner(this.corner, this.edcorner, m.corner, m.edcorner)) {
                    delete();
                    this.disable_reason = DisabledReason.OVERLAPPED;
                    return;
                }
            }
        }

        //Call Multiblock structure create event
        MultiblockAPIStructureCreateEvent e = new MultiblockAPIStructureCreateEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            delete();
            this.disable_reason = DisabledReason.EVENT_CANCELLED;
            return;
        }

        //Load Chunks
        if (this.load_chunk) loadChunks(true);

        StructureManager.getMultiblockStructures().add(this);
    }


    public MultiblockStructure(String name_space, boolean flipped, BlockFace facing, Location corner, Location edcorner,
                               Location core, boolean enabled, String name, boolean unstable,
                               boolean load_chunk, int load_chunk_radius, boolean overlappable, boolean require_update,
                               Map<String, Object> machine_data) {
        this.instance = this;
        this.uuid = UUID.randomUUID();
        this.name_space = name_space;
        this.flipped = flipped;
        this.facing = facing;
        this.corner = corner;
        this.edcorner = edcorner;
        this.core = core;
        this.enabled = enabled;
        this.name = name;
        this.unstable = unstable;
        this.load_chunk = load_chunk;
        this.load_chunk_radius = load_chunk_radius;
        this.require_update = require_update;
        this.overlappable = overlappable;
        boolean d = false;
        for (RegisteredStructure e : StructureManager.getRegisteredStructures()) {
            if (e.getNameSpace().equalsIgnoreCase(name_space)) {
                this.pattern = e.getPattern();
                this.center = e.getCenter();
                if (e.getMachineClass() != null) {
                    try {
                        e.getMachineClass().getConstructor(MultiblockStructure.class).newInstance(this);
                        hooked_machine.deserialize(machine_data);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException n) {
                        n.printStackTrace();
                    }
                }
                d = true;
                break;
            }
        }
        if (!d) {
            delete();
            this.disable_reason = DisabledReason.NOT_REGISTERED;
        }

        //Check for structure overlap
        if (StructureManager.getMultiblockStructures() != null && !overlappable) {
            for (MultiblockStructure m : StructureManager.getMultiblockStructures()) {
                if (m == this.instance) continue;
                if (checkCorner(this.corner, this.edcorner, m.corner, m.edcorner)) {
                    delete();
                    this.disable_reason = DisabledReason.OVERLAPPED;
                    return;
                }
            }
        }

        if (this.core.getBlock().getType() != this.center) {
            delete();
            this.disable_reason = DisabledReason.CENTER_MISMATCH;
            return;
        }

        if (this.unstable) {
            int mismatch = checkStructure(false, this.flipped, true);

            if (mismatch > 0) {
                //System.out.println("DEBUG: STRUCTURE CONSRUCTING FAILED: "+ mismatch);
                delete();
                this.disable_reason = DisabledReason.MISMATCH;
                return;
            }
        }

        //Load Chunks
        if (this.load_chunk) loadChunks(true);

        StructureManager.getMultiblockStructures().add(this);
    }


    private boolean checkLimitValidation(Block center) {
        //Server structure limit
        if (StructureManager.getMultiblockStructures().size() >= getInstance().maxStructureInServer()) {
            delete();
            this.disable_reason = DisabledReason.REACH_WORLD_LIMIT;
            return false;
        }

        //radius structure limit check
        int count = 0;
        for (MultiblockStructure m : StructureManager.getMultiblockStructures()) {
            if (center.getLocation().distance(m.getCore().getLocation()) <= getInstance().structureCheckRadius()) {
                count++;
            }
        }
        if (count >= getInstance().maxStructureInRadius()) {
            delete();
            this.disable_reason = DisabledReason.REACH_RANGE_LIMIT;
            return false;
        }
        return true;
    }


    //Forceload chunks
    public void loadChunks(boolean setlist) {
        if (setlist) {
            this.chunk.clear();
            for (int rad = 0; rad < 360; rad += 2) {
                Location loop = this.core.clone();
                for (int i = 5; i < load_chunk_radius; i += 5) {
                    loop.add(Math.cos(Math.toRadians(rad)) * 5, 0, Math.sin(Math.toRadians(rad) * 5));
                    this.chunk.add(loop.getChunk());
                    loop.getChunk().setForceLoaded(true);
                }
            }
        } else {
            for (Chunk c : this.chunk) {
                c.setForceLoaded(true);
            }
        }
    }


    //Unforceload chunks
    public void releaseChunks(boolean accurate) {
        for (Chunk c : this.chunk) {
            if (accurate) {
                for (MultiblockStructure m : StructureManager.getMultiblockStructures()) {
                    if (m != this) {
                        if (!m.getForceChunks().contains(c)) {
                            c.setForceLoaded(false);
                        }
                    }
                }
            } else {
                c.setForceLoaded(false);
            }
        }
    }


    private static boolean checkCorner(Location targetLoc1, Location targetLoc2, Location area1, Location area2) {
        double[] x0 = {targetLoc1.getX(), targetLoc2.getX()},
                y0 = {targetLoc1.getY(), targetLoc2.getY()},
                z0 = {targetLoc1.getZ(), targetLoc2.getZ()};
        for (double x : x0) {
            for (double y : y0) {
                for (double z : z0) {
                    if (LocationUtil.inArea(new Location(targetLoc1.getWorld(), x, y, z), area1, area2)) return true;
                }
            }
        }
        return false;
    }

    //The first time to check the structure and create BlockList.
    private int checkStructure(boolean updatecorner, boolean isFlipped, boolean createBlockList) {
        int k = 0, k_layer = 0, k_line = 0, k_dot = 0;
        int delta = (isFlipped) ? -1 : 1;
        Block looper = this.corner.getBlock();
        for (List<Object[][]> layer : this.pattern) {
            k_line = 0;
            for (Object[][] line : layer) {
                for (k_dot = 0; k_dot < line.length; k_dot++) {
                    Object[] dot = line[k_dot];
                    if (dot != null) {
                        if (!LocationUtil.DirectionalBlockDataCheck(LocationUtil.LocalBlockGet(looper,this.facing,k_dot * delta,k_layer,k_line).getBlockData(), dot, this.facing, isFlipped)) {
                            k++;
                            //System.out.println("MISMATCH: @ " + Utils.LocalBlockGet(looper,this.facing,k_dot,k_layer,k_line).getLocation());
                            //System.out.println("  EXPECTED " +dot[0]);
                        } else if (createBlockList) {
                            this.block_list.add(LocationUtil.LocalBlockGet(looper,this.facing,k_dot * delta,k_layer,k_line));
                        }
                    }
                }
                k_line++;
            }
            k_layer++;
        }
        if (updatecorner) this.edcorner = LocationUtil.LocalBlockCoords(
                this.corner,((double) k_dot - 1) * delta, (double) k_layer - 1, (double) k_line - 1);
        return k;
    }

    //A scan of structure from BlockList, this should be much faster than checkStructure.
    private int scanStructure() {
        int k = 0;
        int delta = this.flipped ? -1 : 1;
        Block looper = this.corner.getBlock();
        for (int k_layer = 0; k_layer < this.pattern.size(); k_layer++) {
            for (int k_line = 0; k_line < this.pattern.get(0).size(); k_line++) {
                for (int k_dot = 0; k_dot < this.pattern.get(0).get(0).length; k_dot++) {
                    if (!this.block_list.contains(LocationUtil.LocalBlockGet(looper,this.facing,k_dot * delta,k_layer,k_line))) {
                        //System.out.println("DMISMATCH: @ " + Utils.LocalBlockGet(looper,this.facing,k_dot * delta,k_layer,k_line).getLocation());
                        k++;
                    }
                }
            }
        }
        return k;
    }


    //Serialization Section
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(), map1 = new HashMap<>();
        map.put("name_space", this.name_space);
        map.put("flipped", this.flipped);
        map.put("facing", this.facing.name());
        map.put("corner", this.corner.serialize());
        map.put("edcorner", this.edcorner.serialize());
        map.put("core", this.core.serialize());
        map.put("enabled", this.enabled);

        map.put("name", this.name);
        map.put("unstable", this.unstable);
        map.put("load_chunk", this.load_chunk);
        map.put("load_chunk_radius", this.load_chunk_radius);
        map.put("overlappable", this.overlappable);
        map.put("require_update", this.require_update);
        map.put("machine_data", this.getHookedMachine().serialize());
        map1.put("id", this.uuid.toString());
        map1.put("data", map);
        return map1;
    }


    public static MultiblockStructure deserialize(Map<String, Object> map1) {
        Map<String, Object> map = (Map<String, Object>) map1.get("data");
        MultiblockStructure m = new MultiblockStructure(
                (map.get("name_space") != null ? (String) map.get("name_space") : null),
                (map.get("flipped") != null ? (Boolean) map.get("flipped") : false),
                (map.get("facing") != null ? BlockFace.valueOf(map.get("facing").toString()) : BlockFace.NORTH),
                (map.get("corner") != null ? Location.deserialize((Map<String, Object>) map.get("corner")) : null),
                (map.get("edcorner") != null ? Location.deserialize((Map<String, Object>) map.get("edcorner")) : null),
                (map.get("core") != null ? Location.deserialize((Map<String, Object>) map.get("core")) : null),
                (map.get("enabled") != null ? (Boolean) map.get("enabled") : false),

                (map.get("name") != null ? (String) map.get("name") : "Noname"),
                (map.get("unstable") != null ? (Boolean) map.get("unstable") : true),
                (map.get("load_chunk") != null ? (Boolean) map.get("load_chunk") : false),
                (map.get("load_chunk_radius") != null ? (Integer) map.get("load_chunk_radius") : 16),
                (map.get("overlappable") != null ? (Boolean) map.get("overlappable") : false),
                (map.get("require_update") != null ? (Boolean) map.get("require_update") : false),
                (map.get("machine_data") != null ? (Map<String, Object>) map.get("machine_data") : new HashMap<>())
        );
        m.uuid = UUID.fromString((String) map1.get("id"));
        return m;
    }

    //delete this object and it's linked machine
    public void delete() {
        deleteStructure(null);
    }

    public void deleteStructure(Event event) {
        if (this.hooked_machine != null) {
            this.hooked_machine.onDestroy(event);
            StructureManager.getMachines().remove(this.hooked_machine);
            this.hooked_machine = null;
        }
        releaseChunks(true);
        StructureManager.getMultiblockStructures().remove(this);
        this.enabled = false;
        this.disable_reason = DisabledReason.DELETED;
    }


    public boolean isEnabled() {
        return this.enabled;
    }
    public void setEnabled(boolean a) {
        this.enabled = a;
        if (a) {
            this.hooked_machine.Enable();
        } else {
            this.hooked_machine.Disable();
        }
    }


    public DisabledReason getDisabledReason() {
        return this.disable_reason;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getNameSpace() {
        return this.name_space;
    }


    public String getName() {
        return this.name;
    }
    public void setName(String a) {
        this.name = a;
    }


    public Material getCenter() {
        return this.center;
    }


    public boolean isUnstable() {
        return this.unstable;
    }
    public void setUnstable(boolean a) {
        this.unstable = a;
    }


    public boolean isFlipped() {
        return this.flipped;
    }


    public boolean loadChunk() {
        return this.load_chunk;
    }
    public void setLoadChunk(boolean a) {
        this.load_chunk = a;
    }


    public int getLoadRadius() {
        return this.load_chunk_radius;
    }
    public void setLoadRadius(int a) {
        this.load_chunk_radius = a;
    }


    public boolean isOverlappable() {
        return this.overlappable;
    }
    public void setOverlappable(boolean a) {
        this.overlappable = a;
    }


    public Block getCorner() {
        return this.corner.getBlock();
    }


    public Block getEndCorner() {
        return this.edcorner.getBlock();
    }


    public Block getCore() {
        return this.core.getBlock();
    }

    public Machine getHookedMachine() {
        return this.hooked_machine;
    }
    public void setHooked_machine(Machine m) {
        this.hooked_machine = m;
    }

    public List<Chunk> getForceChunks() {
        return this.chunk;
    }


    public BlockFace getFacing() {
        return this.facing;
    }


    public boolean isRequireUpdate() {
        return this.require_update;
    }
    public void setRequireUpdate(boolean a) {
        this.require_update = a;
    }


    public List<List<Object[][]>> getPattern() {
        return this.pattern;
    }


    public int getStructureMismatch() {
        return scanStructure();
    }


    public boolean isTargetBy(Player p, int radius, FluidCollisionMode mode) {
        Block t = p.getTargetBlockExact(radius, mode);
        if (t != null) return LocationUtil.inArea(t.getLocation(), this.corner, this.edcorner);
        return false;
    }


    public List<Block> getBlockList() {
        return this.block_list;
    }


    public String getString() {
        return "MultiblockAPIStructure{name_space=" + this.name_space + "world=" + this.core.getWorld()+ ",x=" + this.core.getX() + ",y=" + this.core.getY() + ",z=" + this.core.getZ() + '}';
    }

    @Override
    public int hashCode() {
        int code = 0;
        code += 31 * uuid.hashCode();
        code += 31 * pattern.hashCode();
        code += 31 * name_space.hashCode();
        code += 31 * core.hashCode();
        code += 31 * block_list.hashCode();
        return code;
    }


    public enum DisabledReason {
        OVERLAPPED,
        NANESPACE_NOT_EXIST,
        MISMATCH,
        EVENT_CANCELLED,
        CENTER_MISMATCH,
        REACH_WORLD_LIMIT,
        REACH_RANGE_LIMIT,
        NOT_REGISTERED,
        DELETED;

        private DisabledReason() {}
    }
}
