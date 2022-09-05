package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.common.TransplantType;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends MixinPlayerEntity {
	@Override
	public int getTransplantedAmount() {
		return TransplantSMPClient.transplanted;
	}

	@Override
	public void setTransplantedAmount(int organs, boolean updateCount, boolean updateType) {
		int prev = this.getTransplantedAmount();
		TransplantSMPClient.transplanted = organs;
		if(illegalTransplantAmount()) {
			TransplantSMPClient.transplanted = prev;
			TransplantSMP.LOGGER.info("illegal amount of " + organs);
			return;
		}
		this.updateTransplants(updateCount, updateType);
	}

	@Override
	public TransplantType getTransplantType() {
		return TransplantSMPClient.transplantType;
	}

	@Override
	public void setTransplantType(TransplantType transplantType, boolean updateType) {
		TransplantSMPClient.transplantType = transplantType;
		this.updateTransplants(updateType, updateType);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public void updateTransplants(boolean updateCount, boolean updateType) {
		if(updateType) {
		}

		if(updateCount) {
			switch (this.getTransplantType()) {
				case ARM_TRANSPLANT:
				case SKIN_TRANSPLANT:
				case STOMACH_TRANSPLANT:
				case HEART_TRANSPLANT:

					break;
			}
		}
		super.updateTransplants(updateCount, updateType);
	}
}
