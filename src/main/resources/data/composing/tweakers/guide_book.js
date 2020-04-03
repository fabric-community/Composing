var RecipeTweaker = libcd.require("libcd.recipe.RecipeTweaker");
var TweakerUtils = libcd.require("libcd.util.TweakerUtils");

var GuideBook = TweakerUtils.createItemStack("patchouli:guide_book");
TweakerUtils.addNbtToStack(GuideBook, '{"patchouli:book":"composing:composing_howto"}');
RecipeTweaker.addShapeless(["minecraft:book", "#composing:small_crystal"], GuideBook);
