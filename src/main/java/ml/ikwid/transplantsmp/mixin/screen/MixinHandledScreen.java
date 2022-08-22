package ml.ikwid.transplantsmp.mixin.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {
	@Shadow protected int x;
	@Shadow protected int y;
	@Shadow protected int backgroundHeight;

	@Shadow public abstract T getScreenHandler();

	@Shadow protected int backgroundWidth;

	protected MixinHandledScreen(Text title) {
		super(title);
	}

	private static final boolean DEBUG = true;
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
		int height = 166; // default HandledScreen value

		int bottom = y + height;

		int draws = ((ITransplantable) (Objects.requireNonNull(MinecraftClient.getInstance().player))).getHotbarDraws();
		if(draws > 9) {
			this.drawTexture(matrices, x, bottom, 0, height - Constants.HOTBAR_SPACE_IN_INV_SCREEN, this.backgroundWidth, Constants.HOTBAR_SPACE_IN_INV_SCREEN);
		}
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
	 * Slight side effect of forcing you to use the drop button but idc that's the superior way
	 *
	 * @author 6Times
	 * @reason Allow Arm/Skin transplant users to click outside of bounds
	 */
	@Overwrite
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
		TransplantSMP.LOGGER.info("denied");
		return false;
	}
}
