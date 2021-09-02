package einstein.jmc.init;

import einstein.einsteins_library.util.RegistryHandler;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.blockentity.GlowstoneCakeBlockEntity;
import einstein.jmc.blockentity.TNTCakeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Bus.MOD)
public class ModTileEntityType {

	public static final BlockEntityType<GlowstoneCakeBlockEntity> GLOWSTONE_CAKE = RegistryHandler.registerBlockEntity(JustMoreCakes.MODID, "glowstone_cake", BlockEntityType.Builder.of(GlowstoneCakeBlockEntity::new, ModBlocks.GLOWSTONE_CAKE));
	public static final BlockEntityType<TNTCakeBlockEntity> TNT_CAKE = RegistryHandler.registerBlockEntity(JustMoreCakes.MODID, "tnt_cake", BlockEntityType.Builder.of(TNTCakeBlockEntity::new, ModBlocks.TNT_CAKE));
}
