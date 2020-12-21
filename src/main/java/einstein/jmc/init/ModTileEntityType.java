package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.tileentity.GlowstoneCakeTileEntity;
import einstein.jmc.tileentity.TNTCakeTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityType {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, JustMoreCakes.MODID);

	public static final RegistryObject<TileEntityType<GlowstoneCakeTileEntity>> GLOWSTONE_CAKE = TILE_ENTITY_TYPES.register("glowstone_cake", () -> TileEntityType.Builder.create(GlowstoneCakeTileEntity::new, ModBlocks.GLOWSTONE_CAKE).build(null));
	public static final RegistryObject<TileEntityType<TNTCakeTileEntity>> TNT_CAKE = TILE_ENTITY_TYPES.register("tnt_cake", () -> TileEntityType.Builder.create(TNTCakeTileEntity::new, ModBlocks.TNT_CAKE).build(null));
}
