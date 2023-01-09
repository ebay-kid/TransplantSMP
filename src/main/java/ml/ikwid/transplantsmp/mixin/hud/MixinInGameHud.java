package ml.ikwid.transplantsmp.mixin.hud;

import ml.ikwid.transplantsmp.api.TransplantType;
import ml.ikwid.transplantsmp.api.ITransplantable;
import ml.ikwid.transplantsmp.common.transplants.ArmTransplant;
import ml.ikwid.transplantsmp.common.transplants.RegisterTransplants;
import ml.ikwid.transplantsmp.common.util.Constants;
import ml.ikwid.transplantsmp.common.util.Utils;
import ml.ikwid.transplantsmp.common.util.render.HUDRenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@SuppressWarnings("DuplicatedCode") // no idea how i'd abstract that code
@Mixin(InGameHud.class)
public abstract class MixinInGameHud {
	@Shadow private int scaledWidth;
	@Shadow private int scaledHeight;
	@Shadow private int renderHealthValue;

	@Shadow private int ticks;
	@Shadow @Final private Random random;

	@Shadow protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);

	@Shadow protected abstract LivingEntity getRiddenEntity();

	@Shadow @Final private MinecraftClient client;

	@ModifyConstant(method = "renderStatusBars", constant = @Constant(intValue = 10, ordinal = 4))
	private int armorBars(int constant) {
		if(this.client.player == null) {
			return constant;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		// TransplantSMP.LOGGER.info("armor bars, current = " + transplantable.getTransplantType().toString() + ", " + transplantable.getTransplantedAmount());
		
		if(transplantable.getTransplantType() == RegisterTransplants.SKIN_TRANSPLANT) {
			// TransplantSMP.LOGGER.info("is skin transplant");
			return 10 + Math.min(transplantable.getRawTransplantedAmount(), 0);
		}
		// TransplantSMP.LOGGER.info("not skin transplant");
		return constant;
	}

	@ModifyConstant(method = "renderStatusBars", constant = @Constant(intValue = 10, ordinal = 5))
	private int hungerBars(int constant) {
		if(this.client.player == null) {
			return constant;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		// TransplantSMP.LOGGER.info("hunger bars, current = " + transplantable.getTransplantType().toString() + ", " + transplantable.getTransplantedAmount());
		
		if(transplantable.getTransplantType() == RegisterTransplants.STOMACH_TRANSPLANT) {
			// TransplantSMP.LOGGER.info("is stomach transplant");
			return 10 + Math.min(transplantable.getRawTransplantedAmount(), 0);
		}
		// TransplantSMP.LOGGER.info("not stomach transplant");
		return constant;
	}

	@ModifyConstant(method = "renderStatusBars", constant = @Constant(intValue = 10, ordinal = 6))
	private int airModifier(int constant) { // shift air bubbles if necessary
		if(this.client.player == null) {
			return constant;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		
		if(transplantable.getTransplantType() == RegisterTransplants.STOMACH_TRANSPLANT && transplantable.getTransplantedAmount() > 0) {
			// TransplantSMP.LOGGER.info("shifted air");
			return 0;
		}
		return constant;
	}

	@Redirect(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
	private void drawEveryHotbarSlotNeeded(InGameHud instance, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
		if(this.client.player == null) {
			instance.drawTexture(matrices, x, y, u, v, width, height);
			return;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		if(transplantable.getTransplantType() != RegisterTransplants.ARM_TRANSPLANT) {
			return;
		}

		ArmTransplant armTransplant = (ArmTransplant) (transplantable.getTransplantType());
		int draws = armTransplant.getHotbarDraws(this.client.player); // shift to handle the other transplants/vanilla
		x += armTransplant.xShift(this.client.player);

		for(int i = 0; i < draws; i++) {
			// keep matrices, y, u, v, height as these will be constant (i hope)

			instance.drawTexture(matrices, x, y, u, v, Constants.OUTER_SLOT_WIDTH, height);
			x += Constants.OUTER_SLOT_WIDTH - 1;
		}
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 1)
	private int fixSelectedSlotLocation(int x) {
		if(this.client.player == null) {
			return x;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		if(transplantable.getTransplantType() != RegisterTransplants.ARM_TRANSPLANT) {
			return x;
		}

		ArmTransplant armTransplant = (ArmTransplant) (transplantable.getTransplantType());
		int i = this.scaledWidth / 2;

		return (i - 92) + armTransplant.xShift(this.client.player) + (Utils.translateSlotToHotbar(this.client.player.getInventory().selectedSlot) * (Constants.OUTER_SLOT_WIDTH - 1)); // mimic the previous shifts
	}

	@ModifyConstant(method = "renderHotbar", constant = @Constant(intValue = 9))
	private int replaceForLoop(int constant) {
		return ((ITransplantable) (Objects.requireNonNull(this.client.player))).getTransplantType() == RegisterTransplants.ARM_TRANSPLANT ? 0 : 9; // skip the original
	}

	@Inject(method = "renderHotbar", at = @At("TAIL"))
	private void renderHotbarItems(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
		if(this.client.player == null) {
			return;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		if(transplantable.getTransplantType() != RegisterTransplants.ARM_TRANSPLANT) {
			return;
		}

		ArmTransplant armTransplant = (ArmTransplant) (transplantable.getTransplantType());
		// TransplantSMP.LOGGER.info("rendering hotbar items.");
		int i = this.scaledWidth / 2;

		int o;
		int p = this.scaledHeight - 19;

		int m = 1; // idk wtf this does
		
		for(int n = 0; n < armTransplant.getHotbarDraws(this.client.player); n++) {
			o = i - 91 + armTransplant.xShift(this.client.player) + n * (Constants.OUTER_SLOT_WIDTH - 1) + 3;

			this.renderHotbarItem(o, p, tickDelta, this.client.player, this.client.player.getInventory().main.get(Utils.translateHotbarToSlot(n)), m);
		}
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 2), index = 1)
	private int rightOffHandShift(int x) {
		if(this.client.player == null) {
			return x;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		if(transplantable.getTransplantType() != RegisterTransplants.ARM_TRANSPLANT) {
			return x;
		}

		ArmTransplant armTransplant = (ArmTransplant) (transplantable.getTransplantType());
		return x + armTransplant.xShift(this.client.player);
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 3), index = 1)
	private int leftOffHandShift(int x) {
		return xShifter(x);
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V", ordinal = 1), index = 0)
	private int leftOffHandShift2(int x) {
		if(this.client.player == null) {
			return x;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		if(transplantable.getTransplantType() != RegisterTransplants.ARM_TRANSPLANT) {
			return x;
		}

		ArmTransplant armTransplant = (ArmTransplant) (transplantable.getTransplantType());
		return x + armTransplant.xShift(this.client.player);
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V", ordinal = 2), index = 0)
	private int rightOffHandShift2(int x) {
		return xShifter(x);
	}

	private int xShifter(int x) {
		if(this.client.player == null) {
			return x;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		if(transplantable.getTransplantType() != RegisterTransplants.ARM_TRANSPLANT) {
			return x;
		}

		ArmTransplant armTransplant = (ArmTransplant) (transplantable.getTransplantType());
		return x - armTransplant.xShift(this.client.player);
	}

	@ModifyConstant(method = "renderHotbar", constant = @Constant(intValue = 6, ordinal = 0))
	private int leftAttackIndShift(int constant) {
		return xShifter(constant);
	}

	@ModifyConstant(method = "renderHotbar", constant = @Constant(intValue = 22, ordinal = 7))
	private int rightAttackIndShift(int constant) {
		return xShifter(constant);
	}

	@Inject(method = "renderStatusBars", at = @At("HEAD"))
	private void drawMoreBars(MatrixStack matrices, CallbackInfo ci) {
		if(this.client.player == null) {
			return;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		
		int transplants = transplantable.getRawTransplantedAmount();
		TransplantType transplantType = transplantable.getTransplantType();
		if((transplantType == RegisterTransplants.HEART_TRANSPLANT || transplantType == RegisterTransplants.ARM_TRANSPLANT) || transplants <= 0) {
			return;
		}

		boolean armor = (transplantType == RegisterTransplants.SKIN_TRANSPLANT);

		// int o = this.scaledHeight - 39; // used either way
		if(armor) {
			HUDRenderUtil.renderArmorBars(matrices, this.scaledHeight, this.renderHealthValue, this.scaledWidth, transplants);
		} else {
			HUDRenderUtil.renderHungerBars(matrices, this.getRiddenEntity(), this.scaledHeight, this.scaledWidth, transplants, this.ticks, this.random);
		}
	}
}
