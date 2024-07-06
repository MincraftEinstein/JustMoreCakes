package einstein.jmc.init;

import com.mojang.serialization.MapCodec;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.loot.AddItemLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, JustMoreCakes.MOD_ID);

    public static final RegistryObject<MapCodec<AddItemLootModifier>> ADD_ITEM = LOOT_MODIFIERS.register("add_item", AddItemLootModifier.CODEC);
}
