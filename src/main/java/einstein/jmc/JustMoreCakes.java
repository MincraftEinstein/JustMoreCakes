package einstein.jmc;

import java.lang.reflect.Field;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.modlauncher.api.INameMappingService.Domain;
import einstein.einsteins_library.util.RegistryHandler;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModClientConfigs;
import einstein.jmc.init.ModPotions;
import einstein.jmc.init.ModServerConfigs;
import einstein.jmc.util.EventHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
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
		RegistryHandler.registerVillageBuilding(MODID, "houses/plains_bakery_1", 1);
		RegistryHandler.registerVillageBuilding(MODID, "houses/plains_bakery_2", 1);
		RegistryHandler.registerVillageBuilding(MODID, "houses/desert_bakery_1", 1);
		RegistryHandler.registerVillageBuilding(MODID, "houses/savanna_bakery_1", 1);
		RegistryHandler.registerVillageBuilding(MODID, "houses/snowy_bakery_1", 1);
		RegistryHandler.registerVillageBuilding(MODID, "houses/snowy_bakery_2", 1);
		RegistryHandler.registerVillageBuilding(MODID, "houses/taiga_bakery_1", 1);
    }
	
	private void doClientStuff(final ParallelDispatchEvent event) {
		event.enqueueWork(() -> {
			ModPotions.registerPotionRecipes();
		});
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
