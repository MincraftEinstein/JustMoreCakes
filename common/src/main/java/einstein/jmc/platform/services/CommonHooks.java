package einstein.jmc.platform.services;

import einstein.jmc.menu.MenuDataProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;

public interface CommonHooks {

    int getBurnTime(ItemStack stack);

    void fireSmeltEvent(Player player, ItemStack stack);

    void openMenu(ServerPlayer player, MenuDataProvider provider);

    void registerBrewingRecipe(Potion potion, Ingredient ingredient, Potion result);
}
