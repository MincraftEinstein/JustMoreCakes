package einstein.jmc.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.util.CakeOvenConstants;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableAnimated.StartDirection;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CakeOvenRecipeCategory implements IRecipeCategory<CakeOvenRecipe>, CakeOvenConstants {

    private static final ResourceLocation TEXTURE = JustMoreCakes.loc("textures/gui/jei_cake_oven_gui.png"); // Image must be 256x256 px
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated flame;
    private final Component title;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public CakeOvenRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(TEXTURE, /*ImageX*/ 0, /*ImageY*/ 0, /*Width*/ 133, /*Height*/ 44);
        icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.CAKE_OVEN.get()));
        title = Component.translatable("gui.jei.jmc.category.cake_oven");
        flame = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(TEXTURE, /*ImageX*/ 133, /*ImageY*/ 0, /*Width*/ 14, /*Height*/ 14), /*AnimationTime*/ 300, StartDirection.TOP, true);
        cachedArrows = CacheBuilder.newBuilder().maximumSize(25L).build(new CacheLoader<>() {

            @Override
            public IDrawableAnimated load(Integer cookTime) {
                return guiHelper.drawableBuilder(TEXTURE, /*ImageX*/ 133, /*ImageY*/ 14, /*Width*/ 24, /*Height*/ 17).buildAnimated(/*AnimationTime*/ cookTime, StartDirection.LEFT, false);
            }
        });
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(CakeOvenRecipe recipe) {
        return getRecipeType().getUid();
    }

    @Override
    public RecipeType<CakeOvenRecipe> getRecipeType() {
        return JEIPlugin.CAKE_OVEN;
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
    public Component getTitle() {
        return title;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CakeOvenRecipe recipe, IFocusGroup focuses) {
        Level level = Objects.requireNonNull(Minecraft.getInstance().level);
        IRecipeSlotBuilder[] ingredientSlots = {
                builder.addSlot(RecipeIngredientRole.INPUT, /*X*/ 34, /*Y*/ 5),
                builder.addSlot(RecipeIngredientRole.INPUT, /*X*/ 52, /*Y*/ 5),
                builder.addSlot(RecipeIngredientRole.INPUT, /*X*/ 34, /*Y*/ 23),
                builder.addSlot(RecipeIngredientRole.INPUT, /*X*/ 52, /*Y*/ 23),
        };

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            ingredientSlots[i].addIngredients(recipe.getIngredients().get(i)); // Adds the ingredients for menu slot 'i' to JEI slot i
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, /*X*/ 112, /*Y*/ 14).addItemStack(recipe.getResultItem(level.registryAccess()));
        builder.setShapeless();
    }

    private IDrawableAnimated getArrow(CakeOvenRecipe recipe) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = DEFAULT_BURN_TIME;
        }

        return cachedArrows.getUnchecked(cookTime);
    }

    @Override
    public void draw(CakeOvenRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        flame.draw(guiGraphics, /*X*/ 1, /*Y*/ 4);
        getArrow(recipe).draw(guiGraphics, /*X*/ 75, /*Y*/ 13);
        drawExperienceText(recipe, guiGraphics, /*Y*/ 0);
        drawCookTimeText(recipe, guiGraphics, /*Y*/ 37);
    }

    private void drawExperienceText(CakeOvenRecipe recipe, GuiGraphics guiGraphics, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceText = Component.translatable("gui.jei.jmc.category.cake_oven.experience", experience);
            Font font = Minecraft.getInstance().font;
            guiGraphics.drawString(font, experienceText, /*X*/ (background.getWidth() - font.width(experienceText) - /*Makes room for the shapeless icon*/13), y, -8355712, false);
        }
    }

    private void drawCookTimeText(CakeOvenRecipe recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20; // Converts cook time in ticks to cook time in seconds
            Component cookTimeText = Component.translatable("gui.jei.jmc.category.cake_oven.time.seconds", cookTimeSeconds);
            Font font = Minecraft.getInstance().font;
            guiGraphics.drawString(font, cookTimeText, /*X*/ (background.getWidth() - font.width(cookTimeText)), y, -8355712, false);
        }
    }
}
