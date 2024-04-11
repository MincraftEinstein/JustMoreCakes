package einstein.jmc.platform;

import einstein.jmc.block.entity.CakeOvenBlockEntity;
import einstein.jmc.block.entity.CakeOvenTransferBlockEntity;
import einstein.jmc.block.entity.CeramicBlockTransferBlockEntity;
import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import einstein.jmc.platform.services.RegistryHelper;
import einstein.jmc.util.BlockEntitySupplier;
import einstein.jmc.util.MenuTypeSupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.MOD_ID;

public class ForgeRegistryHelper implements RegistryHelper {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, MOD_ID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredRegister<GameEvent> GAME_EVENTS = DeferredRegister.create(Registries.GAME_EVENT, MOD_ID);

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> type) {
        Supplier<T> item = ITEMS.register(name, type);
        CREATIVE_TAB_ITEMS.add(item);
        return item;
    }

    @Override
    public <T extends Block> Supplier<T> registerBlockNoItem(String name, Supplier<T> type) {
        return BLOCKS.register(name, type);
    }

    @Override
    public <T extends BlockEntityType<?>> Supplier<T> registerBlockEntity(String name, Supplier<T> type) {
        return BLOCK_ENTITIES.register(name, type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntity(BlockEntitySupplier<T> supplier, Block... blocks) {
        return BlockEntityType.Builder.of((pos, state) -> {
            T t = supplier.create(pos, state);
            if (t instanceof CakeOvenBlockEntity) {
                return (T) new CakeOvenTransferBlockEntity(pos, state);
            }
            else if (t instanceof CeramicBowlBlockEntity) {
                return (T) new CeramicBlockTransferBlockEntity(pos, state);
            }
            return t;
        }, blocks).build(null);
    }

    @Override
    public <T extends Potion> Supplier<T> registerPotion(String name, Supplier<T> type) {
        return POTIONS.register(name, type);
    }

    @Override
    public <T extends MobEffect> Supplier<T> registerMobEffect(String name, Supplier<T> type) {
        return MOB_EFFECTS.register(name, type);
    }

    @Override
    public <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String name, Supplier<T> type) {
        return RECIPE_SERIALIZERS.register(name, type);
    }

    @Override
    public <T extends RecipeType<?>> Supplier<T> registerRecipeType(String name, Supplier<T> type) {
        return RECIPE_TYPES.register(name, type);
    }

    @Override
    public <T extends MenuType<?>> Supplier<T> registerMenuType(String name, Supplier<T> type) {
        return MENU_TYPES.register(name, type);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuTypeSupplier<T> supplier) {
        return IForgeMenuType.create(supplier::create);
    }

    @Override
    public <T extends PoiType> Supplier<T> registerPOIType(String name, Supplier<T> type) {
        return POI_TYPES.register(name, type);
    }

    @Override
    public <T extends VillagerProfession> Supplier<T> registerVillagerProfession(String name, Supplier<T> type) {
        return VILLAGER_PROFESSIONS.register(name, type);
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String name, Function<CreativeModeTab.Builder, T> type) {
        return CREATIVE_MODE_TABS.register(name, () -> type.apply(CreativeModeTab.builder()));
    }

    @Override
    public <T extends GameEvent> Supplier<GameEvent> registerGameEvent(String name, Supplier<T> type) {
        return GAME_EVENTS.register(name, type);
    }
}
