package com.hogeiha.emifoodvalues;

public enum FoodValueSort {
	SATURATION("saturation"),
	NUTRITION("nutrition");

	private final String serializedName;

	FoodValueSort(String serializedName) {
		this.serializedName = serializedName;
	}

	public String getSerializedName() {
		return serializedName;
	}
}
