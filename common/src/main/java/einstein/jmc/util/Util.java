package einstein.jmc.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import einstein.jmc.block.cake.BaseThreeTieredCakeBlock;
import einstein.jmc.init.ModItems;
import einstein.jmc.mixin.RecipeManagerAccessor;
import einstein.jmc.mixin.StructureTemplatePoolAccessor;
import einstein.jmc.platform.Services;
import einstein.jmc.platform.services.IPlatformHelper;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static einstein.jmc.JustMoreCakes.*;

public class Util {

    public static final Gson GSON = new GsonBuilder().create();
    public static final Random RANDOM = new Random();
    public static final Supplier<LootItemCondition.Builder> HAS_CAKE_SPATULA = () -> MatchTool.toolMatches(ItemPredicate.Builder.item().of(ModItems.CAKE_SPATULA.get()));

    public static <T extends Item> ResourceLocation getItemId(T item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static <T extends Block> ResourceLocation getBlockId(T block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public static void createExplosion(final Level level, final BlockPos pos, final float size) {
        if (level.isClientSide) {
            return;
        }
        level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, size, Level.ExplosionInteraction.TNT);
    }

    public static boolean teleportRandomly(LivingEntity entity, double radius, boolean spawnParticles) {
        boolean sucess = false;
        int maxAttempts = 20;
        int attempts = 0;
        double x;
        double y;
        double z;

        for (; !sucess && attempts++ <= maxAttempts; sucess = entity.randomTeleport(x, y, z, spawnParticles)) {
            x = entity.xo + (RANDOM.nextDouble() - RANDOM.nextDouble()) * radius;
            y = entity.yo + (RANDOM.nextDouble() - RANDOM.nextDouble()) * radius;
            z = entity.zo + (RANDOM.nextDouble() - RANDOM.nextDouble()) * radius;
        }
        return sucess;
    }

    public static Block getBlock(ResourceLocation location) {
        Block block = BuiltInRegistries.BLOCK.get(location);
        if (block != Blocks.AIR) {
            return block;
        }
        else {
            throw new NullPointerException("Could not find block: " + location);
        }
    }

    public static boolean timeGoneBy(Level level, int ticks) {
        if (ticks == 0) {
            return true;
        }
        return level.getGameTime() % (ticks) == 0;
    }

    public static void registerVillageBuilding(MinecraftServer server, String biome, String name, int weight) {
        String path = MOD_ID + ":village/" + biome + "/houses/" + biome + "_" + name;

        if (weight < 1) {
            return;
        }

        if (weight > 150) {
            LOGGER.error("Failed to register village building: {} - weight must be less than 150", path);
            return;
        }

        RegistryAccess.Frozen access = server.registryAccess();
        Registry<StructureTemplatePool> templatePoolRegistry = access.registry(Registries.TEMPLATE_POOL).orElseThrow();
        Registry<StructureProcessorList> processorRegistry = access.registry(Registries.PROCESSOR_LIST).orElseThrow();

        StructureTemplatePool templatePool = templatePoolRegistry.get(mcLoc("village/" + biome + "/houses"));

        if (templatePool == null) {
            LOGGER.warn("Failed to register village building: {}  - template pool is null", path);
            return;
        }

        StructurePoolElement structure = StructurePoolElement.legacy(path, processorRegistry.getHolderOrThrow(ProcessorLists.MOSSIFY_10_PERCENT)).apply(StructureTemplatePool.Projection.RIGID);
        StructureTemplatePoolAccessor templateAccessor = ((StructureTemplatePoolAccessor) templatePool);

        for (int i = 0; i < weight; i++) {
            templateAccessor.getTemplates().add(structure);
        }

        List<Pair<StructurePoolElement, Integer>> rawTemplates = new ArrayList<>(templateAccessor.getRawTemplates());
        rawTemplates.add(Pair.of(structure, weight));
        templateAccessor.setRawTemplates(rawTemplates);
    }

    public static <K, V> Map<K, V> createKeySortedMap(Map<K, V> map, Comparator<K> comparator) {
        return map.entrySet().stream().sorted(Map.Entry.comparingByKey(comparator))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static <K, V> Map<K, V> createValueSortedMap(Map<K, V> map, Comparator<V> comparator) {
        return map.entrySet().stream().sorted(Map.Entry.comparingByValue(comparator))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static LootTable.Builder addDropWhenCakeSpatulaPool(LootTable.Builder builder, Block dropped) {
        return addDropWhenCakeSpatulaPool(builder, dropped, 1);
    }

    public static LootTable.Builder addDropWhenCakeSpatulaPool(LootTable.Builder builder, Block dropped, int count) {
        return addDropWhenCakeSpatulaPool(builder, null, dropped, count, false);
    }

    public static LootTable.Builder addDropWhenCakeSpatulaPool(LootTable.Builder builder, @Nullable Block block, Block dropped, int count, boolean addHalfCondition) {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(count))
                .add(LootItem.lootTableItem(dropped).when(HAS_CAKE_SPATULA.get()));

        if (addHalfCondition) {
            pool = addHalfConditionToPool(pool, block);
        }

        return builder.withPool(pool);
    }

    public static LootPool.Builder addHalfConditionToPool(LootPool.Builder builder, Block block) {
        return builder.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                        .hasProperty(BaseThreeTieredCakeBlock.HALF, DoubleBlockHalf.UPPER)));
    }

