package einstein.jmc;

import java.util.function.Function;

import javax.annotation.Nullable;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModConfigs;
import einstein.jmc.init.ModPotions;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

@SuppressWarnings({"deprecation"})
@Mod(JustMoreCakes.MODID)
public class JustMoreCakes
{
    public static final String MODID = "jmc";
    public static JustMoreCakes instance;
    
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
        ModConfigs.init();
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
    	//Item.getIdFromItem(Items.CAKE = new BlockItem(Blocks.CAKE, (new Item.Properties()).maxStackSize(1).group(ItemGroup.FOOD)));
    	//Item.getIdFromItem(Items.CAKE.setMaxStackSize(64));	1.12.2 Version
    	//Item.getIdFromItem(Items.CAKE);
    	//Items.CAKE = new BlockItem(Blocks.CAKE, new Item.Properties().maxStackSize(0));
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
	
    public static class JMCItemGroup extends ItemGroup
    {
        public static final JMCItemGroup instance = new JMCItemGroup(ItemGroup.GROUPS.length, "jmc_tab");
        
        private JMCItemGroup(final int index, final String label) {
            super(index, label);
        }
        
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.CHOCOLATE_CAKE);
        }
    }
}
