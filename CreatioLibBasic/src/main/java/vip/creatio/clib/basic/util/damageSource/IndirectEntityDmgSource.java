package vip.creatio.clib.basic.util.damageSource;

import vip.creatio.clib.basic.util.EntityUtil;
import net.minecraft.server.EntityDamageSourceIndirect;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class IndirectEntityDmgSource extends EntityDmgSource {

    IndirectEntityDmgSource(EntityDamageSourceIndirect source) {
        super(source);
    }

    public IndirectEntityDmgSource(String msgId, Entity entity, @Nullable Entity owner) {
        super(new EntityDamageSourceIndirect(msgId, EntityUtil.toNms(entity),
                owner == null ? null : EntityUtil.toNms(owner)));
    }
}
