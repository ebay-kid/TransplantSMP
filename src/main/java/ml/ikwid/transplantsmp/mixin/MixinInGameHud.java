package ml.ikwid.transplantsmp.mixin;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Constants;
import ml.ikwid.transplantsmp.common.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
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
	private MinecraftClient client;
	private PlayerEntity playerEntity;
	private final InGameHud self = (InGameHud)(Object) this;

	@Shadow private int scaledWidth;
	@Shadow private int scaledHeight;
	@Shadow private int renderHealthValue;

	@Shadow private int ticks;
	@Shadow @Final private Random random;

	@Shadow protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);

	private ITransplantable transplantable;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void setVars(MinecraftClient client, ItemRenderer itemRenderer, CallbackInfo ci) {
		setVarsAfterInit();
	}

	private void setVarsAfterInit() {
		this.client = MinecraftClient.getInstance();
		this.playerEntity = this.client.player;
		this.transplantable = (ITransplantable)(this.playerEntity);

		TransplantSMP.LOGGER.info("sus resets");
	}

	private LivingEntity riddenEntity() { // too lazy to get an accessor or anything
		PlayerEntity playerEntity = this.cameraPlayer();
		if (playerEntity != null) {
			Entity entity = playerEntity.getVehicle();
			if (entity == null) {
				return null;
			}

			if (entity instanceof LivingEntity) {
				return (LivingEntity) entity;
			}
		}

		return null;
	}

	private PlayerEntity cameraPlayer() { // too lazy to get accessor
		return !(this.client.getCameraEntity() instanceof PlayerEntity) ? null : (PlayerEntity)this.client.getCameraEntity();
	}

	@ModifyConstant(method = "renderStatusBars", constant = @Constant(intValue = 10, ordinal = 4))
	private int armorBars(int constant) {
		if(this.transplantable == null) {
			TransplantSMP.LOGGER.info("transplantable null in armor bars");
			setVarsAfterInit();
			return constant;
		}
		// TransplantSMP.LOGGER.info("armor bars, current = " + transplantable.getTransplantType().toString() + ", " + transplantable.getTransplantedAmount());
		if(this.transplantable.getTransplantType() == TransplantType.SKIN_TRANSPLANT) {
			// TransplantSMP.LOGGER.info("is skin transplant");
			return 10 + Math.min(this.transplantable.getHalvedTransplantedAmount(), 0);
		}
		// TransplantSMP.LOGGER.info("not skin transplant");
		return constant;
	}

	@ModifyConstant(method = "renderStatusBars", constant = @Constant(intValue = 10, ordinal = 5))
	private int hungerBars(int constant) {
		if(this.transplantable == null) {
			TransplantSMP.LOGGER.info("transplantable null in hunger bars");
			setVarsAfterInit();
			return constant;
		}
		// TransplantSMP.LOGGER.info("hunger bars, current = " + transplantable.getTransplantType().toString() + ", " + transplantable.getTransplantedAmount());
		if(this.transplantable.getTransplantType() == TransplantType.STOMACH_TRANSPLANT) {
			// TransplantSMP.LOGGER.info("is stomach transplant");
			return 10 + Math.min(this.transplantable.getHalvedTransplantedAmount(), 0);
		}
		// TransplantSMP.LOGGER.info("not stomach transplant");
		return constant;
	}

	@ModifyConstant(method = "renderStatusBars", constant = @Constant(intValue = 10, ordinal = 6))
	private int airModifier(int constant) { // shift air bubbles if necessary
		if(this.transplantable == null) {
			TransplantSMP.LOGGER.info("transplantable null in air");
			setVarsAfterInit();
			return constant;
		}
		if(this.transplantable.getTransplantType() == TransplantType.STOMACH_TRANSPLANT && this.transplantable.getTransplantedAmount() > 0) {
			return 20;
		}
		return constant;
	}

	@Redirect(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
	private void drawEveryHotbarSlotNeeded(InGameHud instance, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
		if(this.transplantable == null) {
			TransplantSMP.LOGGER.info("transplantable null on draw every slot");
			setVarsAfterInit();
			instance.drawTexture(matrices, x, y, u, v, width, height);
			return;
		}
		int draws = this.transplantable.getHotbarDraws(); // shift to handle the other transplants/vanilla
		x += this.transplantable.xShift();
		for(int i = 0; i < draws; i++) {
			// keep matrices, y, u, v, height as these will be constant (i hope)

			instance.drawTexture(matrices, x, y, u, v, Constants.OUTER_SLOT_WIDTH, height);
			x += Constants.OUTER_SLOT_WIDTH;
		}
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 1)
	private int fixSelectedSlotLocation(int x) {
		if(this.transplantable == null || playerEntity == null) {
			TransplantSMP.LOGGER.info("transplantable or player entity null on fixing selected slot");
			setVarsAfterInit();
			return x;
		}
		int i = this.scaledWidth / 2;
		return (i - 91) + transplantable.xShift() + (Utils.translateSlotToHotbar(this.playerEntity.getInventory().selectedSlot) * Constants.OUTER_SLOT_WIDTH); // mimic the previous shifts
	}

	@ModifyConstant(method = "renderHotbar", constant = @Constant(intValue = 9))
	private int replaceForLoop(int constant) {
		return 0; // skip the original
	}

	@Inject(method = "renderHotbar", at = @At("TAIL"))
	private void renderHotbarItems(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
		if(this.transplantable == null) {
			TransplantSMP.LOGGER.info("transplantable null in render hotbar items");
			setVarsAfterInit();
			return;
		}
		// TransplantSMP.LOGGER.info("rendering hotbar items.");
		int i = this.scaledWidth / 2;

		int o;
		int p = this.scaledHeight - 19;

		int m = 1; // idk wtf this does
		for(int n = 0; n < transplantable.getHotbarDraws(); n++) {
			o = i - 91 + transplantable.xShift() + n * Constants.OUTER_SLOT_WIDTH + 2;

			this.renderHotbarItem(o, p, tickDelta, this.playerEntity, this.playerEntity.getInventory().main.get(Utils.translateHotbarToSlot(n)), m);
		}
	}

	/*
	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 2), index = 1)
	private int rightOffHandShift(int x) {
		if(this.transplantable == null) {
			return x;
		}
		return x + transplantable.xShift();
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 3), index = 1)
	private int leftOffHandShift(int x) {
		if(this.transplantable == null) {
			return x;
		}
		return x - transplantable.xShift(); // not sure about this one
	}
	*/

	@Inject(method = "renderStatusBars", at = @At("TAIL"))
	private void drawMoreBars(MatrixStack matrices, CallbackInfo ci) {
		// TransplantSMP.LOGGER.info("renderStatusBars injected");

		if(this.transplantable == null || this.playerEntity == null) {
			setVarsAfterInit();

			TransplantSMP.LOGGER.info("reeeeeee one of them is null");
			return;
		}
		int transplants = this.transplantable.getHalvedTransplantedAmount();
		TransplantType transplantType = this.transplantable.getTransplantType();
		if(transplantType == TransplantType.HEART_TRANSPLANT || transplantType == TransplantType.ARM_TRANSPLANT || transplants <= 0) {
			// TransplantSMP.LOGGER.info("skipped drawing more bars");
			return;
		}

		boolean armor = (transplantType == TransplantType.SKIN_TRANSPLANT);

		int o = this.scaledHeight - 39; // used either way
		if(armor) { // skin transplant
			// TransplantSMP.LOGGER.info("is skin transplant");
			int x;

			int i = MathHelper.ceil(playerEntity.getHealth());
			int j = this.renderHealthValue;
			int m = this.scaledWidth / 2 - 91;
			float f = Math.max((float) playerEntity.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH), (float) Math.max(j, i));
			int p = MathHelper.ceil(playerEntity.getAbsorptionAmount());
			int q = MathHelper.ceil((f + (float) p) / 2.0F / 10.0F); // shifts up based on how many hearts
			int r = Math.max(10 - (q - 2), 3);
			int s = o - (q - 1) * r - 10; // height stuff which i don't understand
			int u = playerEntity.getArmor() - 20; // shift back by 20 to handle already-drawn textures

			// Absorption doesn't get stacked when the player max health is <= 20 (10 hearts, aka vanilla).
			// So, calculate the shift from 0 absorption -> 4 absorption at 20 health
			// 0 absorption, s = o + 10
			// 4 absorption, s = o + 20
			// so in theory, we can call InGameHud.drawTexture(matrices, x, s + 10, u, v, width, height) with this

			for (int w = 0; w < transplants - 10; w++) { // render however many extra there are
				x = m + w * 8;
				if (w * 2 + 1 < u) {
					self.drawTexture(matrices, x, s + 10, 34, 9, 9, 9);
				}

				if (w * 2 + 1 == u) {
					self.drawTexture(matrices, x, s + 10, 25, 9, 9, 9);
				}

				if (w * 2 + 1 > u) {
					self.drawTexture(matrices, x, s + 10, 16, 9, 9, 9);
				}
			}
		} else { // stomach transplant
			// TransplantSMP.LOGGER.info("is stomach transplant");
			if(this.riddenEntity() != null) {
				return;
			}

			// Hopefully I can straight copy-paste this crap into here and just shift it up
			int k = playerEntity.getHungerManager().getFoodLevel() - 20;

			int n = this.scaledWidth / 2 + 91;
			int y;
			int z;
			int aa;
			int ab;
			int ac;
			for(y = 0; y < transplants - 10; ++y) {
				z = o;
				aa = 16;
				ab = 0;
				if (playerEntity.hasStatusEffect(StatusEffects.HUNGER)) {
					aa += 36;
					ab = 13;
				}

				if (playerEntity.getHungerManager().getSaturationLevel() <= 0.0F && this.ticks % (k * 3 + 1) == 0) {
					z = o + (this.random.nextInt(3) - 1);
				}

				ac = n - y * 8 - 9;
				// why 10? it apparently works for the previous one so YOLO (also it shifts around by 10)
				self.drawTexture(matrices, ac, z + 10, 16 + ab * 9, 27, 9, 9); // wtf why is this different
				if (y * 2 + 1 < k) {
					self.drawTexture(matrices, ac, z + 10, aa + 36, 27, 9, 9);
				}

				if (y * 2 + 1 == k) {
					self.drawTexture(matrices, ac, z + 10, aa + 45, 27, 9, 9);
				}
			}
		}
	}
}
