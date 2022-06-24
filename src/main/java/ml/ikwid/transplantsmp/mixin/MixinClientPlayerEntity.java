package ml.ikwid.transplantsmp.mixin;

import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity extends MixinPlayerEntity {
	private final ClientPlayerEntity self = (ClientPlayerEntity)(Object) this;
	private final ITransplantable transplantable = (ITransplantable) self;

	@Override
	public int getTransplantedAmount() {
		return TransplantSMPClient.transplants;
	}

	@Override
	public TransplantType getTransplantType() {
		return TransplantSMPClient.transplantType;
	}

	@Override
	public void updateTransplants() {
		// Do nothing because render code accesses MinecraftClient.getInstance().player and uses that
	}
}
