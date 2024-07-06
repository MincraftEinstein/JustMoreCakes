package einstein.jmc.compat.rei;

import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.forge.REIPluginCommon;

// DO NOT DELETE!! Exists as a way to add REI plugin annotations to the actual plugins from the common project
@SuppressWarnings("unused")
public class ModREIPluginNeoForge {

    @REIPluginCommon
    public static class ModREICommonPluginNeoForge extends ModREICommonPlugin {
    }

    @REIPluginClient
    public static class ModREIClientPluginNeoForge extends ModREIClientPlugin {
    }
}
