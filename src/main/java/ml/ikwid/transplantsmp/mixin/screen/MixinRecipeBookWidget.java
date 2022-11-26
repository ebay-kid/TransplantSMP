package ml.ikwid.transplantsmp.mixin.screen;

import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RecipeBookWidget.class)
public abstract class MixinRecipeBookWidget {
	/**
	 * idk at this point
	 * @author 6Times
	 * @reason loooooool
	 */
	/*
	@Overwrite
	public boolean isClickOutsideBounds(double mouseX, double mouseY, int x, int y, int backgroundWidth, int backgroundHeight, int button) {
		return false;
	}
	 */
}
