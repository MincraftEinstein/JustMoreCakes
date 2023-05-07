package einstein.jmc.compat.rei;

import einstein.jmc.menu.cakeoven.CakeOvenMenu;
import einstein.jmc.util.CakeOvenConstants;
import me.shedaniel.rei.api.common.transfer.RecipeFinder;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleGridMenuInfo;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;

public record CakeOvenMenuInfo(CakeOvenDisplay display) implements SimpleGridMenuInfo<CakeOvenMenu, CakeOvenDisplay> {

    @Override
    public CakeOvenDisplay getDisplay() {
        return display;
    }

    @Override
    public int getCraftingResultSlotIndex(CakeOvenMenu menu) {
        return CakeOvenConstants.RESULT_SLOT;
    }

    @Override
    public int getCraftingWidth(CakeOvenMenu menu) {
        return 2;
    }

    @Override
    public int getCraftingHeight(CakeOvenMenu menu) {
        return 2;
    }

    @Override
    public void clearInputSlots(CakeOvenMenu menu) {
        menu.getContainer().clearContent();
    }

    @Override
    public void populateRecipeFinder(MenuInfoContext<CakeOvenMenu, ?, CakeOvenDisplay> context, RecipeFinder finder) {
        ((StackedContentsCompatible) context.getMenu().getContainer()).fillStackedContents(new StackedContents() {

            @Override
            public void accountSimpleStack(ItemStack stack) {
                finder.addNormalItem(stack);
            }
        });
    }
}
