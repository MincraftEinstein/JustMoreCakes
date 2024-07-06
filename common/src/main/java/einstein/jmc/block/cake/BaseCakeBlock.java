package einstein.jmc.block.cake;

import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.data.SerializableMobEffectInstance;
import einstein.jmc.data.effects.CakeEffects;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModClientConfigs;
import einstein.jmc.init.ModCommonConfigs;
import einstein.jmc.init.ModTriggerTypes;
import einstein.jmc.platform.Services;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.registration.family.CakeFamily;
import einstein.jmc.util.CakeUtil;
import einstein.jmc.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static einstein.jmc.util.Util.RANDOM;
import static einstein.jmc.util.Util.applyEffectFromInstance;

@SuppressWarnings("deprecation")
public class BaseCakeBlock extends Block implements CakeEffectsHolder {

    public static final IntegerProperty BITES = BlockStateProperties.BITES;
    protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[] {
            Block.box(1, 0, 1, 15, 8, 15),
            Block.box(3, 0, 1, 15, 8, 15),
            Block.box(5, 0, 1, 15, 8, 15),
            Block.box(7, 0, 1, 15, 8, 15),
            Block.box(9, 0, 1, 15, 8, 15),
            Block.box(11, 0, 1, 15, 8, 15),
            Block.box(13, 0, 1, 15, 8, 15)
    };

    private final boolean allowsCandles;
    private final boolean canAlwaysEat;
    private final int slices;
    private CakeVariant variant;
    @Nullable
    private CakeEffects cakeEffects;

    protected BaseCakeBlock(CakeVariant variant, int slices) {
        this(variant.getCakeProperties(), variant.allowsCandles(), variant.canAlwaysEat(), slices);
        this.variant = variant;
    }

    public BaseCakeBlock(CakeVariant variant) {
        this(variant, 6);
    }

