package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.api.TransplantType;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends MixinPlayerEntity {
	@Unique
	private final ClientPlayerEntity self = (ClientPlayerEntity) (Object) this;

	@Override
	public int getTransplantedAmount() {
		return TransplantSMPClient.transplanted;
	}

	@Override
	protected void setTransplantedAmount(int organs) {
		super.setTransplantedAmount(organs);
		TransplantSMPClient.transplanted = organs;
	}

	@Override
	public TransplantType getTransplantType() {
		return TransplantSMPClient.transplantType;
	}

	@Override
	public void setTransplantType(TransplantType transplantType) {
		super.setTransplantType(transplantType);
		TransplantSMPClient.transplantType = transplantType;
	}

	@Override
	public void updateTransplants(boolean updateCount, boolean updateType, TransplantType prevType, int prevAmt, int newAmt) {
		if(updateType) {
			prevType.resetTransplantClient(self);
		}

		if(updateCount) {
			this.transplantType.updateCountClient(self, prevAmt, newAmt);
		}
	}
}
