package einstein.jmc;

import einstein.jmc.init.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class JMCItemGroup extends ItemGroup
{
    public JMCItemGroup(final int index, final String label) {
        super(index, label);
    }
    
    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.CHOCOLATE_CAKE);
    }
}