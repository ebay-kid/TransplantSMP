package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.IHotbarScreenHandler;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.inventory.HotbarSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;

@Unique
@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity implements ITransplantable {
	@Unique
	protected TransplantType transplantType;
	@Unique
	protected int transplanted = 0;

	private final PlayerEntity self = (PlayerEntity)(Object) this;

	@Override
	public int getTransplantedAmount() {
		return this.transplanted;
	}

	@Override
	public void setTransplantedAmount(int organs, boolean updateGeneral, boolean updateAll) {
		int prev = this.getTransplantedAmount();
		this.transplanted = organs;
		if(illegalTransplantAmount()) {
			this.transplanted = prev;
			return;
		}
		this.updateTransplants(updateGeneral, updateAll);
	}

	/**
	 * Re-shifts the hotbar slots to account for the new transplant counts
	 */
	@Override
	public void updateTransplants(boolean updateCount, boolean updateType) {
		if(updateCount) {
			if(this.getTransplantType() == TransplantType.ARM_TRANSPLANT) {
				ScreenHandler screenHandler = this.self.currentScreenHandler;
				ArrayList<HotbarSlot> slots = ((IHotbarScreenHandler) screenHandler).getHotbarSlots();

				for(HotbarSlot slot : slots) {
					slot.updateEnabledState();
				}

				/*
				PlayerScreenHandler screenHandler = this.self.playerScreenHandler;
				DefaultedList<Slot> slots = screenHandler.slots;
				int transplanted = this.getTransplantedAmount();
				int size = slots.size();

				int innerXShift = Utils.innerSlotXShift(this.self);
				int draws = this.getHotbarDraws();
				if (transplanted < size) { // died
					slots.remove(draws);
					((MixinScreenHandlerAccessor) screenHandler).getTrackedStacks().remove(draws);
					((MixinScreenHandlerAccessor) screenHandler).getPreviousTrackedStacks().remove(draws);

					for (int i = 0; i < draws + 1; i++) {
						Slot slot = slots.get(i);
						((ISlotTransplanted) slot).setX(slot.x - Utils.calcSlotXShiftArb(draws + 1) + innerXShift);
					}
				} else if (transplanted > size) { // killed someone
					((MixinScreenHandlerAccessor) screenHandler).addSlotAccess(new Slot(this.self.getInventory(), draws, 8 + draws * 18 + Utils.innerSlotXShift(this.self), slots.get(0).y));
					for (int i = 0; i < draws; i++) {
						Slot slot = slots.get(i);
						((ISlotTransplanted) slot).setX(slot.x - Utils.calcSlotXShiftArb(draws - 1) + innerXShift);
					}
				}
				*/
			}
		}
	}

	@Override
	public TransplantType getTransplantType() {
		return this.transplantType;
	}

	@Override
	public void setTransplantType(TransplantType transplantType, boolean updateGeneral) {
		this.transplantType = transplantType;
		this.updateTransplants(updateGeneral, updateGeneral);
	}
}
