package einstein.jmc.util;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.data.CakeEffects;
import einstein.jmc.init.ModPotions;
import einstein.jmc.mixin.StructureTemplatePoolAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.phys.Vec3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.*;

public class Util {

    public static final Gson GSON = new GsonBuilder().create();
    private static final Random RANDOM = new Random();

    public static <T extends Item> ResourceLocation getItemId(T item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static <T extends Block> ResourceLocation getBlockId(T block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public static <T extends MobEffect> ResourceLocation getMobEffectId(T effect) {
        return BuiltInRegistries.MOB_EFFECT.getKey(effect);
    }

    public static void createExplosion(final Level level, final BlockPos pos, final float size) {
        if (level.isClientSide) {
            return;
        }
        level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, size, Level.ExplosionInteraction.TNT);
    }

    public static boolean teleportRandomly(final LivingEntity entity, final double radius) {
        int attempts;
        int tries;
        boolean teleported;
        double x;
        double y;
        double z;
        for (attempts = 20, tries = 0, teleported = false; !teleported && tries++ <= attempts; teleported = entity.randomTeleport(x, y, z, true)) {
            x = entity.xo + (RANDOM.nextDouble() - RANDOM.nextDouble()) * radius;
            y = entity.yo + (RANDOM.nextDouble() - RANDOM.nextDouble()) * radius;
            z = entity.zo + (RANDOM.nextDouble() - RANDOM.nextDouble()) * radius;
        }
        return teleported;
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

    /**
     * Copied from com.illusivesoulworks.cakechomps.CakeChompsMod
     * and slightly changed to work with JustMoreCakes
     */
    public static void useCake(Player player, BlockPos pos, InteractionHand hand, Supplier<Boolean> canInteract) {
        Level level = player.getLevel();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (!player.canEat(false)) {
            return;
        }

        if (!(block instanceof BaseCandleCakeBlock) && !(block instanceof BaseCakeBlock)) {
            return;
        }

        if (block instanceof BaseCakeBlock cake) {
            if (player.getItemInHand(hand).is(ItemTags.CANDLES) && state.getOptionalValue(cake.getBites()).map(val -> val == 0).orElse(false)) {
                return;
            }

            if (cake.getBiteCount() <= 0) {
                return;
            }
        }

        if (block instanceof BaseCandleCakeBlock candleCake && candleCake.getOriginalCake().getBiteCount() <= 0) {
            return;
        }

        if (canInteract.get()) {
            ItemStack blockStack = block.getCloneItemStack(level, pos, state);

            for (int i = 0; i < 16; ++i) {
                Vec3 vec3 = new Vec3(((double) RANDOM.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

                vec3 = vec3.xRot(-player.getXRot() * ((float) Math.PI / 180F));
                vec3 = vec3.yRot(-player.getYRot() * ((float) Math.PI / 180F));

                double d0 = (double) (-RANDOM.nextFloat()) * 0.6D - 0.3D;
                Vec3 vec31 = new Vec3(((double) RANDOM.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);

                vec31 = vec31.xRot(-player.getXRot() * ((float) Math.PI / 180F));
                vec31 = vec31.yRot(-player.getYRot() * ((float) Math.PI / 180F));
                vec31 = vec31.add(player.getX(), player.getEyeY(), player.getZ());

                ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, blockStack);

                if (player.level instanceof ServerLevel serverWorld) {
                    serverWorld.sendParticles(particle, vec31.x, vec31.y, vec31.z, 1, vec3.x, vec3.y + 0.05D, vec3.z, 0.0D);
                }
                else {
                    level.addParticle(particle, vec31.x, vec31.y, vec31.z, vec3.x, vec3.y + 0.05D, vec3.z);
                }
            }
            player.playSound(player.getEatingSound(blockStack), 0.5F + 0.5F * (float) RANDOM.nextInt(2), (RANDOM.nextFloat() - RANDOM.nextFloat()) * 0.2F + 1.0F);
        }
    }

    public static void registerVillageBuilding(MinecraftServer server, String biome, String name, int weight) {
        String path = MOD_ID + ":village/" + biome + "/houses/" + biome + "_" + name;

        if (weight < 1) {
            return;
        }

        if (weight > 150) {
            LOGGER.error("Failed to register village building: " + path + " - weight must be less than 150");
            return;
        }

        RegistryAccess.Frozen access = server.registryAccess();
        Registry<StructureTemplatePool> templatePoolRegistry = access.registry(Registries.TEMPLATE_POOL).orElseThrow();
        Registry<StructureProcessorList> processorRegistry = access.registry(Registries.PROCESSOR_LIST).orElseThrow();

        StructureTemplatePool templatePool = templatePoolRegistry.get(mcLoc("village/" + biome + "/houses"));

        if (templatePool == null) {
            LOGGER.warn("Failed to register village building: " + path + "  - template pool is null");
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

    public static Map<ResourceLocation, CakeEffects> deserializeCakeEffects(ResourceManager manager) {
        ImmutableMap.Builder<ResourceLocation, CakeEffects> builder = ImmutableMap.builder();
        var locations = manager.listResources("cake_effects", location -> location.getPath().endsWith(".json"));

        locations.forEach((location, resource) -> {
            try (InputStream stream = resource.open();
                 Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                JsonObject object = GsonHelper.fromJson(GSON, reader, JsonObject.class);

                CakeEffects.CODEC.parse(JsonOps.INSTANCE, object)
                        .resultOrPartial(error -> LOGGER.error("Failed to decode cake effect with json id {} - Error: {}", location, error))
                        .ifPresent(entry -> builder.put(location, entry));
            }
            catch (Exception exception) {
                LOGGER.error("Error occurred while loading resource json " + location.toString(), exception);
            }
        });

        Map<ResourceLocation, CakeEffects> map = builder.build();
        LOGGER.info("Loaded {} cake effects", map.size());
        return map;
    }

    public static void livingEntityJump(Entity entity) {
        if (entity instanceof Player player) {
            if (player.hasEffect(ModPotions.BOUNCING_EFFECT.get())) {
                player.push(0, 0.15F, 0);
            }
        }
    }

    public static void livingEntityTick(Level level, LivingEntity entity) {
        RandomSource random = entity.getRandom();
        if (entity.hasEffect(ModPotions.BOUNCING_EFFECT.get())) {
            if (entity.verticalCollision && entity.isOnGround() && !entity.isSleeping()) {
                float f = 0.65F;

                if (entity.hasEffect(MobEffects.JUMP)) {
                    f += 0.1F * (entity.getEffect(MobEffects.JUMP).getAmplifier() + 1);
                }

                entity.push(0, f, 0);
                entity.playSound(SoundEvents.SLIME_SQUISH, 1, 1);

                if (level.isClientSide) {
                    for (int i = 0; i < 8; ++i) {
                        float f1 = random.nextFloat() * ((float) Math.PI * 2F);
                        float f2 = random.nextFloat() * 0.5F + 0.5F;
                        float f3 = Mth.sin(f1) * 1 * 0.5F * f2;
                        float f4 = Mth.cos(f1) * 1 * 0.5F * f2;
                        level.addParticle(ParticleTypes.ITEM_SLIME, entity.getX() + f3, entity.getY(), entity.getZ() + f4, 0, 0, 0);
                    }
                }
            }
        }
    }

    public static Predicate<ItemEntity> pandaItems() {
        return itemEntity -> {
            ItemStack stack = itemEntity.getItem();
            return (stack.is(Blocks.BAMBOO.asItem()) || stack.is(Blocks.CAKE.asItem()) || isCake().test(stack)) && itemEntity.isAlive() && !itemEntity.hasPickUpDelay();
        };
    }

    public static Predicate<ItemStack> isCake() {
        return stack -> {
            for (Supplier<BaseCakeBlock> cake : CakeBuilder.BUILDER_BY_CAKE.keySet()) {
                if (stack.is(cake.get().asItem())) {
                    return true;
                }
            }
            return false;
        };
    }
}
