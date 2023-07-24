package einstein.jmc.platform.services;

import einstein.jmc.util.BlockEntitySupplier;
import einstein.jmc.util.MenuTypeSupplier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface RegistryHelper {

    List<Supplier<? extends Item>> CREATIVE_TAB_ITEMS = new ArrayList<>();

    <T extends Item> Supplier<T> registerItem(String name, Supplier<T> type);

    default <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> type) {
        Supplier<T> block = registerBlockNoItem(name, type);
        registerItem(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    <T extends Block> Supplier<T> registerBlockNoItem(String name, Supplier<T> type);

    <T extends BlockEntityType<?>> Supplier<T> registerBlockEntity(String name, Supplier<T> type);

    <T extends BlockEntity> BlockEntityType<T> createBlockEntity(BlockEntitySupplier<T> supplier, Block... blocks);

    <T extends Potion> Supplier<T> registerPotion(String name, Supplier<T> type);

    <T extends MobEffect> Supplier<T> registerMobEffect(String name, Supplier<T> type);

    <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String name, Supplier<T> type);

    <T extends RecipeType<?>> Supplier<T> registerRecipeType(String name, Supplier<T> type);

    <T extends MenuType<?>> Supplier<T> registerMenuType(String name, Supplier<T> type);

    <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuTypeSupplier<T> supplier);

    <T extends PoiType> Supplier<T> registerPOIType(String name, Supplier<T> type);

    <T extends VillagerProfession> Supplier<T> registerVillagerProfession(String name, Supplier<T> type);

    <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String name, Function<CreativeModeTab.Builder, T> type);

    <T extends GameEvent> Supplier<GameEvent> registerGameEvent(String name, Supplier<T> type);
}
