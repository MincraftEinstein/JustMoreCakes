package einstein.jmc.block.cake;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.block.cake.effects.CakeEffects;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModCommonConfigs;
import einstein.jmc.platform.Services;
import einstein.jmc.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static einstein.jmc.util.Util.RANDOM;
import static einstein.jmc.util.Util.applyEffectFromHolder;

@SuppressWarnings("deprecation")
public class BaseCakeBlock extends Block implements CakeEffectsHolder {

    public static final int DEFAULT_NUTRITION = 2;
    public static final float DEFAULT_SATURATION_MODIFIER = 0.1F;
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
    private CakeBuilder builder;
    private CakeEffects cakeEffects;

    protected BaseCakeBlock(CakeBuilder builder, int slices) {
        this(builder.getCakeProperties(), builder.allowsCandles(), builder.canAlwaysEat(), slices);
        this.builder = builder;
    }

    public BaseCakeBlock(CakeBuilder builder) {
        this(builder, 6);
    }

    public BaseCakeBlock(Properties properties, boolean allowsCandles, boolean canAlwaysEat, int slices) {
        super(properties);
        this.allowsCandles = allowsCandles;
        this.canAlwaysEat = canAlwaysEat;
        this.slices = slices;
        if (getSlices() > 0) {
            registerDefaultState(stateDefinition.any().setValue(getBites(), 0));
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return getShapeByBite(state)[state.getValue(getBites())];
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        CakeFamily family = getFamily();

        if (allowsCandles) {
            if (stack.is(ItemTags.CANDLES) && isUneaten(state, pos, level)) {
                Block block = Block.byItem(item);
                if (block instanceof CandleBlock) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }

                    level.playSound(null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1, 1);
                    BaseCandleCakeBlock candleCake = builder.getCandleCakeByCandle().get(block).get();
                    BlockState newState = candleCake.defaultBlockState();
                    level.setBlockAndUpdate(pos, newState);
                    level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    player.awardStat(Stats.ITEM_USED.get(item));
                    afterCandlePlaced(level, state, pos, candleCake);
                    Block.pushEntitiesUp(state, newState, level, pos);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if (family != null && isBaseCake()) {
            if (stack.is(family.getBaseCake().get().asItem()) && isUneaten(state, pos, level)) {
                BlockState newState = family.getTwoTieredCake().get().defaultBlockState();

                level.setBlockAndUpdate(pos, newState);
                Block.pushEntitiesUp(state, newState, level, pos);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1, 1);
                player.awardStat(Stats.ITEM_USED.get(Items.CAKE));

                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                return InteractionResult.SUCCESS;
            }
        }

        if (level.isClientSide) {
            if (eat(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if (stack.isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return eat(level, pos, state, player);
    }

    public void afterCandlePlaced(Level level, BlockState state, BlockPos pos, BaseCandleCakeBlock candleCake) {
    }

    public InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false) && !canAlwaysEat) {
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
            if (cakeEffects != null) {
                applyEffects(player);
            }
        }

        if (player instanceof ServerPlayer serverPlayer) {
            JustMoreCakes.CAKE_EATEN_TRIGGER.trigger(serverPlayer, this);
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

    public void applyEffects(Player player) {
        for (MobEffectHolder holder : cakeEffects.mobEffects()) {
            applyEffectFromHolder(holder, player);
        }
    }

    public BlockState eatActions(Player player, BlockPos pos, BlockState state) {
        if (equals(ModBlocks.FIREY_CAKE.get())) {
            player.setSecondsOnFire(ModCommonConfigs.FIREY_CAKE_ON_FIRE_DUR.get());
        }
        else if (equals(ModBlocks.ICE_CAKE.get())) {
            player.clearFire();
        }
        else if (equals(ModBlocks.CHORUS_CAKE.get())) {
            Util.teleportRandomly(player, ModCommonConfigs.CHORUS_CAKE_TELEPORT_RADIUS.get());
        }
        else if (equals(ModBlocks.ENDER_CAKE.get())) {
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
        return ((getSlices() + 1) - state.getValue(getBites())) * 2;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType computation) {
        return false;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(getter, pos, state);
        if (stack.isEmpty() && getFamily() != null) {
            return new ItemStack(getFamily().getBaseCake().get());
        }
        return stack;
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

    public CakeBuilder getBuilder() {
        return builder;
    }

    @Nullable
    public CakeFamily getFamily() {
        return builder.getFamily();
    }

    public boolean isBaseCake() {
        return builder.getVariant() == CakeVariant.BASE;
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
    public void justMoreCakes$setCakeEffects(CakeEffects cakeEffects) {
        this.cakeEffects = cakeEffects;
    }

    public int getNutrition() {
        if (builder != null) {
            return builder.getNutrition();
        }
        return DEFAULT_NUTRITION;
    }

    public float getSaturationModifier() {
        if (builder != null) {
            return builder.getSaturationModifier();
        }
        return DEFAULT_SATURATION_MODIFIER;
    }

    public static boolean isUneaten(BlockState state, BlockPos pos, Level level) {
        Block block = state.getBlock();
        if (block instanceof BaseThreeTieredCakeBlock threeTieredCakeBlock) {
            if (state.getValue(BaseThreeTieredCakeBlock.HALF) == DoubleBlockHalf.UPPER) {
                return isUneaten(state, threeTieredCakeBlock);
            }

            BlockState aboveState = level.getBlockState(pos.above());
            if (aboveState.getBlock() instanceof BaseThreeTieredCakeBlock aboveThreeTieredCakeBlock
                    && aboveState.getValue(BaseThreeTieredCakeBlock.HALF) == DoubleBlockHalf.UPPER) {
                return isUneaten(aboveState, aboveThreeTieredCakeBlock);
            }
            return false;
        }
        else if (block instanceof BaseCakeBlock cakeBlock && cakeBlock.getBites() != null) {
            return isUneaten(state, cakeBlock);
        }
        return !(block instanceof CakeBlock) || state.getValue(CakeBlock.BITES) == 0;
    }

    private static boolean isUneaten(BlockState state, BaseCakeBlock cakeBlock) {
        return cakeBlock.getSlices() <= 0 || state.getValue(cakeBlock.getBites()) == 0;
    }
}