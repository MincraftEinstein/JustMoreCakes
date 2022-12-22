package einstein.jmc.init;

import einstein.jmc.blockentity.CakeOvenBlockEntity;
import einstein.jmc.blockentity.GlowstoneCakeBlockEntity;
import einstein.jmc.blockentity.TNTCakeBlockEntity;
import einstein.jmc.platform.Services;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntityTypes {

    public static final Supplier<BlockEntityType<GlowstoneCakeBlockEntity>> GLOWSTONE_CAKE = Services.REGISTRY.registerBlockEntity("glowstone_cake", () -> Services.REGISTRY.createBlockEntity(GlowstoneCakeBlockEntity::new, ModBlocks.GLOWSTONE_CAKE.get()));
    public static final Supplier<BlockEntityType<TNTCakeBlockEntity>> TNT_CAKE = Services.REGISTRY.registerBlockEntity("tnt_cake", () -> Services.REGISTRY.createBlockEntity(TNTCakeBlockEntity::new, ModBlocks.TNT_CAKE.get()));
    public static final Supplier<BlockEntityType<CakeOvenBlockEntity>> CAKE_OVEN = Services.REGISTRY.registerBlockEntity("cake_oven", () -> Services.REGISTRY.createBlockEntity(CakeOvenBlockEntity::new, ModBlocks.CAKE_OVEN.get()));

    public static void init() {}
}
