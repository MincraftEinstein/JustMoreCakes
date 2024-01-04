package einstein.jmc.util;

import net.minecraft.resources.ResourceLocation;

public interface CakeModel {

    CakeModel DEFAULT = new CakeModel() {};
    CakeModel FROM_VANILLA = new CakeModel() {};
    CakeModel CUSTOM = new CakeModel() {};

    record CrossCakeModel(ResourceLocation crossTexture) implements CakeModel {}
}