    public BaseCakeBlock(Properties properties, boolean allowsCandles, boolean canAlwaysEat, int slices) {
        super(properties);
        this.allowsCandles = allowsCandles;
        this.canAlwaysEat = canAlwaysEat;
        this.slices = slices;
        if (hasBites()) {
            registerDefaultState(stateDefinition.any().setValue(getBites(), 0));
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return getShapeByBite(state)[state.getValue(getBites())];
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        Item item = stack.getItem();
        CakeFamily family = getFamily();

        if (allowsCandles) {
            if (stack.is(ItemTags.CANDLES) && CakeUtil.isUneaten(state, pos, level)) {
                Block block = Block.byItem(item);
                if (block instanceof CandleBlock) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }

                    level.playSound(null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1, 1);
                    BaseCandleCakeBlock candleCake = variant.getCandleCakeByCandle().get(block).get();
                    BlockState newState = candleCake.defaultBlockState();
                    level.setBlockAndUpdate(pos, newState);
                    level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    player.awardStat(Stats.ITEM_USED.get(item));
                    afterCandlePlaced(level, state, pos, candleCake);
                    Block.pushEntitiesUp(state, newState, level, pos);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        if (family != null && isBaseVariant()) {
            if (stack.is(family.getBaseCake().get().asItem())) {
                if (CakeUtil.convertToTwoTiered(family, state, pos, level, player, stack).consumesAction()) {
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public void afterCandlePlaced(Level level, BlockState state, BlockPos pos, BaseCandleCakeBlock candleCake) {
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            if (eat(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return eat(level, pos, state, player);
    }

    public InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(canAlwaysEat)) {
            return InteractionResult.PASS;
        }

        if (Services.PLATFORM.isModLoaded("cakechomps")) {
            ItemStack stack = state.getBlock().getCloneItemStack(level, pos, state);
            player.spawnItemParticles(stack, 16);
            player.playSound(player.getEatingSound(stack), 0.5F + 0.5F * RANDOM.nextInt(2), (RANDOM.nextFloat() - RANDOM.nextFloat()) * 0.2F + 1.0F);
        }

        player.awardStat(Stats.EAT_CAKE_SLICE);
        player.getFoodData().eat(getNutrition(), getSaturationModifier());
        state = eatActions(player, pos, state);

        if (!level.isClientSide()) {
            CakeEffects effects = justMoreCakes$getCakeEffects();
            if (effects != null) {
                applyEffects(player, effects);
            }
        }

        if (player instanceof ServerPlayer serverPlayer) {
            ModTriggerTypes.CAKE_EATEN.get().trigger(serverPlayer, this);
        }

        int bite = state.getValue(getBites());
        level.gameEvent(player, GameEvent.EAT, pos);

        if (bite < getSlices()) {
            level.setBlockAndUpdate(pos, state.setValue(getBites(), bite + 1));
        }
        else {
            level.removeBlock(pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }

        return InteractionResult.SUCCESS;
    }

    public void applyEffects(Player player, CakeEffects effects) {
        for (SerializableMobEffectInstance effectInstance : effects.mobEffects()) {
            applyEffectFromInstance(effectInstance, player);
        }
    }

    public BlockState eatActions(Player player, BlockPos pos, BlockState state) {
        if (CakeUtil.inFamily(state, ModBlocks.FIREY_CAKE_FAMILY)) {
            player.setRemainingFireTicks(ModCommonConfigs.FIREY_CAKE_ON_FIRE_DUR.get());
        }
        else if (CakeUtil.inFamily(state, ModBlocks.ICE_CAKE_FAMILY)) {
            player.clearFire();
        }
        else if (CakeUtil.inFamily(state, ModBlocks.CHORUS_CAKE_FAMILY)) {
            Util.teleportRandomly(player, ModCommonConfigs.CHORUS_CAKE_TELEPORT_RADIUS.get());
        }
        else if (CakeUtil.inFamily(state, ModBlocks.ENDER_CAKE_FAMILY)) {
            Util.teleportRandomly(player, ModCommonConfigs.ENDER_CAKE_TELEPORT_RADIUS.get());
            player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1, 1);
        }
        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor accessor, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(accessor, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, accessor, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return reader.getBlockState(pos.below()).isSolid();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(getBites());
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return CakeUtil.getComparatorOutput(state);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType computationType) {
        return false;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader reader, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(reader, pos, state);
        if (stack.isEmpty() && getFamily() != null) {
            return new ItemStack(getFamily().getBaseCake().get());
        }
        return stack;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (CakeUtil.inFamily(state, ModBlocks.ENDER_CAKE_FAMILY) && ModClientConfigs.ENDER_CAKE_PARTICLES.get()) {
            for (int i = 0; i < 3; ++i) {
                int xSign = random.nextInt(2) * 2 - 1;
                int zSign = random.nextInt(2) * 2 - 1;
                double x = pos.getX() + 0.5D + 0.25D * xSign;
                double y = pos.getY() + random.nextFloat();
                double z = pos.getZ() + 0.5D + 0.25D * zSign;
                double XSpeed = (random.nextFloat() * xSign);
                double YSpeed = (random.nextFloat() - 0.5D) * 0.125D;
                double ZSpeed = (random.nextFloat() * zSign);
                level.addParticle(ParticleTypes.PORTAL, x, y, z, XSpeed, YSpeed, ZSpeed);
            }
        }
        else if (CakeUtil.inFamily(state, ModBlocks.REDSTONE_CAKE_FAMILY) && ModClientConfigs.REDSTONE_CAKE_PARTICLES.get()) {
            for (int i = 0; i < 2; ++i) {
                double x = pos.getX() + random.nextDouble();
                double y = pos.getY() + random.nextDouble() * 0.5D + 0.25D;
                double z = pos.getZ() + random.nextDouble();
                level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0, 0, 0);
            }
        }
        else if (CakeUtil.inFamily(state, ModBlocks.LAVA_CAKE_FAMILY) && ModClientConfigs.LAVA_CAKE_PARTICLES.get()) {
            if (random.nextInt(10) == 0) {
                double x = pos.getX() + random.nextDouble();
                double y = pos.getY() + 1;
                double z = pos.getZ() + random.nextDouble();
                level.addParticle(ParticleTypes.LAVA, x, y, z, 0, 0, 0);
            }
        }
        else if (CakeUtil.inFamily(state, ModBlocks.SCULK_CAKE_FAMILY)) {
            if (SculkSensorBlock.getPhase(state) == SculkSensorPhase.ACTIVE) {
                Direction direction = Direction.getRandom(random);
                if (direction != Direction.UP && direction != Direction.DOWN) {
                    double x = pos.getX() + 0.5 + (direction.getStepX() == 0 ? 0.5 - random.nextDouble() : direction.getStepX() * 0.6);
                    double y = pos.getY() + 0.25;
                    double z = pos.getZ() + 0.5 + (direction.getStepZ() == 0 ? 0.5 - random.nextDouble() : direction.getStepZ() * 0.6);
                    double ySpeed = random.nextFloat() * 0.04;
                    level.addParticle(DustColorTransitionOptions.SCULK_TO_REDSTONE, x, y, z, 0, ySpeed, 0);
                }
            }
        }
    }

    public boolean hasBites() {
        return getBites() != null && getSlices() > 0;
    }

    @Nullable
    public IntegerProperty getBites() {
        return BITES;
    }

    public VoxelShape[] getShapeByBite(BlockState state) {
        return SHAPE_BY_BITE;
    }

    public int getSlices() {
        return slices;
    }

    public CakeVariant getVariant() {
        return variant;
    }

    @Nullable
    public CakeFamily getFamily() {
        return variant.getFamily();
    }

    public boolean isBaseVariant() {
        return variant.getType() == CakeVariant.Type.BASE;
    }

    @Nullable
    @Override
    public CakeEffects justMoreCakes$getCakeEffects() {
        if (cakeEffects != null) {
            return cakeEffects;
        }

        if (getFamily() != null) {
            return getFamily().justMoreCakes$getCakeEffects();
        }

        return null;
    }

    @Override
    public void justMoreCakes$setCakeEffects(@Nullable CakeEffects cakeEffects) {
        this.cakeEffects = cakeEffects;
    }

    public int getNutrition() {
        if (variant != null) {
            return variant.getNutrition();
        }
        return CakeUtil.DEFAULT_NUTRITION;
    }

    public float getSaturationModifier() {
        if (variant != null) {
            return variant.getSaturationModifier();
        }
        return CakeUtil.DEFAULT_SATURATION_MODIFIER;
    }
}