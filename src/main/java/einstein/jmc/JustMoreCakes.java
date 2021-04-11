package einstein.jmc;

import java.lang.reflect.Field;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.modlauncher.api.INameMappingService.Domain;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModClientConfigs;
import einstein.jmc.init.ModPotions;
import einstein.jmc.init.ModServerConfigs;
import einstein.jmc.init.ModTileEntityType;
import einstein.jmc.init.ModVillagers;
import einstein.jmc.util.EventHandler;
import einstein.jmc.world.gen.village.RegisterDesertBakery;
import einstein.jmc.world.gen.village.RegisterPlainsBakery;
import einstein.jmc.world.gen.village.RegisterSavannaBakery;
import einstein.jmc.world.gen.village.RegisterSnowyBakery;
import einstein.jmc.world.gen.village.RegisterTaigaBakery;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

@SuppressWarnings({ "deprecation" })
@Mod(JustMoreCakes.MODID)
public class JustMoreCakes
{
    public static final String MODID = "jmc";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final JMCItemGroup JMC_GROUP = new JMCItemGroup(ItemGroup.GROUPS.length, "jmc_tab");
    public static JustMoreCakes instance;
    private Field maxItemStackSize;
    
    public JustMoreCakes() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
		ModVillagers.PROFESSIONS.register(modEventBus);
		ModVillagers.POI_TYPES.register(modEventBus);
		ModPotions.POTIONS.register(modEventBus);
		ModPotions.POTION_EFFECTS.register(modEventBus);
		ModTileEntityType.TILE_ENTITY_TYPES.register(modEventBus);
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
		RegisterDesertBakery.updatePools();
		RegisterPlainsBakery.updatePools();
		RegisterSavannaBakery.updatePools();
		RegisterSnowyBakery.updatePools();
		RegisterTaigaBakery.updatePools();
    	DeferredWorkQueue.runLater(() -> {
    		ModVillagers.registerVillagers();
    	});
    }
	
    private void doClientStuff(final FMLClientSetupEvent event) {
    	DeferredWorkQueue.runLater(() -> {
    		ModPotions.registerPotionRecipes();
    	});
    }
    
	@Nullable
	private static Block missingBlock(String name) {
		switch (name) {
		case "cheese_cake":
			return ModBlocks.CHEESECAKE;
		}
		return null;
	}

	@SubscribeEvent
	void missingItems(final MissingMappings<Item> event) {
		handleMissingMappings(event, MODID, name -> {
			switch (name) {
			case "cheese_cake":
				return ModBlocks.CHEESECAKE.asItem();
			}
			IItemProvider block = missingBlock(name);
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
            final Field field = Item.class.getDeclaredField(ObfuscationReflectionHelper.remapName(Domain.FIELD, "field_77777_bU"));
            field.setAccessible(true);
            maxItemStackSize = field;
            LOGGER.debug("Item.Properties.maxStackSize is now accessible");
        }
        catch (Throwable t) {
            LOGGER.catching(Level.WARN, t);
        }
    }
    
    private void resizeCakeStack(FMLCommonSetupEvent event) {
		try {
			maxItemStackSize.setInt(Items.CAKE, 64);
			LOGGER.debug("Changed cake's max stack size to 64");
		}
		catch (Throwable t) {
			LOGGER.catching(Level.WARN, t);
		}
	}
}
