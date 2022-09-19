package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.gamerule.GameruleRegister;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.networking.NetworkingUtil;
import ml.ikwid.transplantsmp.common.util.Utils;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends MixinPlayerEntity {
	private final ServerPlayerEntity self = (ServerPlayerEntity)(Object) this;
	private boolean isSettingTransplant = false;

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeTransplantData(NbtCompound nbt, CallbackInfo ci) {
		nbt.putString("transplantType", this.transplantType.toString());

		nbt.putInt("transplanted", this.transplanted);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readTransplantData(NbtCompound nbt, CallbackInfo ci) {
		String transplantStored = nbt.getString("transplantType");
		if(transplantStored.equals("") || TransplantType.get(transplantStored) == null) {
			this.transplanted = 0;
			this.setTransplantType(null, false); // make sure it's null

			TransplantSMP.LOGGER.info("nothing found, needs transplant");
		} else {
			// Don't do any updates here because we can't send any packets yet.
			// Update in MixinPlayerManager at tail after everything is initialized.
			this.setTransplantedAmount(nbt.getInt("transplanted"), false, false);
			this.setTransplantType(Objects.requireNonNull(TransplantType.get(transplantStored)), false);

			TransplantSMP.LOGGER.info("found something, doesn't need new transplant");
		}
	}

	@Unique
	@Override
	public void updateTransplants(boolean updateCount, boolean updateType) {
		TransplantSMP.LOGGER.info("update transplants -server, amount = " + this.transplanted);

		if(updateType) {
			NetworkingUtil.sendTransplantTypeUpdate(this.getTransplantType().toString(), this.self);
		}

		if(updateCount) {
			NetworkingUtil.sendTransplantCountUpdate(this.self);
		}

		super.updateTransplants(updateCount, updateType);
	}

	@Inject(method = "onDeath", at = @At("TAIL"))
	private void transplant(DamageSource damageSource, CallbackInfo ci) {
		if(Utils.bannableAmount((ITransplantable) (this.self))) {
			Utils.ban(this.self);
			return;
		}

		this.transplantOrgan(false);
		if(damageSource.getAttacker() instanceof ServerPlayerEntity) {
			((ITransplantable) damageSource.getAttacker()).transplantOrgan(true);
		}
	}

	@Inject(method = "copyFrom", at = @At("TAIL"))
	private void copyTransplantData(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		ITransplantable oldTransplantable = (ITransplantable) oldPlayer;

		this.setTransplantedAmount(oldTransplantable.getTransplantedAmount(), false, false);
		this.setTransplantType(oldTransplantable.getTransplantType(), true);

		if(this.getTransplantType() == TransplantType.HEART_TRANSPLANT) {
			self.setHealth(20 + this.getTransplantedAmount()); // make sure heart ppl spawn with full hearts
		} else if(this.getTransplantType() == TransplantType.STOMACH_TRANSPLANT) {
			self.getHungerManager().setFoodLevel(20 + this.getTransplantedAmount()); // make sure stomach ppl spawn with full hunger
		}
	}

	@Override
	public void setIsSettingTransplant(boolean isSettingTransplant) {
		this.isSettingTransplant = isSettingTransplant;
	}

	@Override
	public boolean getIsSettingTransplant() {
		return this.isSettingTransplant;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void balanceArm(CallbackInfo ci) {
		if(this.getTransplantType() == TransplantType.ARM_TRANSPLANT && self.world.getGameRules().getBoolean(GameruleRegister.SHOULD_BALANCE_ARM)) {
			EntityAttributeInstance attributeInstance = this.self.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED);
			if(attributeInstance == null) {
				TransplantSMP.LOGGER.info("attribute instance is null, you're screwed lol");
				return;
			}
			attributeInstance.setBaseValue(attributeInstance.getBaseValue() + this.self.world.getGameRules().get(GameruleRegister.ARM_HASTE_BALANCE_AMOUNT).get());
		}
	}
}
