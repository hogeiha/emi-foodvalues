package com.hogeiha.emifoodvalues;

import java.util.Locale;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

public final class FoodValueUtil {

	private FoodValueUtil() {
	}

	public static FoodProperties food(ItemStack stack) {
		return stack.getFoodProperties(null);
	}

	public static boolean isFood(ItemStack stack) {
		FoodProperties food = food(stack);
		return food != null && food.getNutrition() > 0;
	}

	public static int nutrition(ItemStack stack) {
		FoodProperties food = food(stack);
		return food == null ? 0 : food.getNutrition();
	}

	public static float saturationModifier(ItemStack stack) {
		FoodProperties food = food(stack);
		return food == null ? 0.0F : food.getSaturationModifier();
	}

	/** Vanilla formula: nutrition * saturation modifier * 2. */
	public static float saturation(ItemStack stack) {
		return nutrition(stack) * saturationModifier(stack) * 2.0F;
	}

	public static String formatSaturation(float saturation) {
		return String.format(Locale.ROOT, "%.1f", saturation);
	}
}
