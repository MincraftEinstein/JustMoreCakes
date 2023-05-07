package einstein.jmc.compat.rei;

import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.forge.REIPluginCommon;

// DO NOT DELETE!! Exists as a way to add REI plugin annotations to the actual plugins from the common project
@SuppressWarnings("unused")
public class REIPluginLoaderForge {

    @REIPluginCommon
    public static class REIPluginForge extends REIPlugin {

    }

    @REIPluginClient
    public static class REIClientPluginForge extends REIClientPlugin {

    }
}
