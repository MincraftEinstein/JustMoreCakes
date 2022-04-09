package einstein.jmc.item.crafting;

import java.util.ArrayList;
import java.util.List;

import einstein.jmc.init.ModRecipes;
import einstein.jmc.util.CakeOvenConstants;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

public class CakeOvenRecipe implements Recipe<Container>, CakeOvenConstants {

	protected final ResourceLocation id;
	protected final ItemStack result;
	protected final float experience;
	protected final int cookingTime;
	protected final NonNullList<Ingredient> ingredients;
	
	public CakeOvenRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result, float experience, int cookingTime) {
		this.id = id;
		this.ingredients = ingredients;
		this.result = result;
		this.experience = experience;
		this.cookingTime = cookingTime;
	}
	
	@Override
	public boolean matches(Container container, Level level) {
		List<ItemStack> inputs = new ArrayList<>();
		int stackCount = 0;
		
		for (int i = 0; i < container.getContainerSize(); i++) {
			if (i != RESULT_SLOT && i != FUEL_SLOT) {
				ItemStack stack = container.getItem(i);
				if (!stack.isEmpty()) {
					stackCount++;
					inputs.add(stack);
				}
			}
		}
		return stackCount == ingredients.size() && RecipeMatcher.findMatches(inputs, ingredients) != null;
	}
	
	@Override
	public ItemStack assemble(Container container) {
		return result.copy();
	}
	
	@Override
	public boolean canCraftInDimensions(int x, int y) {
		return true;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}
	
	@Override
	public ItemStack getResultItem() {
		return result;
	}
	
	@Override
	public ResourceLocation getId() {
		return id;
	}
	
	public float getExperience() {
		return experience;
	}
	
	public int getCookingTime() {
		return cookingTime;	
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.CAKE_OVEN_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return ModRecipes.CAKE_OVEN_RECIPE;
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}
	
	public void consumeIngredients(Container container) {
		for (Ingredient ingredient : ingredients) {
			for (int i2 = 0; i2 < container.getContainerSize(); i2++) {
				if (i2 != RESULT_SLOT && i2 != FUEL_SLOT) {
					ItemStack stack = container.getItem(i2);
					if (!stack.isEmpty() && ingredient.test(stack)) {
						
						stack.shrink(1);
						
		                if (stack.isEmpty()) {
		                    container.setItem(i2, ItemStack.EMPTY);
		                }
					}
				}
			}
		}
	}
}
