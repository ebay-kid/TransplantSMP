package ml.ikwid.transplantsmp.mixin.player.specifics;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.IStomachTransplanted;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class MixinHungerManager implements IStomachTransplanted {
	@Shadow private int foodLevel;

	@Shadow public abstract void add(int food, float saturationModifier);

	private ITransplantable transplantable;

	@Unique
	private int maxFoodLevel = 20;

	@ModifyArg(method = "add", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"), index = 1)
	private int increaseMaxHunger(int a) {
		return this.maxFoodLevel;
	}

	@Override
	public void setMaxFoodLevel(int foodLevel) {
		this.maxFoodLevel = foodLevel;
	}

	@Inject(method = "update", at = @At("HEAD"))
	public void stealPlayerEntityAndRunChecks(PlayerEntity player, CallbackInfo ci) {
		this.transplantable = (ITransplantable) player;

		if(this.foodLevel > this.maxFoodLevel) {
			this.foodLevel = this.maxFoodLevel;
			this.add(0, 0); // make sure saturation is capped properly
		}
	}

	@ModifyConstant(method = "update", constant = @Constant(intValue = 18))
	public int scaledHealingValue(int original) {
		if(transplantable == null) {
			TransplantSMP.LOGGER.warn("HungerManager failed to steal the transplantable");
			return original; // hopefully never happens.
		}

		if(transplantable.getTransplantType() != TransplantType.STOMACH_TRANSPLANT) {
			return original;
		}
		if(transplantable.getTransplantedAmount() >= 0) {
			return original;
		}
		return (int)(0.9 * (transplantable.getTransplantedAmount() + 20));
	}

	@ModifyConstant(method = "isNotFull", constant = @Constant(intValue = 20, ordinal = 0))
	private int betterNotFull(int constant) {
		return this.maxFoodLevel;
	}
}
