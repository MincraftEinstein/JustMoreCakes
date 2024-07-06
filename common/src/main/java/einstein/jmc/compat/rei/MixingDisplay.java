package einstein.jmc.compat.rei;

import einstein.jmc.item.crafting.MixingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MixingDisplay extends BasicDisplay {

    private final int mixingTime;

    public MixingDisplay(RecipeHolder<MixingRecipe> holder) {
        this(EntryIngredients.ofIngredients(holder.value().getIngredients()),
                Collections.singletonList(EntryIngredients.of(holder.value().getResultItem(registryAccess()))),
                holder, holder.value().getMixingTime());
    }

    public MixingDisplay(List<EntryIngredient> ingredients, List<EntryIngredient> results, CompoundTag tag) {
        this(ingredients, results, RecipeManagerContext.getInstance().byId(tag, "location"), tag.getInt("MixingTime"));
    }

    public MixingDisplay(List<EntryIngredient> ingredients, List<EntryIngredient> results, RecipeHolder<?> holder, int mixingTime) {
        super(CakeOvenDisplay.fillWithEmpty(ingredients), results, Optional.ofNullable(holder).map(RecipeHolder::id));
        this.mixingTime = mixingTime;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ModREICommonPlugin.MIXING;
    }

    public int getMixingTime() {
        return mixingTime;
    }

    public static BasicDisplay.Serializer<MixingDisplay> serializer() {
        return Serializer.ofRecipeLess(MixingDisplay::new, (display, tag) -> {
            tag.putInt("MixingTime", display.getMixingTime());
        });
    }
}
