package einstein.jmc;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.datafixers.util.Pair;

import cpw.mods.modlauncher.api.INameMappingService.Domain;
import einstein.jmc.init.ModBlockEntityTypes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModClientConfigs;
import einstein.jmc.init.ModItems;
import einstein.jmc.init.ModMenuTypes;
import einstein.jmc.init.ModPotions;
import einstein.jmc.init.ModRecipes;
import einstein.jmc.init.ModServerConfigs;
import einstein.jmc.init.ModVillagers;
import einstein.jmc.util.EventHandler;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.DesertVillagePools;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.data.worldgen.SavannaVillagePools;
import net.minecraft.data.worldgen.SnowyVillagePools;
import net.minecraft.data.worldgen.TaigaVillagePools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.pools.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.coremod.api.ASMAPI;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod(JustMoreCakes.MODID)
public class JustMoreCakes
{
    public static final String MODID = "jmc";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final JMCTab JMC_TAB = new JMCTab(CreativeModeTab.TABS.length, "jmc_tab");
    public static JustMoreCakes instance;
    private Field maxItemStackSize;
    
    public JustMoreCakes() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addGenericListener(RecipeSerializer.class, this::registerRecipeType);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.init(modEventBus);
        ModBlockEntityTypes.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        ModVillagers.POIS.register(modEventBus);
        ModVillagers.PROFESSIONS.register(modEventBus);
        ModPotions.MOB_EFFECTS.register(modEventBus);
        ModPotions.POTIONS.register(modEventBus);
        JustMoreCakes.instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModServerConfigs.SERVERSPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModClientConfigs.CLIENTSPEC);
        accessStackSize();
        if (maxItemStackSize != null) {
        	modEventBus.addListener(this::resizeCakeStack);
        }
    }
    
	private void setup(final FMLCommonSetupEvent event) {
		registerVillageBuilding("houses/plains_bakery_1");
		registerVillageBuilding("houses/plains_bakery_2");
		registerVillageBuilding("houses/desert_bakery_1");
		registerVillageBuilding("houses/savanna_bakery_1");
		registerVillageBuilding("houses/snowy_bakery_1");
		registerVillageBuilding("houses/snowy_bakery_2");
		registerVillageBuilding("houses/taiga_bakery_1");
		GiveGiftToHero.GIFTS.put(ModVillagers.CAKE_BAKER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/cake_baker_gift"));
    }
	
	@SuppressWarnings("unchecked")
	public static void registerVillageBuilding(final String location) {
    	PlainVillagePools.bootstrap();
    	DesertVillagePools.bootstrap();
    	SavannaVillagePools.bootstrap();
    	SnowyVillagePools.bootstrap();
    	TaigaVillagePools.bootstrap();
		for (String biome : new String[] { "plains", "snowy", "savanna", "desert", "taiga" }) {
        	final StructureTemplatePool pattern = BuiltinRegistries.TEMPLATE_POOL.get(new ResourceLocation("minecraft:village/" + biome + "/houses"));
            if (pattern == null) {
                return;
            }
            final Function<StructureTemplatePool.Projection, LegacySinglePoolElement> element = StructurePoolElement.legacy(MODID + ":village/" + biome + "/" + location, ProcessorLists.MOSSIFY_10_PERCENT);
            final StructurePoolElement structure = element.apply(StructureTemplatePool.Projection.RIGID);
            try {
                final String name = ASMAPI.mapField("f_210560_");
                final Field field = StructureTemplatePool.class.getDeclaredField(name);
                field.setAccessible(true);
                final String name2 = ASMAPI.mapField("f_210559_");
                final Field field2 = StructureTemplatePool.class.getDeclaredField(name2);
                field2.setAccessible(true);
    			final List<StructurePoolElement> list = (List<StructurePoolElement>)field.get(pattern);
                for (int i = 0; i < 1; ++i) {
                    list.add(structure);
                }
                final List<Pair<StructurePoolElement, Integer>> list2 = (List<Pair<StructurePoolElement, Integer>>)field2.get(pattern);
                list2.add(Pair.of(structure, 1));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    	}
    }
	
	private void doClientStuff(final ParallelDispatchEvent event) {
		event.enqueueWork(() -> {
			ModPotions.registerPotionRecipes();
		});
	}
	
	private void registerRecipeType(RegistryEvent.Register<RecipeSerializer<?>> event) {
		Registry.register(Registry.RECIPE_TYPE, ModRecipes.CAKE_OVEN_RECIPE.toString(), ModRecipes.CAKE_OVEN_RECIPE);
	}
	
	@Nullable
	private static Block missingBlock(String name) {
		switch (name) {
		case "cheese_cake":
			return ModBlocks.getBlock(ModBlocks.RL("cheesecake"));
		case "triple_decker_cake":
			return ModBlocks.getBlock(ModBlocks.RL("three_tiered_cake"));
		}
		return null;
	}
	
	@SubscribeEvent
	void missingItems(final MissingMappings<Item> event) {
		handleMissingMappings(event, MODID, name -> {
			switch (name) {
			case "cheese_cake":
				return ModBlocks.getBlock(ModBlocks.RL("cheesecake")).asItem();
			case "triple_decker_cake":
				return ModBlocks.getBlock(ModBlocks.RL("three_tiered_cake")).asItem();
			}
			ItemLike block = missingBlock(name);
			return block == null ? null : block.asItem();
		});
	}
	
	@SubscribeEvent
	void missingBlocks(final MissingMappings<Block> event) {
		handleMissingMappings(event, MODID, JustMoreCakes::missingBlock);
	}
	
	public static <T extends IForgeRegistryEntry<T>> void handleMissingMappings(MissingMappings<T> event, String modID, Function<String, T> handler) {
		for (Mapping<T> mapping : event.getAllMappings()) {
			if (modID.equals(mapping.key.getNamespace())) {
				@Nullable
				T value = handler.apply(mapping.key.getPath());
				if (value != null) {
					mapping.remap(value);
				}
			}
		}
	}
	
    private void accessStackSize() {
        try {
            final Field field = Item.class.getDeclaredField(ObfuscationReflectionHelper.remapName(Domain.FIELD, "f_41478_"));
            field.setAccessible(true);
            maxItemStackSize = field;
        }
        catch (Throwable t) {
            LOGGER.catching(Level.WARN, t);
        }
    }
    
    private void resizeCakeStack(FMLCommonSetupEvent event) {
		try {
			maxItemStackSize.setInt(Items.CAKE, 64);
		}
		catch (Throwable t) {
			LOGGER.catching(Level.WARN, t);
		}
	}
}
