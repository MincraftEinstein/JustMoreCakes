package einstein.jmc.mixin;

import einstein.jmc.JustMoreCakes;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    public abstract List<ServerPlayer> getPlayers();

    @Inject(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;<init>(Ljava/util/Collection;)V", shift = At.Shift.BEFORE))
    private void placeNewPlayer(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        JustMoreCakes.onDatapackSync(player, server, true);
    }

    @Inject(method = "reloadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/common/ClientboundUpdateTagsPacket;<init>(Ljava/util/Map;)V"))
    private void reloadResources(CallbackInfo ci) {
        JustMoreCakes.onDatapackSync(null, server, false);

        for (ServerPlayer player : getPlayers()) {
            JustMoreCakes.onDatapackSync(player, server, true);
        }
    }
}
