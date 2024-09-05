package einstein.jmc.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.BaseThreeTieredCakeBlock;
import einstein.jmc.block.cake.candle.BaseThreeTieredCandleCakeBlock;
import einstein.jmc.init.ModCommonConfigs;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.registration.family.CakeFamily;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.mcLoc;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER;

public class CakeUtil {

    public static final int DEFAULT_NUTRITION = 2;
    public static final float DEFAULT_SATURATION_MODIFIER = 0.1F;
    public static final ImmutableMap<Block, Block> VANILLA_CANDLE_CAKES_BY_CANDLE = new ImmutableMap.Builder<Block, Block>()
            .put(Blocks.CANDLE, Blocks.CANDLE_CAKE)
            .put(Blocks.WHITE_CANDLE, Blocks.WHITE_CANDLE_CAKE)
            .put(Blocks.ORANGE_CANDLE, Blocks.ORANGE_CANDLE_CAKE)
            .put(Blocks.MAGENTA_CANDLE, Blocks.MAGENTA_CANDLE_CAKE)
            .put(Blocks.LIGHT_BLUE_CANDLE, Blocks.LIGHT_BLUE_CANDLE_CAKE)
            .put(Blocks.YELLOW_CANDLE, Blocks.YELLOW_CANDLE_CAKE)
            .put(Blocks.LIME_CANDLE, Blocks.LIME_CANDLE_CAKE)
            .put(Blocks.PINK_CANDLE, Blocks.PINK_CANDLE_CAKE)
            .put(Blocks.GRAY_CANDLE, Blocks.GRAY_CANDLE_CAKE)
            .put(Blocks.LIGHT_GRAY_CANDLE, Blocks.LIGHT_GRAY_CANDLE_CAKE)
            .put(Blocks.CYAN_CANDLE, Blocks.CYAN_CANDLE_CAKE)
            .put(Blocks.PURPLE_CANDLE, Blocks.PURPLE_CANDLE_CAKE)
            .put(Blocks.BLUE_CANDLE, Blocks.BLUE_CANDLE_CAKE)
            .put(Blocks.BROWN_CANDLE, Blocks.BROWN_CANDLE_CAKE)
            .put(Blocks.GREEN_CANDLE, Blocks.GREEN_CANDLE_CAKE)
            .put(Blocks.RED_CANDLE, Blocks.RED_CANDLE_CAKE)
            .put(Blocks.BLACK_CANDLE, Blocks.BLACK_CANDLE_CAKE)
            .buildOrThrow();
    public static final Map<Block, ResourceLocation> SUPPORTED_CANDLES = Util.make(new HashMap<>(), map -> {
        // Not using a for loop to prevent modded colors from being added,
        // the main reason for this is so unsupported candles won't create candle cakes with missing assets
        map.put(Blocks.CANDLE, mcLoc(""));
        map.put(Blocks.WHITE_CANDLE, mcLoc("white_"));
        map.put(Blocks.ORANGE_CANDLE, mcLoc("orange_"));
        map.put(Blocks.MAGENTA_CANDLE, mcLoc("magenta_"));
        map.put(Blocks.LIGHT_BLUE_CANDLE, mcLoc("light_blue_"));
        map.put(Blocks.YELLOW_CANDLE, mcLoc("yellow_"));
        map.put(Blocks.LIME_CANDLE, mcLoc("lime_"));
        map.put(Blocks.PINK_CANDLE, mcLoc("pink_"));
        map.put(Blocks.GRAY_CANDLE, mcLoc("gray_"));
        map.put(Blocks.LIGHT_GRAY_CANDLE, mcLoc("light_gray_"));
        map.put(Blocks.CYAN_CANDLE, mcLoc("cyan_"));
        map.put(Blocks.PURPLE_CANDLE, mcLoc("purple_"));
        map.put(Blocks.BLUE_CANDLE, mcLoc("blue_"));
        map.put(Blocks.BROWN_CANDLE, mcLoc("brown_"));
        map.put(Blocks.GREEN_CANDLE, mcLoc("green_"));
        map.put(Blocks.RED_CANDLE, mcLoc("red_"));
        map.put(Blocks.BLACK_CANDLE, mcLoc("black_"));
    });

    public static boolean inFamily(BlockState state, CakeFamily family) {
        return inFamily(state.getBlock(), family);
    }

    public static boolean inFamily(Block block, CakeFamily family) {
        return block.equals(family.getBaseCake().get()) || block.equals(family.getTwoTieredCake().get()) || block.equals(family.getThreeTieredCake().get());
    }

    public static boolean isUneaten(BlockState state, BlockPos pos, Level level) {
        Block block = state.getBlock();
        if (block instanceof BaseThreeTieredCakeBlock threeTieredCakeBlock) {
            if (state.getValue(BaseThreeTieredCakeBlock.HALF) == UPPER) {
                return isUneaten(state, threeTieredCakeBlock);
            }

            BlockState aboveState = level.getBlockState(pos.above());
            if (aboveState.getBlock() instanceof BaseThreeTieredCakeBlock aboveThreeTieredCakeBlock
                    && aboveState.getValue(BaseThreeTieredCakeBlock.HALF) == UPPER) {
                return isUneaten(aboveState, aboveThreeTieredCakeBlock);
            }
            return false;
        }
        else if (block instanceof BaseCakeBlock cakeBlock && cakeBlock.hasBites()) {
            return isUneaten(state, cakeBlock);
        }
        return !(block instanceof CakeBlock) || state.getValue(CakeBlock.BITES) == 0;
    }

