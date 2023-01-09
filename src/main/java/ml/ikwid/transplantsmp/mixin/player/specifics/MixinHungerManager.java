package ml.ikwid.transplantsmp.mixin.player.specifics;

import ml.ikwid.transplantsmp.api.ITransplantable;
import ml.ikwid.transplantsmp.common.imixins.IStomachTransplanted;
import ml.ikwid.transplantsmp.common.transplants.RegisterTransplants;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class MixinHungerManager implements IStomachTransplanted {
	@Shadow private int foodLevel;

	@Shadow public abstract void add(int food, float saturationModifier);

	private ITransplantable transplantable;

	@Unique
	private int maxFoodLevel = 20;

	@ModifyArg(method = "add", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I", ordinal = 0), index = 1)
	private int increaseMaxHunger(int a) {
		// TransplantSMP.LOGGER.info("increaseMaxHunger called, max hunger: " + this.maxFoodLevel);
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
		if(this.transplantable == null || this.transplantable.getTransplantType() != RegisterTransplants.STOMACH_TRANSPLANT || this.transplantable.getTransplantedAmount() >= 0) {
			return original; // hopefully never happens.
		}

		return (int)(0.9 * (transplantable.getTransplantedAmount() + 20));
	}

	/**
	 * @author 6Times
	 * @reason idk what im doing but this works so idc
	 */
	@Overwrite
	public boolean isNotFull() {
		return this.foodLevel < this.maxFoodLevel;
	}
}
