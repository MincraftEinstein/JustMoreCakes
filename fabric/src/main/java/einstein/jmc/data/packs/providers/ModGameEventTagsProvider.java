package einstein.jmc.data.packs.providers;

import einstein.jmc.data.packs.ModGameEventTags;
import einstein.jmc.init.ModGameEvents;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.GameEventTags;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.concurrent.CompletableFuture;

public class ModGameEventTagsProvider extends FabricTagProvider.GameEventTagProvider {

    public ModGameEventTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(ModGameEventTags.STEALTH_EFFECT_BLOCKS).add(GameEvent.DRINK,
                GameEvent.EQUIP, GameEvent.EAT, GameEvent.ELYTRA_GLIDE, GameEvent.HIT_GROUND,
                GameEvent.TELEPORT, GameEvent.ITEM_INTERACT_START, GameEvent.ITEM_INTERACT_FINISH,
                GameEvent.PROJECTILE_SHOOT, GameEvent.STEP, GameEvent.SWIM, GameEvent.SHEAR, GameEvent.SPLASH,
                GameEvent.ENTITY_DAMAGE, GameEvent.ENTITY_DIE, GameEvent.ENTITY_DISMOUNT,
                GameEvent.ENTITY_MOUNT, GameEvent.ENTITY_ACTION);
        getOrCreateTagBuilder(GameEventTags.VIBRATIONS).add(ModGameEvents.SCULK_CAKE_EATEN.get());
        getOrCreateTagBuilder(GameEventTags.WARDEN_CAN_LISTEN).add(ModGameEvents.SCULK_CAKE_EATEN.get());
    }
}