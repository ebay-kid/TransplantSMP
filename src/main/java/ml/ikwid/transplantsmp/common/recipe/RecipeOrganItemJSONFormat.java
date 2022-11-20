package ml.ikwid.transplantsmp.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Define the JSON format for the Organ Item recipe.
 * The JSON format should be as such:
 * <pre>
 * {
 *   "type": "transplantsmp:organ_recipe",
 *   "ingredients": [
 *      { "item": "minecraft:iron_ingot", "slot": 0, "count": 5 },
 *      { "item": "minecraft:gold_ingot", "slot": 1, "count": 1 }
 *      { "item": "minecraft:diamond", "slot": 2, "count": 3 }
 *      { "item": "minecraft:emerald", "slot": 3, "count": 4 }
 *      { "item": "minecraft:quartz", "slot": 4, "count": 6 }
 *      { "item": "minecraft:glowstone_dust", "slot": 5, "count": 9 }
 *      { "item": "minecraft:blaze_powder", "slot": 6, "count": 20 }
 *      { "item": "minecraft:ender_pearl", "slot": 7, "count": 32 }
 *      { "item": "minecraft:slime_ball", "slot": 8, "count": 10 }
 *   ],
 *   "output": { "item": "transplantsmp:organ_item", "count": 1 }
 * }
 * </pre>
 * <p>
 * where "slot" is the slot number of the ingredient in the crafting grid (0 to 8, 0 is top left, 2 is top right, 8 is bottom right)
 * and "count" is the number of items required for each ItemStack (not required for either section, defaults to 1).
 *
 */
public class RecipeOrganItemJSONFormat {
    JsonArray ingredients;
    JsonObject output;
}
