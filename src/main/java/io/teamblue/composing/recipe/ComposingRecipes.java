package io.teamblue.composing.recipe;

import io.teamblue.composing.Composing;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ComposingRecipes {
	public static final RecipeSerializer<GuideBookRecipe> COMPOSING_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Composing.MODID, "guide_book"), new GuideBookRecipe.Serializer());
	public static void init() { }
}
