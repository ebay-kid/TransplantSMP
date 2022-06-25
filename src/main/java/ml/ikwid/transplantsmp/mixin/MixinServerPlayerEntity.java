package ml.ikwid.transplantsmp.mixin;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.IStomachTransplanted;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.networking.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity extends MixinPlayerEntity {
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
			ServerPlayNetworking.send(self, NetworkingConstants.NEEDS_TRANSPLANT, PacketByteBufs.empty());

			TransplantSMP.LOGGER.info("nothing found, needs transplant");
		} else {
			this.setTransplantType(Objects.requireNonNull(TransplantType.get(transplantStored)));
			this.transplanted = nbt.getInt("transplanted");
			this.updateTransplants();

			TransplantSMP.LOGGER.info("found something, doesn't need new transplant");
		}
	}

	@Unique
	@Override
	public void updateTransplants() {
		TransplantSMP.LOGGER.info("update transplants -server");
		switch(this.transplantType) {
			case HEART_TRANSPLANT: // DONE (aka the easiest one)
				EntityAttributeInstance attribute = this.self.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
				attribute.setBaseValue(20 + this.transplanted);
				break;

			case ARM_TRANSPLANT: // Apparently needs nothing so...

			case SKIN_TRANSPLANT:

				break;

			case STOMACH_TRANSPLANT: // DONE (aka the second easiest one)
				IStomachTransplanted hungerMgr = (IStomachTransplanted) (this.self.getHungerManager());
				hungerMgr.setMaxFoodLevel(20 + this.transplanted);
				break;

			default: // REEEEE
				throw new IllegalStateException("Unexpected value: " + transplantType);
		}
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(this.transplanted);
		ServerPlayNetworking.send(this.self, NetworkingConstants.UPDATE_ORGAN_COUNT, buf);
	}

	@Inject(method = "onDeath", at = @At("TAIL"))
	private void transplant(DamageSource damageSource, CallbackInfo ci) {
		this.transplantOrgan(false);
		if(damageSource.getAttacker() instanceof ServerPlayerEntity) {
			((ITransplantable) damageSource.getAttacker()).transplantOrgan(true);
		}
	}

	@Override
	public void setTransplantType(TransplantType transplantType) {
		this.transplantType = transplantType;
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeString(transplantType.toString());
		ServerPlayNetworking.send(self, NetworkingConstants.UPDATE_TRANSPLANT_TYPE, buf);

		this.updateTransplants();
	}
}
