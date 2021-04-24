package vip.creatio.clib.basic.util.damageSource;

import vip.creatio.clib.basic.tools.Wrapper;
import vip.creatio.clib.basic.chat.Component;
import vip.creatio.accessor.Func;
import vip.creatio.accessor.Reflection;
import net.minecraft.server.*;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import vip.creatio.clib.basic.util.EntityUtil;

import static vip.creatio.clib.basic.util.EntityUtil.toNms;
import static vip.creatio.clib.basic.util.EntityUtil.toBukkit;

public class DmgSource implements Wrapper<DamageSource> {

    public static final DmgSource FIRE = new DmgSource(DamageSource.FIRE);
    public static final DmgSource LIGHTNING = new DmgSource(DamageSource.LIGHTNING);
    public static final DmgSource BURN = new DmgSource(DamageSource.BURN);
    public static final DmgSource LAVA = new DmgSource(DamageSource.LAVA);
    public static final DmgSource HOT_FLOOR = new DmgSource(DamageSource.HOT_FLOOR);
    public static final DmgSource STUCK = new DmgSource(DamageSource.STUCK);
    public static final DmgSource CRAMMING = new DmgSource(DamageSource.CRAMMING);
    public static final DmgSource DROWN = new DmgSource(DamageSource.DROWN);
    public static final DmgSource STARVE = new DmgSource(DamageSource.STARVE);
    public static final DmgSource CACTUS = new DmgSource(DamageSource.CACTUS);
    public static final DmgSource FALL = new DmgSource(DamageSource.FALL);
    public static final DmgSource FLY_INTO_WALL = new DmgSource(DamageSource.FLY_INTO_WALL);
    public static final DmgSource OUT_OF_WORLD = new DmgSource(DamageSource.OUT_OF_WORLD);
    public static final DmgSource GENERIC = new DmgSource(DamageSource.GENERIC);
    public static final DmgSource MAGIC = new DmgSource(DamageSource.MAGIC);
    public static final DmgSource WITHER = new DmgSource(DamageSource.WITHER);
    public static final DmgSource ANVIL = new DmgSource(DamageSource.ANVIL);
    public static final DmgSource FALLING_BLOCK = new DmgSource(DamageSource.FALLING_BLOCK);
    public static final DmgSource DRAGON_BREATH = new DmgSource(DamageSource.DRAGON_BREATH);
    public static final DmgSource DRYOUT = new DmgSource(DamageSource.DRYOUT);
    public static final DmgSource SWEET_BERRY_BUSH = new DmgSource(DamageSource.SWEET_BERRY_BUSH);

    protected final DamageSource wrapper;

    DmgSource(DamageSource source) {
        this.wrapper = source;
    }

    public static DmgSource sting(LivingEntity entity) {
        return new EntityDmgSource((EntityDamageSource)
                DamageSource.b /* sting */ (EntityUtil.toNms(entity)));
    }

    public static DmgSource mobAttack(LivingEntity entity) {
        return new EntityDmgSource((EntityDamageSource)
                DamageSource.mobAttack(EntityUtil.toNms(entity)));
    }

    public static DmgSource indirectMobAttack(Entity direct, LivingEntity indirect) {
        return new IndirectEntityDmgSource((EntityDamageSourceIndirect)
                DamageSource.a /* indirectMobAttack */ (EntityUtil.toNms(direct), EntityUtil.toNms(indirect)));
    }

    public static DmgSource playerAttack(HumanEntity human) {
        return new EntityDmgSource((EntityDamageSource)
                DamageSource.playerAttack(EntityUtil.toNms(human)));
    }

    public DmgSource arrow(AbstractArrow arrow, @Nullable Entity shooter) {
        return new IndirectEntityDmgSource((EntityDamageSourceIndirect)
                DamageSource.arrow((EntityArrow) EntityUtil.toNms(arrow), shooter == null ? null : EntityUtil.toNms(shooter)));
    }

    public DmgSource trident(Trident trident, @Nullable Entity shooter) {
        return new IndirectEntityDmgSource((EntityDamageSourceIndirect)
                DamageSource.a(EntityUtil.toNms(trident), shooter == null ? null : EntityUtil.toNms(shooter)));
    }

    public DmgSource fireworks(Firework firework, @Nullable Entity shooter) {
        return new IndirectEntityDmgSource((EntityDamageSourceIndirect)
                DamageSource.a((EntityFireworks) EntityUtil.toNms(firework), shooter == null ? null : EntityUtil.toNms(shooter)));
    }

