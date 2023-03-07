package einstein.jmc.init;

import com.google.common.collect.ImmutableSet;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.platform.Services;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

public class ModVillagers {

    public static final Supplier<PoiType> CAKE_BAKER_POI = Services.REGISTRY.registerPOIType("cake_baker", () -> new PoiType(PoiTypes.getBlockStates(ModBlocks.CAKE_OVEN.get()), 1, 1));
    public static final Supplier<VillagerProfession> CAKE_BAKER = Services.REGISTRY.registerVillagerProfession("cake_baker", () -> new VillagerProfession("cake_baker", holder -> holder.is(JustMoreCakes.loc("cake_baker")), holder -> holder.is(JustMoreCakes.loc("cake_baker")), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_BUTCHER));

    public static void init() {}
}
