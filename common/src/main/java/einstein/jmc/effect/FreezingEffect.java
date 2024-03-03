package einstein.jmc.effect;

import einstein.jmc.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class FreezingEffect extends InstantenousMobEffect {

    public FreezingEffect(MobEffectCategory type, int liquidColor) {
        super(type, liquidColor);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyInstantenousEffect(Entity source, Entity indirectSource, LivingEntity entity, int amplifier, double health) {
        freezeEntity(entity);
    }

    public static void freezeEntity(LivingEntity entity) {
        if (entity instanceof Player && entity.isSpectator()) {
            return;
        }

        AABB boundingBox = entity.getBoundingBox().inflate(1);
        Level level = entity.level();
        for (int x = (int) Math.floor(boundingBox.minX); x < Math.ceil(boundingBox.maxX); x++) {
            for (int y = (int) Math.floor(boundingBox.minY); y < Math.ceil(boundingBox.maxY); y++) {
                for (int z = (int) Math.floor(boundingBox.minZ); z < Math.ceil(boundingBox.maxZ); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (level.isEmptyBlock(pos)) {
                        level.setBlockAndUpdate(pos, ModBlocks.ENCASING_ICE.get().defaultBlockState());
                    }
                }
            }
        }
    }
}
