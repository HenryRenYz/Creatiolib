package vip.creatio.clib.basic.packet;

import vip.creatio.clib.basic.tools.Wrapper;
import vip.creatio.clib.basic.util.EntityUtil;
import net.minecraft.server.DataWatcher;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pose;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Declared entity, the entity not actually exist
 * can be set to anything, used in packet sending.
 *
 * Wrapper class of CraftEntity
 */
//TODO: This spawn entity will add actual entity to world entity list,
// but not tick list, which will prevent block placing at entity and
// potentially unexpected interactive, remove it!
public class VirtualEntity<T extends Entity> implements Entity, Wrapper<T> {

    //CraftEntity
    private final T original;

    public VirtualEntity(T entity, UUID uuid, int eid) {
        this(entity.getType());
        setUniqueID(uuid);
        setEntityID(eid);
    }

    public VirtualEntity(T entity, UUID uuid) {
        this(entity.getType());
        setUniqueID(uuid);
    }

    @SuppressWarnings("unchecked")
    public VirtualEntity(Location loc, T entity) {
        this.original = (T) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), createEntity(loc, entity.getType()));
    }

    public VirtualEntity(Location loc, EntityType type) {
        this(type);
        teleport(loc);
    }

    @SuppressWarnings("unchecked")
    public VirtualEntity(EntityType type) {
        this.original = (T) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), createEntity(Bukkit.getWorlds().get(0).getSpawnLocation(), type));
    }

    //Create NMS Entity
    private static net.minecraft.server.Entity createEntity(@NotNull Location loc, @NotNull EntityType type) {
        //TODO: entity will be put into world tick list, which is not desired
        return ((CraftWorld) (loc.getWorld() == null ? Bukkit.getWorlds().get(0) : loc.getWorld())).createEntity(loc, type.getEntityClass());
    }

    public T getEntity() {
        return original;
    }

    public void setUniqueID(UUID uuid) {
        toNms().a_(uuid); /* setUniqueID */
    }

    public void setEntityID(int eid) {
        toNms().e(eid); /* setEntityID */
    }

    public void setHeadPitch(float pitch) {
        if (original instanceof LivingEntity) {
            toNms().setHeadRotation(pitch);
        }
    }

    public float getHeadPitch() {
        if (original instanceof LivingEntity) {
            return toNms().getHeadRotation();
        }
        return 0;
    }

    @Override
    public @NotNull Location getLocation() {
        return original.getLocation();
    }

    @Override
    public @Nullable Location getLocation(@Nullable Location loc) {
        return original.getLocation(loc);
    }

    @Override
    public void setVelocity(@NotNull Vector velocity) {
        original.setVelocity(velocity);
    }

    @Override
    public @NotNull Vector getVelocity() {
        return original.getVelocity();
    }

    @Override
    public double getHeight() {
        return original.getHeight();
    }

    @Override
    public double getWidth() {
        return original.getWidth();
    }

    @Override
    public @NotNull BoundingBox getBoundingBox() {
        return original.getBoundingBox();
    }

    @Override
    public boolean isOnGround() {
        return original.isOnGround();
    }

    @Override
    public boolean isInWater() {
        return original.isInWater();
    }

    @Override
    public @NotNull World getWorld() {
        return original.getWorld();
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        original.setRotation(yaw, pitch);
    }

    @Override
    public boolean teleport(@NotNull Location location) {
        return original.teleport(location);
    }

    @Override
    public boolean teleport(@NotNull Location location, PlayerTeleportEvent.@NotNull TeleportCause cause) {
        return original.teleport(location, cause);
    }

    @Override
    public boolean teleport(@NotNull Entity destination) {
        return original.teleport(destination);
    }

    @Override
    public boolean teleport(@NotNull Entity destination, PlayerTeleportEvent.@NotNull TeleportCause cause) {
        return original.teleport(destination, cause);
    }

    @Override
    public @NotNull List<Entity> getNearbyEntities(double x, double y, double z) {
        return original.getNearbyEntities(x, y, z);
    }

    @Override
    public int getEntityId() {
        return original.getEntityId();
    }

    @Override
    public int getFireTicks() {
        return original.getFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return original.getMaxFireTicks();
    }

    @Override
    public void setFireTicks(int ticks) {
        original.setFireTicks(ticks);
    }

    @Override
    public void remove() {
        original.remove();
    }

    @Override
    public boolean isDead() {
        return original.isDead();
    }

    @Override
    public boolean isValid() {
        return original.isValid();
    }

    @Override
    public void sendMessage(@NotNull String message) {
        // VirtualEntity will not receive any message
    }

    @Override
    public void sendMessage(@NotNull String[] messages) {
        // VirtualEntity will not receive any message
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String message) {
        // VirtualEntity will not receive any message
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String[] messages) {
        // VirtualEntity will not receive any message
    }

    @Override
    public @NotNull Server getServer() {
        return original.getServer();
    }

    @Override
    public @NotNull String getName() {
        return original.getName();
    }

    @Override
    public boolean isPersistent() {
        return original.isPersistent();
    }

    @Override
    public void setPersistent(boolean persistent) {
        original.setPersistent(persistent);
    }

    @Override
    public @Nullable Entity getPassenger() {
        return null;
    }

    @Override
    public boolean setPassenger(@NotNull Entity passenger) {
        // VirtualEntity will not have a passenger
        return false;
    }

    @Override
    public @NotNull List<Entity> getPassengers() {
        return new ArrayList<>();
    }

    @Override
    public boolean addPassenger(@NotNull Entity passenger) {
        // VirtualEntity will not have a passenger
        return false;
    }

    @Override
    public boolean removePassenger(@NotNull Entity passenger) {
        // VirtualEntity will not have a passenger
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean eject() {
        return true;
    }

    @Override
    public float getFallDistance() {
        return 0;
    }

    @Override
    public void setFallDistance(float distance) {
        // Non-tick-able entity will never fall
    }

    @Override
    public void setLastDamageCause(@Nullable EntityDamageEvent event) {
        // Non-tick-able entity will never be damaged
    }

    @Override
    public @Nullable EntityDamageEvent getLastDamageCause() {
        return null;
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return original.getUniqueId();
    }

    @Override
    public int getTicksLived() {
        return -1;
    }

    @Override
    public void setTicksLived(int value) {
        // VirtualEntity will never live in the world
    }

    @Override
    public void playEffect(@NotNull EntityEffect type) {
        original.playEffect(type);
    }

    @Override
    public @NotNull EntityType getType() {
        return original.getType();
    }

    @Override
    public boolean isInsideVehicle() {
        return false;
    }

    @Override
    public boolean leaveVehicle() {
        return true;
    }

    @Override
    public @Nullable Entity getVehicle() {
        return null;
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        original.setCustomNameVisible(flag);
    }

    @Override
    public boolean isCustomNameVisible() {
        return original.isCustomNameVisible();
    }

    @Override
    public void setGlowing(boolean flag) {
        original.setGlowing(flag);
    }

    @Override
    public boolean isGlowing() {
        return original.isGlowing();
    }

    @Override
    public void setInvulnerable(boolean flag) {
        original.setInvulnerable(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return original.isInvulnerable();
    }

    @Override
    public boolean isSilent() {
        return original.isSilent();
    }

    @Override
    public void setSilent(boolean flag) {
        original.setSilent(flag);
    }

    @Override
    public boolean hasGravity() {
        return true;
    }

    @Override
    public void setGravity(boolean gravity) {
        // Gravity ticks invalid for non-tick-able entity
    }

    @Override
    public int getPortalCooldown() {
        return 0;
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        // Non-tick-able entity can access portals
    }

    @Override
    public @NotNull Set<String> getScoreboardTags() {
        return original.getScoreboardTags();
    }

    @Override
    public boolean addScoreboardTag(@NotNull String tag) {
        return original.addScoreboardTag(tag);
    }

    @Override
    public boolean removeScoreboardTag(@NotNull String tag) {
        return original.removeScoreboardTag(tag);
    }

    @Override
    public @NotNull PistonMoveReaction getPistonMoveReaction() {
        return original.getPistonMoveReaction();
    }

    @Override
    public @NotNull BlockFace getFacing() {
        return original.getFacing();
    }

    @Override
    public @NotNull Pose getPose() {
        return original.getPose();
    }

    @Override
    public @NotNull Spigot spigot() {
        return original.spigot();
    }

    public DataWatcher getDataWatcher() {
        return toNms().getDataWatcher();
    }

    @Override
    public T unwrap() {
        return original;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends T> wrappedClass() {
        return (Class) original.getClass();
    }

    public net.minecraft.server.Entity toNms() {
        return EntityUtil.toNms(original);
    }

    @Override
    public @Nullable String getCustomName() {
        return original.getCustomName();
    }

    @Override
    public void setCustomName(@Nullable String name) {
        original.setCustomName(name);
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
        original.setMetadata(metadataKey, newMetadataValue);
    }

    @Override
    public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        return original.getMetadata(metadataKey);
    }

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        return original.hasMetadata(metadataKey);
    }

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
        original.removeMetadata(metadataKey, owningPlugin);
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        return original.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        return original.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(@NotNull String name) {
        return original.hasPermission(name);
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        return original.hasPermission(perm);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        return original.addAttachment(plugin, name, value);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return original.addAttachment(plugin);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
        return original.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        return original.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment attachment) {
        original.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        original.recalculatePermissions();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return original.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean value) {
        // VirtualEntity cannot set to op
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return original.getPersistentDataContainer();
    }
}
