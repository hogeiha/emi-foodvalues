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

	// "饥饿值6+" / "饱和度10+" 的档位，只登记食物真的达到的那些
	private static final int[] NUTRITION_THRESHOLDS = {2, 3, 4, 5, 6, 8, 10};
	private static final int[] SATURATION_THRESHOLDS = {5, 10, 15, 20};

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
			addSearchAliases(registry, food);
		}

		LOGGER.info("Registered {} food entries in 2 EMI categories", foods.size());
	}

	private static void addSearchAliases(EmiRegistry registry, ItemStack food) {
		EmiStack stack = EmiStack.of(food);
		int nutrition = FoodValueUtil.nutrition(food);
		float saturation = FoodValueUtil.saturation(food);

		// 食物：全部可食用物品
		registry.addAlias(stack, Component.translatable("emifoodvalues.alias.food"));

		// 饥饿值：实际回复的饥饿值。饥饿值8 / 饥饿值=8 / 饥饿值6+
		// 饱食度是同义词，键名带 _alt
		for (String prefix : new String[] {"emifoodvalues.alias.hunger", "emifoodvalues.alias.hunger_alt"}) {
			registry.addAlias(stack, Component.translatable(prefix, nutrition));
			registry.addAlias(stack, Component.translatable(prefix + "_exact", nutrition));
			for (int threshold : NUTRITION_THRESHOLDS) {
				if (nutrition >= threshold) {
					registry.addAlias(stack, Component.translatable(prefix + "_at_least", threshold));
				}
			}
		}

		// 饱和度：隐藏的那个值，取整数部分。饱和度14 / 饱和度=14 / 饱和度10+
		int saturationValue = (int) saturation;
		registry.addAlias(stack, Component.translatable("emifoodvalues.alias.saturation", saturationValue));
		registry.addAlias(stack, Component.translatable("emifoodvalues.alias.saturation_exact", saturationValue));
		for (int threshold : SATURATION_THRESHOLDS) {
			if (saturation >= threshold) {
				registry.addAlias(stack,
						Component.translatable("emifoodvalues.alias.saturation_at_least", threshold));
			}
		}
	}
}
