package einstein.jmc.init;

import com.google.common.collect.ImmutableSet;

import einstein.jmc.JustMoreCakes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModVillagers {

	public static final DeferredRegister<PoiType> POIS = DeferredRegister.create(ForgeRegistries.POI_TYPES, JustMoreCakes.MODID);
	public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, JustMoreCakes.MODID);
	
	public static final RegistryObject<PoiType> CAKE_BAKER_POI = POIS.register("cake_baker", () -> new PoiType("cake_baker", PoiType.getBlockStates(ModBlocks.CAKE_OVEN.get()), 1, 1));
	public static final RegistryObject<VillagerProfession> CAKE_BAKER = PROFESSIONS.register("cake_baker", () -> new VillagerProfession("cake_baker", CAKE_BAKER_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_BUTCHER));
}