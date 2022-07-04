package ml.ikwid.transplantsmp.mixin;

import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen {
	ClientPlayerEntity player = MinecraftClient.getInstance().player;
	ITransplantable transplantable = (ITransplantable) player;

	@ModifyConstant(method = "onMouseClick(I)V", constant = @Constant(intValue = 9))
	private int fixHotkey(int constant) {
		return transplantable.getHotbarDraws();
	}

	@ModifyConstant(method = "onMouseClick(I)V", constant = @Constant(intValue = 40))
	private int changeOffHand(int constant) {
		return Constants.OFF_HAND;
	}

	@ModifyConstant(method = "handleHotbarKeyPressed", constant = @Constant(intValue = 40))
	private int changeOffHand2(int constant) {
		return Constants.OFF_HAND;
	}

	@ModifyConstant(method = "handleHotbarKeyPressed", constant = @Constant(intValue = 9))
	private int fixHotkey2(int constant) {
		return transplantable.getHotbarDraws();
	}

	/**
	 * @author 6Times
	 * @reason For Arm/Skin Transplant users to access their slots.
	 */
	@Overwrite
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
		return false;
	}
}
