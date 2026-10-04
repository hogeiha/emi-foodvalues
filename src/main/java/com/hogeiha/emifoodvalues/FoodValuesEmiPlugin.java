package com.hogeiha.emifoodvalues;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

@EmiEntrypoint
public class FoodValuesEmiPlugin implements EmiPlugin {

	private static final Logger LOGGER = LogManager.getLogger(EmiFoodValues.MOD_ID);
	private static final int[] NUTRITION_THRESHOLDS = {4, 6, 8};
	private static final int[] SATURATION_THRESHOLDS = {5, 10, 15};

	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(FoodValueCategory.SATURATION);
		registry.addCategory(FoodValueCategory.NUTRITION);

		List<ItemStack> foods = new ArrayList<>();
		for (Item item : ForgeRegistries.ITEMS) {
			ItemStack stack = new ItemStack(item);
			if (FoodValueUtil.isFood(stack)) {
				foods.add(stack);
			}
		}

		for (ItemStack food : foods) {
			registry.addRecipe(new FoodValueRecipe(FoodValueCategory.SATURATION, food, FoodValueSort.SATURATION));
			registry.addRecipe(new FoodValueRecipe(FoodValueCategory.NUTRITION, food, FoodValueSort.NUTRITION));

			EmiStack stack = EmiStack.of(food);
			int nutrition = FoodValueUtil.nutrition(food);
			float saturation = FoodValueUtil.saturation(food);

			// Aliases so the search bar can filter by the two values.
			registry.addAlias(stack, Component.translatable("emifoodvalues.alias.food"));
			registry.addAlias(stack, Component.translatable("emifoodvalues.alias.nutrition", nutrition));
			registry.addAlias(stack, Component.translatable("emifoodvalues.alias.saturation", (int) saturation));

			for (int threshold : NUTRITION_THRESHOLDS) {
				if (nutrition >= threshold) {
					registry.addAlias(stack,
							Component.translatable("emifoodvalues.alias.nutrition_at_least", threshold));
				}
			}
			for (int threshold : SATURATION_THRESHOLDS) {
				if (saturation >= threshold) {
					registry.addAlias(stack,
							Component.translatable("emifoodvalues.alias.saturation_at_least", threshold));
				}
			}
		}

		LOGGER.info("Registered {} food entries in 2 EMI categories", foods.size());
	}
}
