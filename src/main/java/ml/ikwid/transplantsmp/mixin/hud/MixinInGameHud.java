package ml.ikwid.transplantsmp.mixin.hud;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Constants;
import ml.ikwid.transplantsmp.common.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {
	private final InGameHud self = (InGameHud)(Object) this;

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
			TransplantSMP.LOGGER.info("this.client.player in armor bars");
			return constant;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		// TransplantSMP.LOGGER.info("armor bars, current = " + transplantable.getTransplantType().toString() + ", " + transplantable.getTransplantedAmount());
		
		if(transplantable.getTransplantType() == TransplantType.SKIN_TRANSPLANT) {
			// TransplantSMP.LOGGER.info("is skin transplant");
			return 10 + Math.min(transplantable.getHalvedTransplantedAmount(), 0);
		}
		// TransplantSMP.LOGGER.info("not skin transplant");
		return constant;
	}

	@ModifyConstant(method = "renderStatusBars", constant = @Constant(intValue = 10, ordinal = 5))
	private int hungerBars(int constant) {
		if(this.client.player == null) {
			TransplantSMP.LOGGER.info("this.client.player in hunger bars");
			return constant;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		// TransplantSMP.LOGGER.info("hunger bars, current = " + transplantable.getTransplantType().toString() + ", " + transplantable.getTransplantedAmount());
		
		if(transplantable.getTransplantType() == TransplantType.STOMACH_TRANSPLANT) {
			// TransplantSMP.LOGGER.info("is stomach transplant");
			return 10 + Math.min(transplantable.getHalvedTransplantedAmount(), 0);
		}
		// TransplantSMP.LOGGER.info("not stomach transplant");
		return constant;
	}

	@ModifyConstant(method = "renderStatusBars", constant = @Constant(intValue = 10, ordinal = 6))
	private int airModifier(int constant) { // shift air bubbles if necessary
		if(this.client.player == null) {
			TransplantSMP.LOGGER.info("this.client.player in air");
			return constant;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		
		if(transplantable.getTransplantType() == TransplantType.STOMACH_TRANSPLANT && transplantable.getTransplantedAmount() > 0) {
			// TransplantSMP.LOGGER.info("shifted air");
			return 0;
		}
		return constant;
	}

	@Redirect(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
	private void drawEveryHotbarSlotNeeded(InGameHud instance, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
		if(this.client.player == null) {
			// TransplantSMP.LOGGER.info("this.client.player in draw every slot");
			instance.drawTexture(matrices, x, y, u, v, width, height);
			return;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		
		int draws = transplantable.getHotbarDraws(); // shift to handle the other transplants/vanilla
		x += transplantable.xShift();
		for(int i = 0; i < draws; i++) {
			// keep matrices, y, u, v, height as these will be constant (i hope)

			instance.drawTexture(matrices, x, y, u, v, Constants.OUTER_SLOT_WIDTH - 1, height);
			x += (Constants.OUTER_SLOT_WIDTH - 1);
		}
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 1)
	private int fixSelectedSlotLocation(int x) {
		if(this.client.player == null) {
			TransplantSMP.LOGGER.info("this.client.player on fixing selected slot");
			return x;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		
		int i = this.scaledWidth / 2;
		return (i - 91) + transplantable.xShift() + (Utils.translateSlotToHotbar(this.client.player.getInventory().selectedSlot) * (Constants.OUTER_SLOT_WIDTH - 1)); // mimic the previous shifts
	}

	@ModifyConstant(method = "renderHotbar", constant = @Constant(intValue = 9))
	private int replaceForLoop(int constant) {
		return 0; // skip the original
	}

	@Inject(method = "renderHotbar", at = @At("TAIL"))
	private void renderHotbarItems(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
		if(this.client.player == null) {
			TransplantSMP.LOGGER.info("this.client.player in render hotbar items");
			return;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		// TransplantSMP.LOGGER.info("rendering hotbar items.");
		int i = this.scaledWidth / 2;

		int o;
		int p = this.scaledHeight - 19;

		int m = 1; // idk wtf this does
		
		for(int n = 0; n < transplantable.getHotbarDraws(); n++) {
			o = i - 91 + transplantable.xShift() + n * (Constants.OUTER_SLOT_WIDTH - 1) + 3;

			this.renderHotbarItem(o, p, tickDelta, this.client.player, this.client.player.getInventory().main.get(Utils.translateHotbarToSlot(n)), m);
		}
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 2), index = 1)
	private int rightOffHandShift(int x) {
		if(this.client.player == null) {
			return x;
		}
		return x + ((ITransplantable)(this.client.player)).xShift();
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 3), index = 1)
	private int leftOffHandShift(int x) {
		if(this.client.player == null) {
			return x;
		}
		return x - ((ITransplantable)(this.client.player)).xShift(); // not sure about this one
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V", ordinal = 1), index = 0)
	private int leftOffHandShift2(int x) {
		if(this.client.player == null) {
			return x;
		}
		return x + ((ITransplantable)(this.client.player)).xShift();
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V", ordinal = 2), index = 0)
	private int rightOffHandShift2(int x) {
		if(this.client.player == null) {
			return x;
		}
		return x - ((ITransplantable)(this.client.player)).xShift();
	}

	@ModifyConstant(method = "renderHotbar", constant = @Constant(intValue = 6, ordinal = 0))
	private int leftAttackIndShift(int constant) {
		if(this.client.player == null) {
			return constant;
		}
		return constant - ((ITransplantable)(this.client.player)).xShift();
	}

	@ModifyConstant(method = "renderHotbar", constant = @Constant(intValue = 22, ordinal = 7))
	private int rightAttackIndShift(int constant) {
		if(this.client.player == null) {
			return constant;
		}
		return constant - ((ITransplantable)(this.client.player)).xShift();
	}

	@Inject(method = "renderStatusBars", at = @At("TAIL"))
	private void drawMoreBars(MatrixStack matrices, CallbackInfo ci) { // moof code
		// TransplantSMP.LOGGER.info("renderStatusBars injected");

		if(this.client.player == null) {
			TransplantSMP.LOGGER.info("this.client.player in draw more bars");
			return;
		}
		ITransplantable transplantable = (ITransplantable) (this.client.player);
		
		int transplants = transplantable.getHalvedTransplantedAmount();
		TransplantType transplantType = transplantable.getTransplantType();
		if(transplantType == TransplantType.HEART_TRANSPLANT || transplantType == TransplantType.ARM_TRANSPLANT || transplants <= 0) {
			TransplantSMP.LOGGER.info("skipped drawing more bars");
			return;
		}

		boolean armor = (transplantType == TransplantType.SKIN_TRANSPLANT);

		int o = this.scaledHeight - 39; // used either way
		if(armor) { // skin transplant
			int x;

			int i = MathHelper.ceil(this.client.player.getHealth());
			int j = this.renderHealthValue;
			int m = this.scaledWidth / 2 - 91;
			float f = Math.max((float) this.client.player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH), (float) Math.max(j, i));
			int p = MathHelper.ceil(this.client.player.getAbsorptionAmount());
			int q = MathHelper.ceil((f + (float) p) / 2.0F / 10.0F); // shifts up based on how many hearts
			int r = Math.max(10 - (q - 2), 3);
			int s = o - (q - 1) * r - 10; // height stuff which i don't understand
			int u = this.client.player.getArmor() - 20; // shift back by 20 to handle already-drawn textures

			if(this.client.player.getArmor() == 0) {
				return;
			}

			// Absorption doesn't get stacked when the player max health is <= 20 (10 hearts, aka vanilla).
			// So, calculate the shift from 0 absorption -> 4 absorption at 20 health
			// 0 absorption, s = o + 10
			// 4 absorption, s = o + 20
			// so in theory, we can call InGameHud.drawTexture(matrices, x, s + 10, u, v, width, height) with this

			for (int w = 0; w < transplants; w++) { // render however many extra there are
				x = m + w * 8;
				if (w * 2 + 1 < u) {
					self.drawTexture(matrices, x, s - 10, 34, 9, 9, 9);
				}

				if (w * 2 + 1 == u) {
					self.drawTexture(matrices, x, s - 10, 25, 9, 9, 9);
				}

				if (w * 2 + 1 > u) {
					self.drawTexture(matrices, x, s - 10, 16, 9, 9, 9);
				}
			}
		} else { // stomach transplant
			if(this.getRiddenEntity() != null) {
				return;
			}

			// Hopefully I can straight copy-paste this crap into here and just shift it up
			int k = this.client.player.getHungerManager().getFoodLevel() - 20;

			int n = this.scaledWidth / 2 + 91;
			int y;
			int z;
			int aa;
			int ab;
			int ac;
			for(y = 0; y < transplants; ++y) {
				z = o;
				aa = 16;
				ab = 0;
				if (this.client.player.hasStatusEffect(StatusEffects.HUNGER)) {
					aa += 36;
					ab = 13;
				}

				if (this.client.player.getHungerManager().getSaturationLevel() <= 0.0F && this.ticks % (k * 3 + 1) == 0) {
					z = o + (this.random.nextInt(3) - 1);
				}

				ac = n - y * 8 - 9;
				// why 10? it apparently works for the previous one so YOLO (also it shifts around by 10)
				self.drawTexture(matrices, ac, z - 10, 16 + ab * 9, 27, 9, 9); // wtf why is this different
				if (y * 2 + 1 < k) {
					self.drawTexture(matrices, ac, z - 10, aa + 36, 27, 9, 9);
				}

				if (y * 2 + 1 == k) {
					self.drawTexture(matrices, ac, z - 10, aa + 45, 27, 9, 9);
				}
			}
		}
	}
}
