package einstein.jmc;

import einstein.jmc.compat.AmendmentsCompat;
import einstein.jmc.compat.FarmersDelightCompat;
import einstein.jmc.data.BowlContents;
import einstein.jmc.data.effects.CakeEffectsManager;
import einstein.jmc.init.*;
import einstein.jmc.mixin.AdvancementAccessor;
import einstein.jmc.mixin.AdvancementRequirementsAccessor;
import einstein.jmc.platform.Services;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.registration.family.CakeFamily;
import einstein.jmc.util.Util;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
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
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static einstein.jmc.init.ModCommonConfigs.CAKE_BAKERY_GENERATION_WEIGHT;
import static einstein.jmc.util.Util.registerVillageBuilding;

public class JustMoreCakes {

    public static final String MOD_ID = "jmc";
    public static final String MOD_NAME = "Just More Cakes!";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final ResourceKey<LootTable> CAKE_BAKER_GIFT = ResourceKey.create(Registries.LOOT_TABLE, loc("gameplay/hero_of_the_village/cake_baker_gift"));
    public static final ResourceKey<DamageType> OBSIDIAN_CAKE_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, loc("obsidian_cake"));

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
        ModLootConditionTypes.init();
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
        if (playerUpdate) {
            CakeEffectsManager.syncToPlayer(player);
            return;
        }

        CakeEffectsManager.loadCakeEffects();
        BowlContents.EMPTY.clear();

        if (ModCommonConfigs.DISABLE_DEFAULT_CAKE_RECIPE.get()) {
            RecipeManager recipeManager = server.getRecipeManager();
            Util.removeRecipe(recipeManager, mcLoc("cake"), RecipeType.CRAFTING);

            if (FarmersDelightCompat.IS_ENABLED.get()) {
                Util.removeRecipe(recipeManager, FarmersDelightCompat.fdLoc("cake_from_milk_bottle"), RecipeType.CRAFTING);
            }
        }

        if (ModCommonConfigs.MODIFY_BIRTHDAY_SONG.get()) {
            modifyBirthdaySongAdvancement(server);
        }
    }

    private static void modifyBirthdaySongAdvancement(MinecraftServer server) {
        AdvancementHolder holder = server.getAdvancements().get(mcLoc("husbandry/allay_deliver_cake_to_note_block"));
        if (holder != null) {
            Advancement advancement = holder.value();
            Map<String, Criterion<?>> criteria = new HashMap<>(advancement.criteria());
            AdvancementRequirements requirements = advancement.requirements();
            List<List<String>> requirementsList = new ArrayList<>(requirements.requirements());
            List<String> requirement = new ArrayList<>(requirementsList.getFirst());
            LocationPredicate.Builder locationPredicate = LocationPredicate.Builder.location().setBlock(
                    BlockPredicate.Builder.block().of(Blocks.NOTE_BLOCK));

            CakeFamily.REGISTERED_CAKE_FAMILIES.forEach((location, family) -> {
                String criteria_id = location.toString();
                criteria.put(criteria_id, new Criterion<>(CriteriaTriggers.ALLAY_DROP_ITEM_ON_BLOCK,
                        ItemUsedOnLocationTrigger.TriggerInstance.allayDropItemOnBlock(locationPredicate,
                                ItemPredicate.Builder.item().of(family.getBaseItem().get())
                        ).triggerInstance()
                ));
                requirement.add(criteria_id);
            });

            requirementsList.set(0, requirement);
            ((AdvancementRequirementsAccessor) (Object) requirements).setRequirements(requirementsList);
            ((AdvancementAccessor) (Object) advancement).setCriteria(criteria);
        }
    }

    public static InteractionResult blockClicked(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        boolean canUse = !player.isSecondaryUseActive() || (player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty());

        if (stack.is(Blocks.CAKE.asItem()) && canUse) {
            if (AmendmentsCompat.IS_ENABLED.get()) {
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