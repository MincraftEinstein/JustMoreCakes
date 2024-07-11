package einstein.jmc;

import einstein.jmc.compat.AmendmentsCompat;
import einstein.jmc.data.BowlContents;
import einstein.jmc.data.effects.CakeEffectsManager;
import einstein.jmc.init.*;
import einstein.jmc.platform.Services;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static einstein.jmc.init.ModCommonConfigs.CAKE_BAKERY_GENERATION_WEIGHT;
import static einstein.jmc.util.Util.registerVillageBuilding;

public class JustMoreCakes {

    public static final String MOD_ID = "jmc";
    public static final String MOD_NAME = "Just More Cakes!";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final ResourceKey<LootTable> CAKE_BAKER_GIFT = ResourceKey.create(Registries.LOOT_TABLE, loc("gameplay/hero_of_the_village/cake_baker_gift"));

    public static void init() {
        ModItems.init();
        ModBlocks.init();
        ModBlockEntityTypes.init();
        ModMenuTypes.init();
        ModRecipes.init();
        ModVillagers.init();
        ModPotions.init();
        ModCreativeModeTabs.init();
        ModGameEvents.init();
        ModPackets.init();
        ModTriggerTypes.init();
    }

    public static void commonSetup() {
        for (CakeVariant variant : CakeVariant.VARIANT_BY_CAKE.values()) {
            if (variant.hasItem()) {
                Services.HOOKS.registerCompostable(variant.getItem().get(), 1);
            }
        }

        Services.HOOKS.registerCompostable(ModBlocks.CUPCAKE_VARIANT.getCake().get(), 0.25F);
        Services.HOOKS.registerCompostable(ModItems.CREAM_CHEESE.get(), 0.65F);
    }

    public static void onServerStarting(MinecraftServer server) {
        registerVillageBuilding(server, "plains", "bakery_1", CAKE_BAKERY_GENERATION_WEIGHT.get());
        registerVillageBuilding(server, "plains", "bakery_2", CAKE_BAKERY_GENERATION_WEIGHT.get());
        registerVillageBuilding(server, "desert", "bakery_1", CAKE_BAKERY_GENERATION_WEIGHT.get());
        registerVillageBuilding(server, "savanna", "bakery_1", CAKE_BAKERY_GENERATION_WEIGHT.get());
        registerVillageBuilding(server, "snowy", "bakery_1", CAKE_BAKERY_GENERATION_WEIGHT.get());
        registerVillageBuilding(server, "snowy", "bakery_2", CAKE_BAKERY_GENERATION_WEIGHT.get());
        registerVillageBuilding(server, "taiga", "bakery_1", CAKE_BAKERY_GENERATION_WEIGHT.get());

        onDatapackSync(null, server, false);
    }

    public static void onDatapackSync(@Nullable ServerPlayer player, MinecraftServer server, boolean playerUpdate) {
        if (ModCommonConfigs.DISABLE_DEFAULT_CAKE_RECIPE.get()) {
            Util.removeRecipe(server.getRecipeManager(), mcLoc("cake"));
        }

        if (playerUpdate) {
            CakeEffectsManager.syncToPlayer(player);
            return;
        }

        CakeEffectsManager.loadCakeEffects();
        BowlContents.EMPTY.clear();
    }

    public static InteractionResult blockClicked(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        boolean canUse = !player.isSecondaryUseActive() || (player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty());

        if (stack.is(Blocks.CAKE.asItem()) && canUse) {
            if (Services.PLATFORM.isModLoaded("amendments")) {
                return AmendmentsCompat.cakeUsedOnBlock(player, level, hand, hitResult);
            }
        }
        return InteractionResult.PASS;
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
            if (entity.verticalCollision && entity.onGround() && !entity.isSleeping()) {
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

    public static ResourceLocation loc(String string) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, string);
    }

    public static ResourceLocation mcLoc(String string) {
        return ResourceLocation.withDefaultNamespace(string);
    }
}