package einstein.jmc.init;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.entity.*;
import einstein.jmc.util.BlockEntitySupplier;
import einstein.jmc.util.DefaultCakeFamily;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static einstein.jmc.platform.Services.REGISTRY;

public class ModBlockEntityTypes {

    public static final Supplier<BlockEntityType<GlowstoneCakeBlockEntity>> GLOWSTONE_CAKE = registerForFamily("glowstone_cake", GlowstoneCakeBlockEntity::new, ModBlocks.GLOWSTONE_CAKE_FAMILY);
    public static final Supplier<BlockEntityType<TNTCakeBlockEntity>> TNT_CAKE = registerForCake("tnt_cake", TNTCakeBlockEntity::new, ModBlocks.TNT_CAKE_VARIANT.getCake());
    public static final Supplier<BlockEntityType<CakeOvenBlockEntity>> CAKE_OVEN = register("cake_oven", CakeOvenBlockEntity::new, ModBlocks.CAKE_OVEN);
    public static final Supplier<BlockEntityType<CakeStandBlockEntity>> CAKE_STAND = register("cake_stand", CakeStandBlockEntity::new, ModBlocks.CAKE_STAND);
    public static final Supplier<BlockEntityType<CeramicBowlBlockEntity>> CERAMIC_BOWL = register("ceramic_bowl", CeramicBowlBlockEntity::new, ModBlocks.CERAMIC_BOWL);

    public static void init() {
    }

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerForFamily(String name, BlockEntitySupplier<T> supplier, DefaultCakeFamily family) {
        return REGISTRY.registerBlockEntity(name, () -> {
            List<Supplier<? extends Block>> cakes = new ArrayList<>();
            family.forEach(cake -> {
                cakes.add(cake);
                cakes.addAll(cake.get().getVariant().getCandleCakeByCandle().values());
            });
            return REGISTRY.createBlockEntity(supplier, cakes.stream().map(Supplier::get).toArray(Block[]::new));
        });
    }

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerForCake(String name, BlockEntitySupplier<T> supplier, Supplier<? extends BaseCakeBlock> cake) {
        return REGISTRY.registerBlockEntity(name, () -> {
            List<Supplier<? extends Block>> cakes = new ArrayList<>(cake.get().getVariant().getCandleCakeByCandle().values());
            cakes.add(cake);
            return REGISTRY.createBlockEntity(supplier, cakes.stream().map(Supplier::get).toArray(Block[]::new));
        });
    }

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntitySupplier<T> supplier, Supplier<? extends Block> block) {
        return REGISTRY.registerBlockEntity(name, () -> REGISTRY.createBlockEntity(supplier, block.get()));
    }
}
