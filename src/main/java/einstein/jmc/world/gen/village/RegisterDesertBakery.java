package einstein.jmc.world.gen.village;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

import com.mojang.datafixers.util.Pair;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.LegacySingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.DesertVillagePools;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import net.minecraftforge.coremod.api.ASMAPI;

@SuppressWarnings("unchecked")
public class RegisterDesertBakery
{
    public static void updatePools() {
        DesertVillagePools.init();
        final JigsawPattern desertPattern = WorldGenRegistries.JIGSAW_POOL.getOrDefault(new ResourceLocation("minecraft:village/desert/houses"));
        if (desertPattern == null) {
            return;
        }
        final Function<JigsawPattern.PlacementBehaviour, LegacySingleJigsawPiece> desert1 = JigsawPiece.func_242851_a("jmc:village/desert/houses/desert_bakery_1", ProcessorLists.field_244107_g);
        final JigsawPiece desertPiece1 = desert1.apply(JigsawPattern.PlacementBehaviour.RIGID);
        try {
            final String name = ASMAPI.mapField("field_214953_e");
            final Field field = JigsawPattern.class.getDeclaredField(name);
            field.setAccessible(true);
            final String name2 = ASMAPI.mapField("field_214952_d");
            final Field field2 = JigsawPattern.class.getDeclaredField(name2);
            field2.setAccessible(true);
			final List<JigsawPiece> list = (List<JigsawPiece>)field.get(desertPattern);
            final int n = 1;
            for (int i = 0; i < n; ++i) {
                list.add(desertPiece1);
            }
            final List<Pair<JigsawPiece, Integer>> list2 = (List<Pair<JigsawPiece, Integer>>)field2.get(desertPattern);
            list2.add(Pair.of(desertPiece1, n));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
