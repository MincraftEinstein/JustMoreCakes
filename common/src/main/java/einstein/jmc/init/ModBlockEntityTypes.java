package einstein.jmc.init;

import einstein.jmc.blockentity.CakeOvenBlockEntity;
import einstein.jmc.blockentity.GlowstoneCakeBlockEntity;
import einstein.jmc.blockentity.TNTCakeBlockEntity;
import einstein.jmc.platform.Services;
import einstein.jmc.util.BlockEntitySupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

import static einstein.jmc.platform.Services.REGISTRY;

public class ModBlockEntityTypes {

    public static final Supplier<BlockEntityType<GlowstoneCakeBlockEntity>> GLOWSTONE_CAKE = register("glowstone_cake", GlowstoneCakeBlockEntity::new, ModBlocks.GLOWSTONE_CAKE);
    public static final Supplier<BlockEntityType<TNTCakeBlockEntity>> TNT_CAKE = register("tnt_cake", TNTCakeBlockEntity::new, ModBlocks.TNT_CAKE);
    public static final Supplier<BlockEntityType<CakeOvenBlockEntity>> CAKE_OVEN = register("cake_oven", CakeOvenBlockEntity::new, ModBlocks.CAKE_OVEN);

    public static void init() {
    }

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntitySupplier<T> supplier, Supplier<? extends Block> block) {
        return REGISTRY.registerBlockEntity(name, () -> REGISTRY.createBlockEntity(supplier, block.get()));
    }
}