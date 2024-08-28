package einstein.jmc.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddItemLootModifier extends LootModifier {

    public static final Supplier<MapCodec<AddItemLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(instance -> codecStart(instance).and(
            ItemStack.CODEC.fieldOf("item").forGetter(modifier -> modifier.stack)
    ).apply(instance, AddItemLootModifier::new)));

    private final ItemStack stack;

    public AddItemLootModifier(LootItemCondition[] conditions, ItemStack stack) {
        super(conditions);
        this.stack = stack;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ItemStack newStack = stack.copy();
        int count = newStack.getCount();

        while (count > newStack.getMaxStackSize()) {
            int amount = count - newStack.getMaxStackSize();
            generatedLoot.add(newStack.split(amount));
            count -= amount;
        }

        generatedLoot.add(newStack);
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
