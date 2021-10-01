package einstein.jmc;

import einstein.jmc.init.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class JMCTab extends CreativeModeTab
{
    public JMCTab(final int index, final String label) {
        super(index, label);
    }
    
    @Override
    public ItemStack makeIcon() {
    	return new ItemStack(ModBlocks.getBlock(ModBlocks.RL("chocolate_cake")));
    }
}