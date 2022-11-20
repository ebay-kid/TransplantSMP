package ml.ikwid.transplantsmp.mixin.inventory;

import ml.ikwid.transplantsmp.common.recipe.RecipeOrganItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public class MixinCraftingResultSlot {
    @Shadow @Final private CraftingInventory input;

    @Inject(method = "onTakeItem", at = @At("HEAD"))
    private void onCrafted(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        var opt = player.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, this.input, player.world);
        Recipe<?> recipe;
        if(opt.isPresent()) {
            recipe = opt.get();
        } else {
            return;
        }
        if(recipe instanceof RecipeOrganItem recipeOrganItem) {
            for(int i = 0; i < 9; i++) {
                if(recipeOrganItem.getIngredientsAndCounts().get(i).key() == Ingredient.EMPTY || recipeOrganItem.getIngredientsAndCounts().get(i).value() == 0) {
                    continue;
                }
                input.getStack(i).decrement(recipeOrganItem.getIngredientsAndCounts().get(i).value() - 1);
            }
        }
    }
}
