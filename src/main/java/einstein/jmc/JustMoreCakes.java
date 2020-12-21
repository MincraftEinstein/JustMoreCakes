package einstein.jmc;

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
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings({"deprecation"})
@Mod(JustMoreCakes.MODID)
@EventBusSubscriber(modid = JustMoreCakes.MODID)
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
//    	Item.getIdFromItem(Items.CAKE);
//    	Items.CAKE = new BlockItem(Blocks.CAKE, new Item.Properties().maxStackSize(0));
    	DeferredWorkQueue.runLater(() -> {
    		ModPotions.registerPotionRecipes();
    	});
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