    public static void removeRecipe(RecipeManager recipeManager, ResourceLocation id) {
        RecipeManagerAccessor manager = (RecipeManagerAccessor) recipeManager;
        Map<ResourceLocation, RecipeHolder<?>> recipes = new HashMap<>(manager.getRecipes());

        for (ResourceLocation recipeId : recipes.keySet()) {
            if (recipeId.equals(id)) {
                recipes.remove(recipeId);
                break;
            }
        }

        manager.setRecipes(recipes);
    }

    public static void applyEffect(MobEffectInstance instance, LivingEntity entity) {
        MobEffect effect = instance.getEffect().value();
        if (effect.isInstantenous()) {
            effect.applyInstantenousEffect(entity, entity, entity, instance.getAmplifier(), 1);
            return;
        }
        entity.addEffect(instance);
    }

    public static void bounceUp(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (vec3.y < 0) {
            double d0 = entity instanceof LivingEntity ? 0.5 : 0.3D;
            entity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
        }
    }

    public static void serializeResult(JsonObject json, Item item, int count) {
        JsonObject object = new JsonObject();
        object.addProperty("item", BuiltInRegistries.ITEM.getKey(item).toString());

        if (count > 1) {
            object.addProperty("count", count);
        }

        json.add("result", object);
    }

    public static RegistryAccess getRegistryAccess() {
        MinecraftServer server = Services.HOOKS.getCurrentServer();
        if (server != null) {
            return server.registryAccess();
        }

        if (Services.PLATFORM.getPhysicalSide() == IPlatformHelper.PhysicalSide.CLIENT) {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                return level.registryAccess();
            }
            throw new UnsupportedOperationException("Failed to get registry access. Level was null");
        }
        throw new UnsupportedOperationException("Failed to get registry access. Server was null");
    }

    public static <T extends Block> Codec<T> blockOfTypeCodec(Class<T> clazz) {
        return BuiltInRegistries.BLOCK.byNameCodec().flatXmap(
                block -> checkInstanceOf(block, clazz, "block"),
                t -> checkInstanceOf(t, clazz, "block")
        );
    }

    @SuppressWarnings("unchecked")
    public static <T, V extends T> DataResult<V> checkInstanceOf(T object, Class<V> clazz, String msg) {
        if (clazz.isInstance(object)) {
            return (DataResult<V>) DataResult.success(object);
        }
        return DataResult.error(() -> "Expected " + msg + " to be of " + clazz + " was " + object.getClass());
    }
}
