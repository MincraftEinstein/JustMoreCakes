package einstein.jmc.init;

import java.util.function.Supplier;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blockentity.CakeOvenBlockEntity;
import einstein.jmc.blockentity.GlowstoneCakeBlockEntity;
import einstein.jmc.blockentity.TNTCakeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntityTypes {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, JustMoreCakes.MODID);
	
	public static final RegistryObject<BlockEntityType<GlowstoneCakeBlockEntity>> GLOWSTONE_CAKE = register("glowstone_cake", () -> BlockEntityType.Builder.of(GlowstoneCakeBlockEntity::new, ModBlocks.GLOWSTONE_CAKE.get()).build(null));
	public static final RegistryObject<BlockEntityType<TNTCakeBlockEntity>> TNT_CAKE = register("tnt_cake", () -> BlockEntityType.Builder.of(TNTCakeBlockEntity::new, ModBlocks.TNT_CAKE.get()).build(null));
	public static final RegistryObject<BlockEntityType<CakeOvenBlockEntity>> CAKE_OVEN = register("cake_oven", () -> BlockEntityType.Builder.of(CakeOvenBlockEntity::new, ModBlocks.CAKE_OVEN.get()).build(null));
	
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(final String name, final Supplier<BlockEntityType<T>> blockEntity) {
		return BLOCK_ENTITIES.register(name, blockEntity);
	}
}
