package einstein.jmc.util;

import einstein.jmc.block.cake.BaseCakeBlock;

import java.util.function.Supplier;

public class DefaultCakeFamily extends CakeFamily {

    private final String flavorName;

    DefaultCakeFamily(String flavorName) {
        super(flavorName + "_cake");
        this.flavorName = flavorName;
    }

    public String getFlavorName() {
        return flavorName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Supplier<BaseCakeBlock> getBaseCake() {
        return (Supplier<BaseCakeBlock>) super.getBaseCake();
    }
}
