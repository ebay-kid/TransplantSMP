package ml.ikwid.transplantsmp.mixin.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import ml.ikwid.transplantsmp.api.ITransplantable;
import ml.ikwid.transplantsmp.common.transplants.ArmTransplant;
import ml.ikwid.transplantsmp.common.transplants.RegisterTransplants;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {
	@Shadow protected int x;
	@Shadow protected int y;

	@Shadow public abstract T getScreenHandler();

	@Shadow protected int backgroundWidth;

	protected MixinHandledScreen(Text title) {
		super(title);
	}

	private int getHotbarDraws(@NotNull ITransplantable transplantable) {
		return transplantable.getTransplantType() == RegisterTransplants.ARM_TRANSPLANT ? ((ArmTransplant) (transplantable.getTransplantType())).getHotbarDraws(MinecraftClient.getInstance().player) : 9;
	}

	private static final boolean DEBUG = false;
	@Inject(method = "render", at = @At("TAIL"))
	private void debug(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if(DEBUG) {
			for(Slot slot : this.getScreenHandler().slots) {
				if(slot != null) {
					this.textRenderer.draw(matrices, Text.of("" + slot.getIndex()), slot.x + this.x, slot.y + this.y, 0xFFFFFF);
				}
			}
		}
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawBackground(Lnet/minecraft/client/util/math/MatrixStack;FII)V", shift = At.Shift.AFTER))
	private void renderHotbarSection(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		// draw hotbar ourselves with the exact same texture as the HUD because I'm slightly lazy
		RenderSystem.setShaderTexture(0, HandledScreen.BACKGROUND_TEXTURE);

		int x = this.x;
		int y = this.y;
		int height = 166; // default HandledScreen value, TODO: fix this for beacon too cuz im 98% sure it breaks

		int bottom = y + height;

		int draws = getHotbarDraws((ITransplantable) (Objects.requireNonNull(MinecraftClient.getInstance().player)));
		if(draws > 9) {
			this.drawTexture(matrices, x, bottom, 0, height - Constants.HOTBAR_SPACE_IN_INV_SCREEN, this.backgroundWidth, Constants.HOTBAR_SPACE_IN_INV_SCREEN);
		}
	}

	@ModifyConstant(method = "onMouseClick(I)V", constant = @Constant(intValue = 9))
	private int fixHotkey(int constant) {
		return getHotbarDraws((ITransplantable) (Objects.requireNonNull(MinecraftClient.getInstance().player)));
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
		return getHotbarDraws((ITransplantable) (Objects.requireNonNull(MinecraftClient.getInstance().player)));
	}

	@Redirect(method = "isClickOutsideBounds", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;backgroundHeight:I", opcode = Opcodes.GETFIELD))
	private int fixBackgroundHeight(HandledScreen<?> handledScreen) {
		return ((AccessorHandledScreen) handledScreen).getBackgroundHeight() + Constants.HOTBAR_SPACE_IN_INV_SCREEN; // whyyyyyy is code randomly copied instead of calling super
	}
}