    private static boolean isUneaten(BlockState state, BaseCakeBlock cakeBlock) {
        return cakeBlock.getSlices() <= 0 || state.getValue(cakeBlock.getBites()) == 0;
    }

    public static int getComparatorOutput(BlockState state) {
        if (state.getBlock() instanceof BaseCakeBlock cakeBlock) {
            if (cakeBlock.hasBites()) {
                return getMultipliedSignal(cakeBlock.isBaseVariant(), (cakeBlock.getSlices() + 1) - state.getValue(cakeBlock.getBites()));
            }

            CakeVariant.Type variant = cakeBlock.getVariant().getType();
            return getMultipliedSignal(cakeBlock.isBaseVariant(), switch (variant) {
                case BASE -> 7;
                case TWO_TIERED -> 11;
                case THREE_TIERED -> 16;
            });
        }
        return 0;
    }

    public static int getMultipliedSignal(boolean isBaseVariant, int signal) {
        return signal * (ModCommonConfigs.DOUBLE_BASE_CAKE_COMPARATOR_OUTPUT.get() && isBaseVariant ? 2 : 1);
    }

    public static BlockState createLowerState(Block block, boolean hasBites) {
        BlockState newState = block.defaultBlockState().setValue(BaseThreeTieredCakeBlock.HALF, LOWER);

        if (hasBites) {
            BaseCakeBlock cakeBlock = ((BaseCakeBlock) block);
            if (cakeBlock.hasBites()) {
                newState = newState.setValue(cakeBlock.getBites(), 5);
            }
        }

        return newState;
    }

    public static void destroyOppositeHalf(BlockState state, BlockPos pos, Level level, ItemStack toolStack, boolean dropResources) {
        boolean isLower = state.getValue(BaseThreeTieredCakeBlock.HALF) == LOWER;
        BlockPos otherPos = isLower ? pos.above() : pos.below();
        BlockState otherState = level.getBlockState(otherPos);

        if (otherState.is(state.getBlock()) && otherState.getValue(BaseThreeTieredCakeBlock.HALF) == (isLower ? UPPER : LOWER)) {
            level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 19); // 19 ignores block shape updates
            level.levelEvent(2001, otherPos, Block.getId(otherState));

            otherState.updateNeighbourShapes(level, otherPos, 2, Block.UPDATE_LIMIT - 1);
            otherState.updateIndirectNeighbourShapes(level, otherPos, 2, Block.UPDATE_LIMIT - 1);

            if (dropResources) {
                BlockEntity otherBlockEntity = otherState.hasBlockEntity() ? level.getBlockEntity(otherPos) : null;
                Block.dropResources(otherState, level, otherPos, otherBlockEntity, null, toolStack);
            }
        }
    }

    public static InteractionResult convertToThreeTiered(CakeFamily family, BlockState state, BlockPos pos, Level level, Player player, ItemStack stack, boolean ignoreUneaten) {
        if (ignoreUneaten || isUneaten(state, pos, level)) {
            BlockPos abovePos = pos.above();
            if (level.getBlockState(abovePos).canBeReplaced()) {
                BlockState newState = family.getThreeTieredCake().get().defaultBlockState();

                level.setBlockAndUpdate(abovePos, newState);
                level.setBlockAndUpdate(pos, createLowerState(newState.getBlock(), true));
                Block.pushEntitiesUp(Blocks.AIR.defaultBlockState(), newState, level, abovePos);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, abovePos);
                level.playSound(null, abovePos, newState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1, 1);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));

                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult convertToTwoTiered(CakeFamily family, BlockState state, BlockPos pos, Level level, Player player, ItemStack stack, boolean ignoreUneaten) {
        if (ignoreUneaten || isUneaten(state, pos, level)) {
            BlockState newState = family.getTwoTieredCake().get().defaultBlockState();

            level.setBlockAndUpdate(pos, newState);
            Block.pushEntitiesUp(state, newState, level, pos);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            level.playSound(null, pos, newState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1, 1);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));

            if (!player.isCreative()) {
                stack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static ImmutableList<Block> getVanillaCandleCakes() {
        return VANILLA_CANDLE_CAKES_BY_CANDLE.values().asList();
    }

    public static <T> T redirectUse(Block block, BlockState state, Level level, BlockPos pos, BiFunction<BlockState, BlockPos, T> aboveResult, Supplier<T> superResult) {
        if (state.getValue(BaseThreeTieredCandleCakeBlock.HALF) == LOWER) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);

            if (aboveState.is(block) && aboveState.getValue(BaseThreeTieredCandleCakeBlock.HALF) == UPPER) {
                return aboveResult.apply(aboveState, abovePos);
            }
        }
        return superResult.get();
    }
}
