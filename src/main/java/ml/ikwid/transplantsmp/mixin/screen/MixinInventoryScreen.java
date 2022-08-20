package ml.ikwid.transplantsmp.mixin.screen;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen {
	@Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", ordinal = 0))
	private Element screwTheRecipeBook(InventoryScreen instance, Element element) {
		return null;
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;isOpen()Z", ordinal = 0))
	private boolean itIsNotOpen(RecipeBookWidget instance) {
		return false;
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", ordinal = 1))
	private void screwTheRecipeBook2(RecipeBookWidget instance, MatrixStack matrices, int mouseX, int mouseY, float delta) {
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;drawGhostSlots(Lnet/minecraft/client/util/math/MatrixStack;IIZF)V", ordinal = 0))
	private void screwTheRecipeBook3(RecipeBookWidget instance, MatrixStack matrices, int x, int y, boolean bl, float delta) {
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;drawTooltip(Lnet/minecraft/client/util/math/MatrixStack;IIII)V", ordinal = 0))
	private void screwTheRecipeBook4(RecipeBookWidget instance, MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
	}
}
