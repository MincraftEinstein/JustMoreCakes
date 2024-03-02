package einstein.jmc.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.util.Pair;
import einstein.jmc.entity.ai.behavior.HarvestSugarCane;
import einstein.jmc.init.ModVillagers;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;

@Mixin(VillagerGoalPackages.class)
public class VillagerGoalPackagesMixin {

    @ModifyExpressionValue(method = "getWorkPackage", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;", remap = false))
    private static ImmutableList<Pair<? extends BehaviorControl<Villager>, Integer>> runOne(ImmutableList<Pair<? extends BehaviorControl<Villager>, Integer>> original, VillagerProfession profession) {
        var list = new ArrayList<>(original);
        list.add(Pair.of(new HarvestSugarCane(), profession == ModVillagers.CAKE_BAKER.get() ? 2 : 5));
        return ImmutableList.copyOf(list);
    }
}
