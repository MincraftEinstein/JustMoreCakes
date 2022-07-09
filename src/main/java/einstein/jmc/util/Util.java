package einstein.jmc.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class Util {

    public static final ResourceLocation getBlockRegistryName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public static final ResourceLocation getItemRegistryName(ItemLike item) {
        return ForgeRegistries.ITEMS.getKey(item.asItem());
    }

    public static void createExplosion(final Level level, final BlockPos pos, final float size) {
        if (level.isClientSide) {
            return;
        }
        level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, size, Explosion.BlockInteraction.BREAK);
    }

    public static boolean teleportRandomly(final LivingEntity entity, final double radius) {
        final Random rand = new Random();
        int attempts;
        int tries;
        boolean teleported;
        double x;
        double y;
        double z;
        for (attempts = 20, tries = 0, teleported = false; !teleported && tries++ <= attempts; teleported = entity.randomTeleport(x, y, z, true)) {
            x = entity.xo + (rand.nextDouble() - rand.nextDouble()) * radius;
            y = entity.yo + (rand.nextDouble() - rand.nextDouble()) * radius;
            z = entity.zo + (rand.nextDouble() - rand.nextDouble()) * radius;
        }
        return teleported;
    }
}
