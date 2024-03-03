package einstein.jmc.platform;

import einstein.jmc.menu.MenuDataProvider;
import einstein.jmc.platform.services.CommonHooks;
import einstein.jmc.platform.services.NetworkHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

public class ForgeCommonHooks implements CommonHooks {

    @Override
    public int getBurnTime(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null);
    }

    @Override
    public void fireSmeltEvent(Player player, ItemStack stack) {
        ForgeEventFactory.firePlayerSmeltedEvent(player, stack);
    }

    @Override
    public void openMenu(ServerPlayer player, MenuDataProvider provider) {
        NetworkHooks.openScreen(player, provider, buf -> provider.writeMenuData(player, buf));
    }

    @Override
    public void registerBrewingRecipe(Potion potion, Ingredient ingredient, Potion result) {
        BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), potion)), ingredient, PotionUtils.setPotion(new ItemStack(Items.POTION), result));
    }

    @Override
    public void registerCompostableInternal(ItemLike item, float chance) {
        ComposterBlock.COMPOSTABLES.put(item, chance);
    }
}
