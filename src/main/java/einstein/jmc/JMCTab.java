package einstein.jmc;

import einstein.jmc.init.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class JMCTab extends CreativeModeTab {

    public JMCTab(final int index, final String name) {
        super(index, name);
    }
    
    @Override
    public ItemStack makeIcon() {
    	return new ItemStack(ModBlocks.CHOCOLATE_CAKE.get());
    }
}