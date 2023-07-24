package einstein.jmc.platform;

import einstein.jmc.platform.services.RegistryHelper;
import einstein.jmc.util.BlockEntitySupplier;
import einstein.jmc.util.MenuTypeSupplier;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
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

import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.loc;

// Note: Registry entries MUST!!! be stored in a local variable before being put in a supplier
public class FabricRegistryHelper implements RegistryHelper {

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> type) {
        T item = Registry.register(BuiltInRegistries.ITEM, loc(name), type.get());
        Supplier<T> supplierItem = () -> item;
        CREATIVE_TAB_ITEMS.add(supplierItem);
        return supplierItem;
    }

    @Override
    public <T extends Block> Supplier<T> registerBlockNoItem(String name, Supplier<T> type) {
        T block = Registry.register(BuiltInRegistries.BLOCK, loc(name), type.get());
        return () -> block;
    }

    @Override
    public <T extends BlockEntityType<?>> Supplier<T> registerBlockEntity(String name, Supplier<T> type) {
        T blockEntity = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, loc(name), type.get());
        return () -> blockEntity;
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntity(BlockEntitySupplier<T> supplier, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(supplier::create, blocks).build();
    }

    @Override
    public <T extends Potion> Supplier<T> registerPotion(String name, Supplier<T> type) {
        T potion = Registry.register(BuiltInRegistries.POTION, loc(name), type.get());
        return () -> potion;
    }

    @Override
    public <T extends MobEffect> Supplier<T> registerMobEffect(String name, Supplier<T> type) {
        T effect = Registry.register(BuiltInRegistries.MOB_EFFECT, loc(name), type.get());
        return () -> effect;
    }

    @Override
    public <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String name, Supplier<T> type) {
        T serializer = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, loc(name), type.get());
        return () -> serializer;
    }

    @Override
    public <T extends RecipeType<?>> Supplier<T> registerRecipeType(String name, Supplier<T> type) {
        T recipeType = Registry.register(BuiltInRegistries.RECIPE_TYPE, loc(name), type.get());
        return () -> recipeType;
    }

    @Override
    public <T extends MenuType<?>> Supplier<T> registerMenuType(String name, Supplier<T> type) {
        T menuType = Registry.register(BuiltInRegistries.MENU, loc(name), type.get());
        return () -> menuType;
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuTypeSupplier<T> supplier) {
        return new ExtendedScreenHandlerType<>(supplier::create);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends PoiType> Supplier<T> registerPOIType(String name, Supplier<T> type) {
        T poi = (T) PointOfInterestHelper.register(loc(name), type.get().maxTickets(), type.get().validRange(), type.get().matchingStates());
        return () -> poi;
    }

    @Override
    public <T extends VillagerProfession> Supplier<T> registerVillagerProfession(String name, Supplier<T> type) {
        T profession = Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, loc(name), type.get());
        return () -> profession;
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String name, Function<CreativeModeTab.Builder, T> type) {
        T tab = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, loc(name), type.apply(FabricItemGroup.builder()));
        return () -> tab;
    }

    @Override
    public <T extends GameEvent> Supplier<GameEvent> registerGameEvent(String name, Supplier<T> type) {
        T gameEvent = Registry.register(BuiltInRegistries.GAME_EVENT, loc(name), type.get());
        return () -> gameEvent;
    }
}
