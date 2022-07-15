package ml.ikwid.transplantsmp.mixin;

import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ISlotTransplanted;
import ml.ikwid.transplantsmp.common.util.Constants;
import ml.ikwid.transplantsmp.common.util.Utils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
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
					PlayerScreenHandler playerScreenHandler = this.self.playerScreenHandler;
					DefaultedList<Slot> slots = playerScreenHandler.slots;

					for (int i = 0; i < 18; i++) {
						int slotWithIndex = Utils.mapSlotIndexToID(i);
						if(slotWithIndex == -1) { // no slot found

						}
						((ISlotTransplanted)(slots.get(slotWithIndex))).setX(Utils.innerSlotXShift(this.self) + Constants.INNER_SLOT_WIDTH * i);
					}

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
