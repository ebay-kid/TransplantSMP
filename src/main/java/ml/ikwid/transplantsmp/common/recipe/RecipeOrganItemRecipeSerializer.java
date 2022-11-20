package ml.ikwid.transplantsmp.common.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class RecipeOrganItemRecipeSerializer implements RecipeSerializer<RecipeOrganItem> {
    private RecipeOrganItemRecipeSerializer() {
    }

    public static final RecipeOrganItemRecipeSerializer INSTANCE = new RecipeOrganItemRecipeSerializer();
    public static final Identifier ID = new Identifier("transplantsmp", "organ_recipe");

    /**
     * JSON -> Recipe
     */
    @Override
    public RecipeOrganItem read(Identifier id, JsonObject json) {
        RecipeOrganItemJSONFormat recipeJSON = new Gson().fromJson(json, RecipeOrganItemJSONFormat.class);
        if(recipeJSON.ingredients == null || recipeJSON.output == null) {
            throw new JsonSyntaxException("RecipeOrganItem: JSON is missing ingredients or output");
        }

        List<RecipeOrganItem.Reference2IntPair<Ingredient>> ingredientsAndCounts = DefaultedList.ofSize(9, new RecipeOrganItem.Reference2IntPair<>(Ingredient.EMPTY, 0));
        for(var i : recipeJSON.ingredients) {
            JsonObject ingredientJSON = i.getAsJsonObject();
            if(ingredientJSON.get("item") == null || ingredientJSON.get("slot") == null) {
                throw new JsonSyntaxException("RecipeOrganItem: JSON is missing item or slot");
            }
            Ingredient ingredient = Ingredient.fromJson(ingredientJSON);
            int slot = ingredientJSON.get("slot").getAsInt();
            int count = ingredientJSON.get("count") == null ? 1 : ingredientJSON.get("count").getAsInt();

            ingredientsAndCounts.set(slot, new RecipeOrganItem.Reference2IntPair<>(ingredient, count));
        }

        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJSON.output.get("item").getAsString())).orElseThrow(() -> new JsonSyntaxException("RecipeOrganItem: JSON is missing item"));
        ItemStack output = new ItemStack(outputItem, recipeJSON.output.get("count") == null ? 1 : recipeJSON.output.get("count").getAsInt());

        return new RecipeOrganItem(id, ingredientsAndCounts, output);
    }

    /**
     * Packet -> Recipe
     */
    @Override
    public RecipeOrganItem read(Identifier id, PacketByteBuf buf) {
        List<RecipeOrganItem.Reference2IntPair<Ingredient>> ingredientsAndCounts = DefaultedList.ofSize(9, new RecipeOrganItem.Reference2IntPair<>(Ingredient.EMPTY, 0));
        for(int i = 0; i < 9; i++) {
            if(buf.readBoolean()) {
                Ingredient ingredient = Ingredient.fromPacket(buf);
                int count = buf.readVarInt();
                ingredientsAndCounts.set(i, new RecipeOrganItem.Reference2IntPair<>(ingredient, count));
            }
        }

        return new RecipeOrganItem(id, ingredientsAndCounts, buf.readItemStack());
    }

    /**
     * Recipe -> Packet
     */
    @Override
    public void write(PacketByteBuf buf, RecipeOrganItem recipe) {
        var ingredientsAndCounts = recipe.getIngredientsAndCounts();
        for(var i : ingredientsAndCounts) {
            if(i == null || i.key() == Ingredient.EMPTY || i.value() == 0) {
                buf.writeBoolean(false); // implying skip
            } else {
                buf.writeBoolean(true); // implying there is a thing
                i.key().write(buf);
                buf.writeVarInt(i.value());
            }
        }
        buf.writeItemStack(recipe.getOutput());
    }
}
