package ml.ikwid.transplantsmp.mixin.stomachtransplant;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.IStomachTransplanted;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class MixinHungerManager implements IStomachTransplanted {
	private ITransplantable transplantable;
	@Unique
	private int maxFoodLevel = 20;

	@ModifyArg(method = "add", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"), index = 1)
	private int increaseMaxHunger(int a) {
		return maxFoodLevel;
	}

	@Override
	public void setMaxFoodLevel(int foodLevel) {
		this.maxFoodLevel = foodLevel;
	}

	@Inject(method = "update", at = @At("HEAD"))
	public void stealPlayerEntity(PlayerEntity player, CallbackInfo ci) {
		this.transplantable = (ITransplantable) player;
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
}
