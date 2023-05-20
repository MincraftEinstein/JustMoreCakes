package einstein.jmc.platform;

import einstein.jmc.CakeOvenTransferBlockEntity;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.blockentity.CakeOvenBlockEntity;
import einstein.jmc.platform.services.RegistryHelper;
import einstein.jmc.util.BlockEntitySupplier;
import einstein.jmc.util.MenuTypeSupplier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ForgeRegistryHelper implements RegistryHelper {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JustMoreCakes.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JustMoreCakes.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, JustMoreCakes.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, JustMoreCakes.MOD_ID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, JustMoreCakes.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, JustMoreCakes.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, JustMoreCakes.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, JustMoreCakes.MOD_ID);
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, JustMoreCakes.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, JustMoreCakes.MOD_ID);

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
}
