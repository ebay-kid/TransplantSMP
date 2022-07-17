package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.IStomachTransplanted;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.networking.NetworkingUtil;
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

		if(updateCount) {
			switch (this.transplantType) {
				case HEART_TRANSPLANT: // DONE (aka the easiest one)
					EntityAttributeInstance attribute = this.self.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
					if (attribute == null) {
						TransplantSMP.LOGGER.warn("attribute shouldn't be null uhhh you're kinda screwed");
						return;
					}
					attribute.setBaseValue(20 + this.transplanted);
					break;

				case ARM_TRANSPLANT: // Apparently needs nothing so...

				case SKIN_TRANSPLANT:

					break;

				case STOMACH_TRANSPLANT: // DONE (aka the second easiest one)
					IStomachTransplanted hungerMgr = (IStomachTransplanted) (this.self.getHungerManager());
					hungerMgr.setMaxFoodLevel(20 + this.transplanted);
					break;
			}

			NetworkingUtil.sendTransplantCountUpdate(this.self);
		}

		if(updateType) {
			NetworkingUtil.sendTransplantTypeUpdate(this.getTransplantType().toString(), this.self);
		}

		super.updateTransplants(updateCount, updateType);
	}

	@Inject(method = "onDeath", at = @At("TAIL"))
	private void transplant(DamageSource damageSource, CallbackInfo ci) {
		this.transplantOrgan(false);
		if(damageSource.getAttacker() instanceof ServerPlayerEntity) {
			((ITransplantable) damageSource.getAttacker()).transplantOrgan(true);
		}
	}

	@Override
	public void setTransplantType(TransplantType transplantType, boolean updateGeneral) {
		this.transplantType = transplantType;
		this.updateTransplants(updateGeneral, updateGeneral);
	}

	@Inject(method = "copyFrom", at = @At("TAIL"))
	private void copyTransplantData(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		ITransplantable oldTransplantable = (ITransplantable) oldPlayer;

		this.setTransplantedAmount(oldTransplantable.getTransplantedAmount(), false, false);
		this.setTransplantType(oldTransplantable.getTransplantType(), true);

		if(this.getTransplantType() == TransplantType.HEART_TRANSPLANT) {
			self.setHealth(20 + this.getTransplantedAmount()); // make sure heart ppl spawn with full hearts
		}
	}
}
