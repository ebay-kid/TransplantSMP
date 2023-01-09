package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.api.TransplantType;
import ml.ikwid.transplantsmp.api.TransplantTypes;
import ml.ikwid.transplantsmp.api.ITransplantable;
import ml.ikwid.transplantsmp.common.networking.ServerNetworkingUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
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
		if(!this.isSettingTransplant) {
			nbt.putString("transplantType", this.transplantType.toString());
			nbt.putInt("transplanted", this.transplanted);
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readTransplantData(NbtCompound nbt, CallbackInfo ci) {
		String transplantStored = nbt.getString("transplantType");
		if(transplantStored.equals("") || TransplantTypes.get(transplantStored) == null) {
			this.transplanted = 0;
			this.setTransplantType(null, false); // make sure it's null

			TransplantSMP.LOGGER.info("nothing found, needs transplant");
		} else {
			// Don't do any updates here because we can't send any packets yet.
			// Update in MixinPlayerManager at tail after everything is initialized.
			this.setTransplantType(Objects.requireNonNull(TransplantTypes.get(transplantStored)), false);
			this.setTransplantedAmount(nbt.getInt("transplanted"), false);

			TransplantSMP.LOGGER.info("found something, doesn't need new transplant");
		}
	}

	@Override
	public void updateTransplants(boolean updateCount, boolean updateType, TransplantType prevType, int prevAmt, int newAmt) {
		if(updateType) {
			if(prevType != null) {
				prevType.onResetTransplantServer(self);
			}
			ServerNetworkingUtil.sendTransplantTypeUpdate(this.getTransplantType().toString(), this.self);
		}

		if(updateCount) {
			this.transplantType.onUpdateCountServer(self, prevAmt, newAmt);
			ServerNetworkingUtil.sendTransplantCountUpdate(this.self);
		}
	}

	@Inject(method = "onDeath", at = @At("TAIL"))
	private void transplant(DamageSource damageSource, CallbackInfo ci) {
		this.transplantOrgan(false);
		Entity entity;
		if((entity = damageSource.getAttacker()) != null && entity.isPlayer()) { // should be changed to capture the death message translatable text, TODO when can test
			this.transplantOrgan(true);
		}
	}

	@Inject(method = "copyFrom", at = @At("TAIL"))
	private void copyTransplantData(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		if(!alive) {
			ITransplantable oldTransplantable = (ITransplantable) oldPlayer;

			this.setTransplantedAmount(oldTransplantable.getTransplantedAmount(), false);
			this.setTransplantType(oldTransplantable.getTransplantType(), true);

			this.transplantType.onPlayerRespawn(self, this.getTransplantedAmount());
		} else {
			iHateClientSideServerSideDesyncWithTheHungerManagerPleaseFixMojangThankYouIDontWantToKeepSendingPacketEveryTick(null);
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

	// @Inject(method = "tick", at = @At("TAIL"))
	private void iHateClientSideServerSideDesyncWithTheHungerManagerPleaseFixMojangThankYouIDontWantToKeepSendingPacketEveryTick(CallbackInfo ci) {
		ServerNetworkingUtil.sendTransplantCountUpdate(this.self);
	}
}
