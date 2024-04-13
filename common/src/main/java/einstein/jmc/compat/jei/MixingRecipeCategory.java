package einstein.jmc.compat.jei;

import einstein.jmc.init.ModItems;
import einstein.jmc.item.crafting.MixingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class MixingRecipeCategory implements IRecipeCategory<MixingRecipe> {

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;

    public MixingRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(ModJEIPlugin.TEXTURE, 0, 44, 110, 59);
        icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.WHISK.get()));
        title = Component.translatable("gui.jei.jmc.category.mixing");
    }

    @Override
    public RecipeType<MixingRecipe> getRecipeType() {
        return ModJEIPlugin.MIXING_RECIPE;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MixingRecipe recipe, IFocusGroup focuses) {
        Level level = Objects.requireNonNull(Minecraft.getInstance().level);
        IRecipeSlotBuilder[] ingredientSlots = {
                builder.addSlot(RecipeIngredientRole.INPUT, 1, 5),
                builder.addSlot(RecipeIngredientRole.INPUT, 19, 5),
                builder.addSlot(RecipeIngredientRole.INPUT, 1, 23),
                builder.addSlot(RecipeIngredientRole.INPUT, 19, 23)
        };

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            ingredientSlots[i].addIngredients(recipe.getIngredients().get(i));
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, 47, 39).addItemStack(new ItemStack(ModItems.WHISK.get()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 78, 13).addItemStack(recipe.getResultItem(level.registryAccess()));
        builder.setShapeless();
    }

    @Override
    public void draw(MixingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        Component text = Component.translatable("gui.jei.jmc.category.mixing.uses", recipe.getMixingTime());
        guiGraphics.drawString(font, text, 68, 44, -8355712, false);
    }
}
