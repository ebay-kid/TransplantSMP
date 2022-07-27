package ml.ikwid.transplantsmp.mixin.screen;

import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GenericContainerScreen.class)
public class MixinGenericContainerScreen {
	/*
	@ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/GenericContainerScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 6)
	private int changeRenderedHeight(int constant) {
		return constant - Constants.HOTBAR_SPACE_IN_INV_SCREEN;
	}
	*/
}
