package einstein.jmc.init;

import java.util.Arrays;
import java.util.Comparator;

public enum CakeTypes {

	CHOCOLATE(0, "chocolate"),
	CARROT(1, "carrot"),
	PUMPKIN(2, "pumpkin"),
	MELON(3, "melon"),
	APPLE(4, "apple"),
	POISON(5, "poison"),
	COOKIE(6, "cookie"),
	GOLDEN_APPLE(7, "golden_apple"),
	FIREY(8, "firey"),
	REDSTONE(9, "redstone"),
	ENDER(10, "ender"),
	CHEESE(11, "cheese"),
	THREE_TIERED(12, "three_tiered"),
	SLIME(13, "slime"),
	BEETROOT(14, "beetroot"),
	LAVA(15, "lava"),
	CREEPER(16, "creeper"),
	SEED(17, "seed"),
	ICE(18, "ice"),
	CHRISTMAS(19, "christmas"),
	SPRINKLE(20, "sprinkle"),
	SWEET_BERRY(21, "sweet_berry"),
	HONEY(22, "honey"),
	WARPED_FUNGUS(23, "warped_fungus");
	
	private final int id;
	private final String name;

	private static final CakeTypes[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(CakeTypes::getId)).toArray((id) -> {
				return new CakeTypes[id];
			});

	private CakeTypes(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static CakeTypes byId(int id) {
		if (id < 0 || id >= BY_ID.length) {
			id = 0;
		}
		
		return BY_ID[id];
	}
}
