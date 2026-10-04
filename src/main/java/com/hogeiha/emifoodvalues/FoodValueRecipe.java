package com.hogeiha.emifoodvalues;

import java.util.List;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class FoodValueRecipe implements EmiRecipe {

	public static final int WIDTH = 132;
	public static final int HEIGHT = 30;
	private static final int TEXT_X = 20;

	private final EmiRecipeCategory category;
	private final EmiStack stack;
	private final int nutrition;
	private final float saturation;
	private final ResourceLocation id;

	public FoodValueRecipe(EmiRecipeCategory category, ItemStack itemStack, FoodValueSort sort) {
		this.category = category;
		this.stack = EmiStack.of(itemStack);
		this.nutrition = FoodValueUtil.nutrition(itemStack);
		this.saturation = FoodValueUtil.saturation(itemStack);

		ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
		String path = itemId == null ? "unknown" : itemId.getNamespace() + "/" + itemId.getPath();
		this.id = new ResourceLocation(EmiFoodValues.MOD_ID,
				"/food/" + sort.getSerializedName() + "/" + path);
	}

	public EmiStack getStack() {
		return stack;
	}

	public int getNutrition() {
		return nutrition;
	}

	public float getSaturation() {
		return saturation;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return category;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return List.of(stack);
	}

	@Override
	public List<EmiStack> getOutputs() {
		// Info entry, no crafting result.
		return List.of();
	}

	@Override
	public boolean supportsRecipeTree() {
		return false;
	}

	@Override
	public int getDisplayWidth() {
		return WIDTH;
	}

	@Override
	public int getDisplayHeight() {
		return HEIGHT;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addSlot(stack, 0, 0).recipeContext(this);
		widgets.addText(Component.literal(trimToWidth(stack.getName().getString())), TEXT_X, 1, 0xFFFFFF, true);
		widgets.addText(Component.translatable("emifoodvalues.text.nutrition", nutrition),
				TEXT_X, 11, 0xFFC04D, true);
		widgets.addText(Component.translatable("emifoodvalues.text.saturation",
				FoodValueUtil.formatSaturation(saturation)), TEXT_X, 21, 0x7FE07F, true);
	}

	// EMI does not clip widgets, so long names have to be cut to the entry width.
	private static String trimToWidth(String name) {
		Minecraft client = Minecraft.getInstance();
		if (client == null || client.font == null) {
			return name;
		}
		return client.font.plainSubstrByWidth(name, WIDTH - TEXT_X - 2);
	}
}
