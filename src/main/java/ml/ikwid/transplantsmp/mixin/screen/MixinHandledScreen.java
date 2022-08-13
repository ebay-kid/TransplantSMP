package ml.ikwid.transplantsmp.mixin.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.inventory.HotbarSlot;
import ml.ikwid.transplantsmp.common.util.Constants;
import ml.ikwid.transplantsmp.common.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen {
	@Shadow protected int x;
	@Shadow protected int y;
	@Shadow protected int backgroundHeight;
	@SuppressWarnings("rawtypes")
	private final HandledScreen self = (HandledScreen)(Object) this;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawBackground(Lnet/minecraft/client/util/math/MatrixStack;FII)V"))
	private void renderHotbarSection(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		// draw hotbar ourselves with the exact same texture as the HUD because I'm slightly lazy
		RenderSystem.setShaderTexture(0, Constants.WIDGETS_TEXTURE);

		int x = this.x;
		int y = this.y;
		int height = this.backgroundHeight;

		// TransplantSMP.LOGGER.info("bg height: " + height);
		int bottom = y + height;
		// TransplantSMP.LOGGER.info("render y: " + bottom);

		int draws = ((ITransplantable) (Objects.requireNonNull(MinecraftClient.getInstance().player))).getHotbarDraws();
		for(int i = 0; i < draws; i++) {
			self.drawTexture(matrices, x + ((ITransplantable) (MinecraftClient.getInstance().player)).xShift() + (i * Constants.OUTER_SLOT_WIDTH), bottom, 0, 0, Constants.OUTER_SLOT_WIDTH, Constants.OUTER_SLOT_HEIGHT);
		}

		// RenderSystem.setShaderTexture(0, HandledScreen.BACKGROUND_TEXTURE); // fix it for the rest of the code
	}

	@Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/screen/slot/Slot;x:I", opcode = Opcodes.GETFIELD, ordinal = 0))
	private int changeXHighlightRender(Slot slot) {
		if(!(slot instanceof HotbarSlot hotbarSlot)) {
			return slot.x;
		}
		return Utils.calcSlotXShiftArb(hotbarSlot.getTransplantable().getHotbarDraws(), hotbarSlot.getIndex());
	}

	@Redirect(method = "drawSlot", at = @At(value = "FIELD", target = "Lnet/minecraft/screen/slot/Slot;x:I", opcode = Opcodes.GETFIELD, ordinal = 0))
	private int changeXSlotRender1(Slot slot) {
		if(!(slot instanceof HotbarSlot hotbarSlot)) {
			return slot.x;
		}
		return Utils.calcSlotXShiftArb(hotbarSlot.getTransplantable().getHotbarDraws(), hotbarSlot.getIndex());
	}

	@Redirect(method = "drawSlot", at = @At(value = "FIELD", target = "Lnet/minecraft/screen/slot/Slot;x:I", opcode = Opcodes.GETFIELD, ordinal = 1))
	private int changeXSlotRender2(Slot slot) {
		if(!(slot instanceof HotbarSlot hotbarSlot)) {
			return slot.x;
		}
		return Utils.calcSlotXShiftArb(hotbarSlot.getTransplantable().getHotbarDraws(), hotbarSlot.getIndex());
	}

	@ModifyConstant(method = "onMouseClick(I)V", constant = @Constant(intValue = 9))
	private int fixHotkey(int constant) {
		return ((ITransplantable) (Objects.requireNonNull(MinecraftClient.getInstance().player))).getHotbarDraws();
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
		return ((ITransplantable) (Objects.requireNonNull(MinecraftClient.getInstance().player))).getHotbarDraws();
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
