package einstein.jmc.data;

import java.util.function.Consumer;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModAdvancementsGenerator extends AdvancementProvider {

	private static final Block[] CAKES = { Blocks.CAKE, ModBlocks.APPLE_CAKE.get(), ModBlocks.BEETROOT_CAKE.get(), ModBlocks.BIRTHDAY_CAKE.get(), ModBlocks.BROWN_MUSHROOM_CAKE.get(),
			ModBlocks.CARROT_CAKE.get(), ModBlocks.CHEESECAKE.get(), ModBlocks.CHIRSTMAS_CAKE.get(), ModBlocks.CHOCOLATE_CAKE.get(), ModBlocks.CHORUS_CAKE.get(), ModBlocks.COOKIE_CAKE.get(),
			ModBlocks.CREEPER_CAKE.get(), ModBlocks.CRIMSON_FUNGUS_CAKE.get(), ModBlocks.CUPCAKE.get(), ModBlocks.ENDER_CAKE.get(), ModBlocks.FIREY_CAKE.get(), ModBlocks.GLOWSTONE_CAKE.get(),
			ModBlocks.GOLDEN_APPLE_CAKE.get(), ModBlocks.HONEY_CAKE.get(), ModBlocks.ICE_CAKE.get(), ModBlocks.LAVA_CAKE.get(), ModBlocks.MELON_CAKE.get(), ModBlocks.POISON_CAKE.get(),
			ModBlocks.PUMPKIN_CAKE.get(), ModBlocks.RED_MUSHROOM_CAKE.get(), ModBlocks.REDSTONE_CAKE.get(), ModBlocks.SEED_CAKE.get(), ModBlocks.SLIME_CAKE.get(), ModBlocks.SPRINKLE_CAKE.get(),
			ModBlocks.SWEET_BERRY_CAKE.get(), ModBlocks.THREE_TIERED_CAKE.get(), ModBlocks.TNT_CAKE.get(), ModBlocks.WARPED_FUNGUS_CAKE.get()};
	
	public ModAdvancementsGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, existingFileHelper);
	}
	
	@Override
	protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
		Advancement craftCake = addCakes(Advancement.Builder.advancement().parent(new ResourceLocation("minecraft:husbandry/plant_seed")).requirements(RequirementsStrategy.OR)
			.display(Blocks.CAKE.asItem(), traslatable("craft_cake.title"), traslatable("craft_cake.description"), (ResourceLocation)null, FrameType.TASK, true, true, false))
			.save(consumer, rl("husbandry/craft_cake"), fileHelper);
		addCakes(Advancement.Builder.advancement().parent(craftCake))
			.display(ModBlocks.CHOCOLATE_CAKE.get(), traslatable("craft_all_cakes.title"), traslatable("craft_all_cakes.description"), (ResourceLocation)null, FrameType.CHALLENGE, true, true, false)
			.rewards(AdvancementRewards.Builder.experience(100)).save(consumer, rl("husbandry/craft_all_cakes"), fileHelper);
	}
	
	private Advancement.Builder addCakes(Advancement.Builder advancement) {
		for (ItemLike cake : CAKES) {
			Item cakeItem = cake.asItem();
			advancement.addCriterion(ForgeRegistries.ITEMS.getKey(cakeItem).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(cakeItem).build()));
		}
		
		return advancement;
	}
	
	private TranslatableComponent traslatable(String str) {
		return new TranslatableComponent("advancements.husbandry." + str);
	}
	
	private ResourceLocation rl(String path) {
		return new ResourceLocation(JustMoreCakes.MODID, path);
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes advancements";
	}
}
