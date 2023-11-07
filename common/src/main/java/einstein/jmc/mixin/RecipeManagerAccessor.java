package einstein.jmc.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor {

    @Accessor("recipes")
    Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>> getRecipes();

    @Accessor("recipes")
    void setRecipes(Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>> recipes);
}