    public DmgSource fireball(SizedFireball fireball, @Nullable Entity shooter) {
        return new IndirectEntityDmgSource((EntityDamageSourceIndirect)
                DamageSource.fireball((EntityFireballFireball) EntityUtil.toNms(fireball), shooter == null ? null : EntityUtil.toNms(shooter)));
    }

    public DmgSource witherSkull(WitherSkull skull, @Nullable Entity shooter) {
        return new IndirectEntityDmgSource((EntityDamageSourceIndirect)
                DamageSource.a((EntityWitherSkull) EntityUtil.toNms(skull), shooter == null ? null : EntityUtil.toNms(shooter)));
    }

    public DmgSource thrown(Entity thrown, @Nullable Entity shooter) {
        return new IndirectEntityDmgSource((EntityDamageSourceIndirect)
                DamageSource.projectile(EntityUtil.toNms(thrown), shooter == null ? null : EntityUtil.toNms(shooter)));
    }

    public DmgSource indirectMagic(Entity magic, @Nullable Entity shooter) {
        return new IndirectEntityDmgSource((EntityDamageSourceIndirect)
                DamageSource.c(EntityUtil.toNms(magic), shooter == null ? null : EntityUtil.toNms(shooter)));
    }

    public DmgSource thorns(Entity attacker) {
        return new EntityDmgSource((EntityDamageSource)
                DamageSource.a(EntityUtil.toNms(attacker)));
    }

    public boolean isBypassArmor() {
        return wrapper.ignoresArmor();
    }

    private static final Func<DamageSource> SET_BYPASS_ARMOR =
            Reflection.method(DamageSource.class, "setIgnoreArmor");
    public DmgSource setBypassArmor() {
        SET_BYPASS_ARMOR.invoke(wrapper);
        return this;
    }

    public boolean isBypassInvul() {
        return wrapper.ignoresInvulnerability();
    }

    private static final Func<DamageSource> SET_BYPASS_INVUL =
            Reflection.method(DamageSource.class, "setIgnoresInvulnerability");
    public DmgSource setBypassInvul() {
        SET_BYPASS_INVUL.invoke(wrapper);
        return this;
    }

    public boolean isBypassStarvation() {
        return wrapper.isStarvation();
    }

    private static final Func<DamageSource> SET_BYLASS_STARVATION =
            Reflection.method(DamageSource.class, "setStarvation");
    protected DmgSource setBypassStarvation() {
        SET_BYLASS_STARVATION.invoke(wrapper);
        return this;
    }

    public float getExhaustion() {
        return wrapper.getExhaustionCost();
    }

    @Nullable
    public Entity getDirectEntity() {
        net.minecraft.server.Entity e = wrapper.j /* getDirectEntity */ ();
        return e == null ? null : EntityUtil.toBukkit(e);
    }

    @Nullable
    public Entity getEntity() {
        net.minecraft.server.Entity e = wrapper.getEntity();
        return e == null ? null : EntityUtil.toBukkit(e);
    }

    public boolean isFireSource() {
        return wrapper.isFire();
    }

    private static final Func<DamageSource> SET_FIRE_SOURCE =
            Reflection.method(DamageSource.class, "setFire");
    protected DmgSource setFireSource() {
        SET_FIRE_SOURCE.invoke(wrapper);
        return this;
    }

    public boolean isProjectile() {
        return wrapper.b /* isProjectile */ ();
    }

    public boolean scalesWithDifficulty() {
        return wrapper.s /* scalesWithDifficulty */ ();
    }

    public boolean isMagic() {
        return wrapper.isMagic();
    }

    public DmgSource setMagic() {
        wrapper.setMagic();
        return this;
    }

    public boolean isExplosion() {
        return wrapper.isExplosion();
    }

    public String getMsgId() {
        return wrapper.q /* getMsgId */ ();
    }

    public boolean isCreativePlayer() {
        return wrapper.v /* isCreativePlayer*/ ();
    }

    @Nullable
    public Location getSourcePosition() {
        return null;
    }

    public Component getLocalizedDeathMessage(LivingEntity killer) {
        return Component.wrap(wrapper.getLocalizedDeathMessage(EntityUtil.toNms(killer)));
    }

    public static DmgSource wrap(DamageSource source) {
        if (source instanceof EntityDamageSourceIndirect) {
            return new IndirectEntityDmgSource((EntityDamageSourceIndirect) source);
        } else if (source instanceof EntityDamageSource) {
            return new EntityDmgSource((EntityDamageSource) source);
        } else {
            return new DmgSource(source);
        }
    }

    @Override
    public String toString() {
        return wrapper.toString();
    }

    @Override
    public DamageSource unwrap() {
        return wrapper;
    }

    @Override
    public Class<? extends DamageSource> wrappedClass() {
        return wrapper.getClass();
    }
}
