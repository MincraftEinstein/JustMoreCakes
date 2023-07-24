package einstein.jmc.data.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModGameEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.tags.GameEventTags;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModGameEventTagsProvider extends GameEventTagsProvider {

    public ModGameEventTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, JustMoreCakes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_255981_) {
        tag(ModGameEventTags.SNEAKING_EFFECT_BLOCKS).add(GameEvent.DRINK,
                GameEvent.EQUIP, GameEvent.EAT, GameEvent.ELYTRA_GLIDE, GameEvent.HIT_GROUND,
                GameEvent.TELEPORT, GameEvent.ITEM_INTERACT_START, GameEvent.ITEM_INTERACT_FINISH,
                GameEvent.PROJECTILE_SHOOT, GameEvent.STEP, GameEvent.SWIM, GameEvent.SHEAR, GameEvent.SPLASH,
                GameEvent.ENTITY_DAMAGE, GameEvent.ENTITY_DIE, GameEvent.ENTITY_DISMOUNT,
                GameEvent.ENTITY_MOUNT, GameEvent.ENTITY_SHAKE);
        tag(GameEventTags.VIBRATIONS).add(ModGameEvents.SCULK_CAKE_EATEN.get());
        tag(GameEventTags.WARDEN_CAN_LISTEN).add(ModGameEvents.SCULK_CAKE_EATEN.get());
    }
}
