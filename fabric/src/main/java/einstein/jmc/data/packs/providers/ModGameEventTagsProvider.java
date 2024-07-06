package einstein.jmc.data.packs.providers;

import einstein.jmc.data.packs.ModGameEventTags;
import einstein.jmc.init.ModGameEvents;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.GameEventTags;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.concurrent.CompletableFuture;

public class ModGameEventTagsProvider extends FabricTagProvider<GameEvent> {

    public ModGameEventTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.GAME_EVENT, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(ModGameEventTags.STEALTH_EFFECT_BLOCKS).add(GameEvent.DRINK.key(),
                GameEvent.EQUIP.key(), GameEvent.EAT.key(), GameEvent.ELYTRA_GLIDE.key(), GameEvent.HIT_GROUND.key(),
                GameEvent.TELEPORT.key(), GameEvent.ITEM_INTERACT_START.key(), GameEvent.ITEM_INTERACT_FINISH.key(),
                GameEvent.PROJECTILE_SHOOT.key(), GameEvent.STEP.key(), GameEvent.SWIM.key(), GameEvent.SHEAR.key(), GameEvent.SPLASH.key(),
                GameEvent.ENTITY_DAMAGE.key(), GameEvent.ENTITY_DIE.key(), GameEvent.ENTITY_DISMOUNT.key(),
                GameEvent.ENTITY_MOUNT.key(), GameEvent.ENTITY_ACTION.key());
        ResourceLocation sculkCakeEaten = BuiltInRegistries.GAME_EVENT.getKey(ModGameEvents.SCULK_CAKE_EATEN.get().value());
        getOrCreateTagBuilder(GameEventTags.VIBRATIONS).add(sculkCakeEaten);
        getOrCreateTagBuilder(GameEventTags.WARDEN_CAN_LISTEN).add(sculkCakeEaten);
    }
}