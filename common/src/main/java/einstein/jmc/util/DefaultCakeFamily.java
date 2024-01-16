package einstein.jmc.util;

import einstein.jmc.block.cake.BaseCakeBlock;

import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.loc;

public class DefaultCakeFamily extends CakeFamily {

    DefaultCakeFamily(String flavorName) {
        super(loc(flavorName), flavorName + "_cake");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Supplier<BaseCakeBlock> getBaseCake() {
        return (Supplier<BaseCakeBlock>) super.getBaseCake();
    }
}
