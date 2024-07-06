package einstein.jmc.data.packs.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.data.packs.ModGameEventTags;
import einstein.jmc.init.ModGameEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.GameEventTags;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModGameEventTagsProvider extends GameEventTagsProvider {

    public ModGameEventTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, JustMoreCakes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModGameEventTags.STEALTH_EFFECT_BLOCKS).add(GameEvent.DRINK.key(),
                GameEvent.EQUIP.key(), GameEvent.EAT.key(), GameEvent.ELYTRA_GLIDE.key(), GameEvent.HIT_GROUND.key(),
                GameEvent.TELEPORT.key(), GameEvent.ITEM_INTERACT_START.key(), GameEvent.ITEM_INTERACT_FINISH.key(),
                GameEvent.PROJECTILE_SHOOT.key(), GameEvent.STEP.key(), GameEvent.SWIM.key(), GameEvent.SHEAR.key(), GameEvent.SPLASH.key(),
                GameEvent.ENTITY_DAMAGE.key(), GameEvent.ENTITY_DIE.key(), GameEvent.ENTITY_DISMOUNT.key(),
                GameEvent.ENTITY_MOUNT.key(), GameEvent.ENTITY_ACTION.key());
        ResourceKey<GameEvent> sculkCakeEaten = BuiltInRegistries.GAME_EVENT.getResourceKey(ModGameEvents.SCULK_CAKE_EATEN.get().value()).orElseThrow();
        tag(GameEventTags.VIBRATIONS).add(sculkCakeEaten);
        tag(GameEventTags.WARDEN_CAN_LISTEN).add(sculkCakeEaten);
    }
}
