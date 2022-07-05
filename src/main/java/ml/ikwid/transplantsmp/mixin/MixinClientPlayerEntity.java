package ml.ikwid.transplantsmp.mixin;

import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.util.Constants;
import ml.ikwid.transplantsmp.common.util.Utils;
import ml.ikwid.transplantsmp.mixin.skintransplant.MixinPlayerScreenHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity extends MixinPlayerEntity {
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
	public void updateTransplants() {
		switch(this.getTransplantType()) {
			case ARM_TRANSPLANT:
				PlayerScreenHandler playerScreenHandler = this.self.playerScreenHandler;
				DefaultedList<Slot> slots = playerScreenHandler.slots;
				int transplanted = this.getTransplantedAmount();

				for(int i = 0; i < 18; i++) {
					((MixinSlotAccessor)(slots.get(i))).setX(Utils.innerSlotXShift(this.self) + Constants.INNER_SLOT_WIDTH * i);
				}

				break;

			case SKIN_TRANSPLANT:
			case STOMACH_TRANSPLANT:
			case HEART_TRANSPLANT:
				break;
		}
	}
}
