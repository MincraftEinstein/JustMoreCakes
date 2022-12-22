package einstein.jmc.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

@FunctionalInterface
public interface MenuTypeSupplier<T> {

    T create(int id, Inventory inventory, FriendlyByteBuf buf);
}
