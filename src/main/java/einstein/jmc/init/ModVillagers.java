package einstein.jmc.init;

import java.lang.reflect.InvocationTargetException;

import com.google.common.collect.ImmutableSet;

import einstein.jmc.JustMoreCakes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.brain.task.GiveHeroGiftsTask;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(bus = Bus.MOD)
public class ModVillagers {
	public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, JustMoreCakes.MODID);
	public static final DeferredRegister<PointOfInterestType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, JustMoreCakes.MODID);

	public static final RegistryObject<PointOfInterestType> FURNACE = POI_TYPES.register("furnace", () -> new PointOfInterestType("furnace", PointOfInterestType.getAllStates(Blocks.FURNACE), 1, 1));
	public static final RegistryObject<VillagerProfession> CAKE_BAKER = PROFESSIONS.register("cake_baker", () -> new VillagerProfession("cake_baker", FURNACE.get(), ImmutableSet.of(Items.SUGAR, Items.WHEAT, Items.MILK_BUCKET), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_BUTCHER));

	public static void registerVillagers() {
		registerGifts();
		registerPointOfInterests();
	}
	
	private static void registerGifts() {
		GiveHeroGiftsTask.GIFTS.put(CAKE_BAKER.get(), new ResourceLocation(JustMoreCakes.MODID, "gameplay/hero_of_the_village/cake_baker_gift"));
	}

	private static void registerPointOfInterests() {
		try {
			ObfuscationReflectionHelper.findMethod(PointOfInterestType.class, "func_221052_a", PointOfInterestType.class).invoke(null, FURNACE.get());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}