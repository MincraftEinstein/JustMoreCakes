package einstein.jmc.data;

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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class ModAdvancementsGenerator extends AdvancementProvider {

	private static final RegistryObject<?>[] CAKES = { ModBlocks.APPLE_CAKE, ModBlocks.BEETROOT_CAKE, ModBlocks.BROWN_MUSHROOM_CAKE,
			ModBlocks.CARROT_CAKE, ModBlocks.CHEESECAKE, ModBlocks.CHOCOLATE_CAKE, ModBlocks.CHORUS_CAKE, ModBlocks.COOKIE_CAKE,
			ModBlocks.CREEPER_CAKE, ModBlocks.CRIMSON_FUNGUS_CAKE, ModBlocks.CUPCAKE, ModBlocks.ENDER_CAKE, ModBlocks.FIREY_CAKE, ModBlocks.GLOWSTONE_CAKE,
			ModBlocks.GOLDEN_APPLE_CAKE, ModBlocks.HONEY_CAKE, ModBlocks.ICE_CAKE, ModBlocks.LAVA_CAKE, ModBlocks.MELON_CAKE, ModBlocks.POISON_CAKE,
			ModBlocks.PUMPKIN_CAKE, ModBlocks.RED_MUSHROOM_CAKE, ModBlocks.REDSTONE_CAKE, ModBlocks.SEED_CAKE, ModBlocks.SLIME_CAKE,
			ModBlocks.SWEET_BERRY_CAKE, ModBlocks.THREE_TIERED_CAKE, ModBlocks.TNT_CAKE, ModBlocks.WARPED_FUNGUS_CAKE, ModBlocks.RED_VELVET_CAKE, ModBlocks.GLOW_BERRY_CAKE };

	public ModAdvancementsGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, existingFileHelper);
	}
	
	@Override
	protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
		Advancement craftCake = addCakes(Advancement.Builder.advancement().parent(new ResourceLocation("minecraft:husbandry/plant_seed")).requirements(RequirementsStrategy.OR)
			.display(Blocks.CAKE.asItem(), translatable("craft_cake.title"), translatable("craft_cake.description"), null, FrameType.TASK, true, true, false))
			.save(consumer, modLoc("husbandry/craft_cake"), fileHelper);
		addCakes(Advancement.Builder.advancement().parent(craftCake))
			.display(ModBlocks.CHOCOLATE_CAKE.get(), translatable("craft_all_cakes.title"), translatable("craft_all_cakes.description"), null, FrameType.CHALLENGE, true, true, false)
			.rewards(AdvancementRewards.Builder.experience(100)).save(consumer, modLoc("husbandry/craft_all_cakes"), fileHelper);
	}
	
	private Advancement.Builder addCakes(Advancement.Builder advancement) {
		advancement.addCriterion("cake", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(Blocks.CAKE).build()));

		for (RegistryObject<?> cake : CAKES) {
			Item cakeItem = ((ItemLike) cake.get()).asItem();
			advancement.addCriterion(cake.getId().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(cakeItem).build()));
		}
		
		return advancement;
	}
	
	private Component translatable(String str) {
		return Component.translatable("advancements.husbandry." + str);
	}
	
	private ResourceLocation modLoc(String path) {
		return new ResourceLocation(JustMoreCakes.MOD_ID, path);
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes advancements";
	}
}
