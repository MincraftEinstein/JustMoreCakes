package einstein.jmc.platform.services;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.menu.MenuDataProvider;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public interface CommonHooks {

    int getBurnTime(ItemStack stack);

    void fireSmeltEvent(Player player, ItemStack stack);

    void openMenu(ServerPlayer player, MenuDataProvider provider);

    void registerBrewingRecipe(Potion potion, Ingredient ingredient, Potion result);

    default void registerCompostable(ItemLike item, float chance) {
        if (item != null) {
            item = item.asItem();
            if (item != Items.AIR) {
                if (chance > 0 && chance <= 1) {
                    registerCompostableInternal(item, chance);
                    return;
                }
                JustMoreCakes.LOGGER.warn("Attempted to register {} with an invalid chance. Must be between 0 and 1", item.asItem());
                return;
            }
            JustMoreCakes.LOGGER.warn("Attempted to register an invalid item as a compostable");
        }
    }

    void registerCompostableInternal(ItemLike item, float chance);

    MinecraftServer getCurrentServer();
}
