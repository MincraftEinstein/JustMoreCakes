package einstein.jmc.util;

public interface CakeOvenConstants {
	static final int SLOT_COUNT = 6;
	static final int DATA_COUNT = 4;
	static final int INGREDIENT_SLOT_1 = 0;
	static final int INGREDIENT_SLOT_2 = 1;
	static final int INGREDIENT_SLOT_3 = 2;
	static final int INGREDIENT_SLOT_4 = 3;
	static final int FUEL_SLOT = 4;
	static final int RESULT_SLOT = 5;
	static final int INGREDIENT_SLOT_COUNT = 4;
	static final int DEFAULT_BURN_TIME = 200;
	static final int[] HOPPER_PULL_SLOTS = { RESULT_SLOT };
	static final int[] HOPPER_FEED_THROUGH_TOP_SLOTS = { INGREDIENT_SLOT_1, INGREDIENT_SLOT_2, INGREDIENT_SLOT_3, INGREDIENT_SLOT_4 };
	static final int[] HOPPER_FEED_THROUGH_SIDE_SLOTS = { FUEL_SLOT };
}
