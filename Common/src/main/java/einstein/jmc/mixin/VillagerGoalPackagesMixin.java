package einstein.jmc.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import einstein.jmc.entity.ai.behavior.HarvestSugarCane;
import einstein.jmc.init.ModVillagers;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(VillagerGoalPackages.class)
public class VillagerGoalPackagesMixin {

    @Unique
    private static final ThreadLocal<VillagerProfession> CURRENT_PROFESSION = new ThreadLocal<>();

    @Inject(method = "getWorkPackage", at = @At("HEAD"))
    private static void method(VillagerProfession profession, float speedModifier, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> cir) {
        CURRENT_PROFESSION.set(profession);
    }

    @Redirect(method = "getWorkPackage", at = @At(value = "NEW", target = "(Ljava/util/List;)Lnet/minecraft/world/entity/ai/behavior/RunOne;"))
    private static RunOne<Villager> createRunOne(List<Pair<? extends BehaviorControl<? super Villager>, Integer>> immutableList) {
        List<Pair<? extends BehaviorControl<? super Villager>, Integer>> list = new ArrayList<>(immutableList);
        list.add(Pair.of(new HarvestSugarCane(), CURRENT_PROFESSION.get() == ModVillagers.CAKE_BAKER.get() ? 2 : 5));
        return new RunOne<>(list);
    }
}
