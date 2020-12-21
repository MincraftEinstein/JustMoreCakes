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
import net.minecraft.world.gen.feature.structure.TaigaVillagePools;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import net.minecraftforge.coremod.api.ASMAPI;

@SuppressWarnings("unchecked")
public class RegisterTaigaBakery
{
    public static void updatePools() {
        TaigaVillagePools.init();
        final JigsawPattern taigaPattern = WorldGenRegistries.JIGSAW_POOL.getOrDefault(new ResourceLocation("minecraft:village/taiga/houses"));
        if (taigaPattern == null) {
            return;
        }
        final Function<JigsawPattern.PlacementBehaviour, LegacySingleJigsawPiece> taiga1 = JigsawPiece.func_242851_a("jmc:village/taiga/houses/taiga_bakery_1", ProcessorLists.field_244107_g);
        final JigsawPiece taigaPiece1 = taiga1.apply(JigsawPattern.PlacementBehaviour.RIGID);
        try {
            final String name = ASMAPI.mapField("field_214953_e");
            final Field field = JigsawPattern.class.getDeclaredField(name);
            field.setAccessible(true);
            final String name2 = ASMAPI.mapField("field_214952_d");
            final Field field2 = JigsawPattern.class.getDeclaredField(name2);
            field2.setAccessible(true);
			final List<JigsawPiece> list = (List<JigsawPiece>)field.get(taigaPattern);
            final int n = 1;
            for (int i = 0; i < n; ++i) {
                list.add(taigaPiece1);
            }
            final List<Pair<JigsawPiece, Integer>> list2 = (List<Pair<JigsawPiece, Integer>>)field2.get(taigaPattern);
            list2.add(Pair.of(taigaPiece1, n));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
