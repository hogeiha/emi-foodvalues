package com.hogeiha.emifoodvalues;

import java.util.Comparator;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public final class FoodValueCategory {

	public static final ResourceLocation SATURATION_ID =
			new ResourceLocation(EmiFoodValues.MOD_ID, "food_values_saturation");
	public static final ResourceLocation NUTRITION_ID =
			new ResourceLocation(EmiFoodValues.MOD_ID, "food_values_nutrition");

	public static final EmiRecipeCategory SATURATION = new EmiRecipeCategory(SATURATION_ID,
			EmiStack.of(Items.GOLDEN_CARROT), EmiStack.of(Items.GOLDEN_CARROT), sort(true));

	public static final EmiRecipeCategory NUTRITION = new EmiRecipeCategory(NUTRITION_ID,
			EmiStack.of(Items.COOKED_BEEF), EmiStack.of(Items.COOKED_BEEF), sort(false));

	private FoodValueCategory() {
	}

	private static Comparator<EmiRecipe> sort(boolean bySaturation) {
		return (a, b) -> {
			if (!(a instanceof FoodValueRecipe first) || !(b instanceof FoodValueRecipe second)) {
				return 0;
			}
			int result = bySaturation
					? Float.compare(second.getSaturation(), first.getSaturation())
					: Integer.compare(second.getNutrition(), first.getNutrition());
			if (result != 0) {
				return result;
			}
			result = Integer.compare(second.getNutrition(), first.getNutrition());
			if (result != 0) {
				return result;
			}
			result = Float.compare(second.getSaturation(), first.getSaturation());
			if (result != 0) {
				return result;
			}
			return String.valueOf(first.getId()).compareTo(String.valueOf(second.getId()));
		};
	}
}
