package einstein.jmc.platform;

import einstein.jmc.menu.MenuDataProvider;
import einstein.jmc.platform.services.CommonHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

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
        PotionBrewing.POTION_MIXES.add(new PotionBrewing.Mix<>(ForgeRegistries.POTIONS, potion, ingredient, result));
    }

    @Override
    public void registerCompostableInternal(ItemLike item, float chance) {
        ComposterBlock.COMPOSTABLES.put(item, chance);
    }

    @Override
    public MinecraftServer getCurrentServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }
}
