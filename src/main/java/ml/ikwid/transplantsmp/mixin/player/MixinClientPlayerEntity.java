package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.common.TransplantType;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends MixinPlayerEntity {
	private final ClientPlayerEntity self = (ClientPlayerEntity)(Object) this;

	@Override
	public int getTransplantedAmount() {
		return TransplantSMPClient.transplants;
	}

	@Override
	public TransplantType getTransplantType() {
		return TransplantSMPClient.transplantType;
	}

	@Override
	public void updateTransplants(boolean updateCount, boolean updateType) {
		if(updateCount) {
			switch (this.getTransplantType()) {
				case ARM_TRANSPLANT:

					break;

				case SKIN_TRANSPLANT:
				case STOMACH_TRANSPLANT:
				case HEART_TRANSPLANT:
					break;
			}
		}
		super.updateTransplants(updateCount, updateType);
	}
}
