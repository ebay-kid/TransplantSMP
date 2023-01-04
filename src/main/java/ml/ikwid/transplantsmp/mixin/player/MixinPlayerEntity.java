package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.api.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Unique
@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity implements ITransplantable {
	@Unique
	protected TransplantType transplantType;
	@Unique
	protected int transplanted = 0;

	@Override
	public int getTransplantedAmount() {
		return (this.transplanted * this.transplantType.getDefaultChangeByAmount()) / 2;
	}

	@Override
	public void setTransplantedAmount(int organs, boolean updateCount, boolean updateType) {
		if(!this.transplantType.canTransplant(organs)) {
			TransplantSMP.LOGGER.info("illegal amount of " + organs);
			return;
		}
		int prev = this.getTransplantedAmount();
		this.setTransplantedAmount(organs);
		this.updateTransplants(updateCount, updateType, this.getTransplantType(), prev, organs);
	}

	@Override
	public TransplantType getTransplantType() {
		return this.transplantType;
	}

	@Override
	public void setTransplantType(TransplantType transplantType, boolean updateType) {
		TransplantType prev = this.getTransplantType();
		this.setTransplantType(transplantType);
		this.updateTransplants(updateType, updateType, prev, this.getTransplantedAmount(), this.getTransplantedAmount());
	}

	protected void setTransplantedAmount(int organs) {
		this.transplanted = organs;
	}

	protected void setTransplantType(TransplantType transplantType) {
		this.transplantType = transplantType;
	}

	/*
	@Environment(EnvType.CLIENT)
	@Redirect(method = "getAttackCooldownProgressPerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
	private double balanceArmClient(PlayerEntity instance, EntityAttribute entityAttribute) {
		double attrVal = instance.getAttributeValue(entityAttribute);
		if(TransplantSMPClient.balanceArm && this.getTransplantType() == TransplantType.ARM_TRANSPLANT) {
			attrVal += ((1.0d + this.getTransplantedAmount()) * TransplantSMPClient.armHasteBalanceAmount / 20d);
		}
		return attrVal;
	}

	@Environment(EnvType.SERVER)
	@Redirect(method = "getAttackCooldownProgressPerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
	private double balanceArmServer(PlayerEntity instance, EntityAttribute entityAttribute) {
		double attrVal = instance.getAttributeValue(entityAttribute);
		if(Objects.requireNonNull(self.getServer()).getOverworld().getGameRules().getBoolean(GameruleRegister.SHOULD_BALANCE_ARM) && this.getTransplantType() == TransplantType.ARM_TRANSPLANT) {
			attrVal += ((1.0d + this.getTransplantedAmount()) * self.getServer().getOverworld().getGameRules().get(GameruleRegister.ARM_HASTE_BALANCE_AMOUNT).get() / 20d);
		}
		return attrVal;
	}
	 */
}
