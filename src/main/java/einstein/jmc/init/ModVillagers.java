package einstein.jmc.init;

import einstein.einsteins_library.util.RegistryHandler;
import einstein.jmc.JustMoreCakes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Bus.MOD)
public class ModVillagers {

	public static final PoiType CAKE_BAKER_POI = RegistryHandler.registerPOI(JustMoreCakes.MODID, "cake_baker", ModBlocks.CAKE_OVEN);
	public static final VillagerProfession CAKE_BAKER = RegistryHandler.registerProfession(JustMoreCakes.MODID, "cake_baker", CAKE_BAKER_POI, SoundEvents.VILLAGER_WORK_BUTCHER, "gameplay/hero_of_the_village/cake_baker_gift");
}