package ml.ikwid.transplantsmp.common.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord") // idk man you weren't able to make it a record but you said to do it :P
public class RecipeOrganItem implements CraftingRecipe {
    private final List<Reference2IntPair<Ingredient>> ingredientsAndCounts;
    private final ItemStack output;
    private final Identifier id;

    public record Reference2IntPair<T>(T key, int value) {
    }

    public RecipeOrganItem(Identifier id, List<Reference2IntPair<Ingredient>> ingredientsAndCounts, ItemStack output) {
        this.id = id;
        this.ingredientsAndCounts = ingredientsAndCounts;
        this.output = output;
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        if(world.isClient()) {
            return false;
        }
        for(int i = 0; i < 9; i++) {
            if(ingredientsAndCounts.get(i).key() == Ingredient.EMPTY || ingredientsAndCounts.get(i).value() == 0) {
                continue;
            }
            if(!(ingredientsAndCounts.get(i).key().test(inventory.getStack(i)) && ingredientsAndCounts.get(i).value() <= inventory.getStack(i).getCount())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        return this.getOutput().copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeOrganItemRecipeSerializer.INSTANCE;
    }

    public List<Reference2IntPair<Ingredient>> getIngredientsAndCounts() {
        return this.ingredientsAndCounts;
    }
}
