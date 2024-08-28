package einstein.jmc.data.packs.providers.farmersdelight;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.registration.CakeVariant;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

import static einstein.jmc.data.packs.providers.ModLootModifiersProvider.addCakeSpatulaDropModifier;
import static einstein.jmc.data.packs.providers.ModLootModifiersProvider.addKnifeDropModifier;

public class FDSupportLootModifiers extends GlobalLootModifierProvider {

    public FDSupportLootModifiers(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, JustMoreCakes.MOD_ID);
    }

    @Override
    protected void start() {
        add("sweet_berry_cheesecake", addCakeSpatulaDropModifier(vectorwing.farmersdelight.common.registry.ModBlocks.SWEET_BERRY_CHEESECAKE.get()));
        add("apple_pie", addCakeSpatulaDropModifier(vectorwing.farmersdelight.common.registry.ModBlocks.APPLE_PIE.get()));
        add("chocolate_pie", addCakeSpatulaDropModifier(vectorwing.farmersdelight.common.registry.ModBlocks.CHOCOLATE_PIE.get()));
        Block cupcake = ModBlocks.CUPCAKE_VARIANT.getCake().get();
        add("cupcake", addKnifeDropModifier(cupcake, ModItems.CAKE_SLICE.get(), 2));
        ModBlocks.VANILLA_CAKE_FAMILY.forEach(cakeBlock -> {
            if (cakeBlock != null) {
                CakeVariant variant = cakeBlock.get().getVariant();
                int count = cakeBlock.get().getSlices() + 1;

                add(variant.getCakeName(), addKnifeDropModifier(cakeBlock.get(), ModItems.CAKE_SLICE.get(), count));

                variant.getCandleCakeByCandle().forEach((candleBlock, candleCakeBlock) -> {
                    add(BuiltInRegistries.BLOCK.getKey(candleCakeBlock.get()).getPath(),
                            addKnifeDropModifier(candleCakeBlock.get(), ModItems.CAKE_SLICE.get(), count)
                    );
                });
            }
        });
    }
}
