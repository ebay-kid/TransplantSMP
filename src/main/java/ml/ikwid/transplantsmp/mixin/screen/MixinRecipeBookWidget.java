package ml.ikwid.transplantsmp.mixin.screen;

import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RecipeBookWidget.class)
public class MixinRecipeBookWidget {
	/*
	@ModifyConstant(method = "reset", constant = @Constant(intValue = 0, ordinal = 0))
	private int shiftRecipeBook1(int original) {
		return original - Constants.OUTER_SLOT_WIDTH;
	}

	@ModifyConstant(method = "reset", constant = @Constant(intValue = 86, ordinal = 0))
	private int shiftRecipeBook2(int original) {
		return original - Constants.OUTER_SLOT_WIDTH;
	}

	@ModifyConstant(method = "isWide", constant = @Constant(intValue = 86, ordinal = 0))
	private int changeConst(int original) {
		return original - Constants.OUTER_SLOT_WIDTH;
	}
	*/

	/**
	 * @author 6Times
	 * @reason loooooool
	 */
	@Overwrite
	public boolean isClickOutsideBounds(double mouseX, double mouseY, int x, int y, int backgroundWidth, int backgroundHeight, int button) {
		return false;
	}
}
