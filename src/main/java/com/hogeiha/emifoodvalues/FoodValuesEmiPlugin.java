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

		// Alias, so a plain search for food lists everything edible.
		Component foodAlias = Component.translatable("emifoodvalues.alias.food");

		for (ItemStack food : foods) {
			registry.addRecipe(new FoodValueRecipe(FoodValueCategory.SATURATION, food, FoodValueSort.SATURATION));
			registry.addRecipe(new FoodValueRecipe(FoodValueCategory.NUTRITION, food, FoodValueSort.NUTRITION));
			registry.addAlias(EmiStack.of(food), foodAlias);
		}

		LOGGER.info("Registered {} food entries in 2 EMI categories", foods.size());
	}
}
