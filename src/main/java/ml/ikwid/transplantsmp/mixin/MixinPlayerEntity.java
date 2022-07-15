package ml.ikwid.transplantsmp.mixin;

import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ISlotTransplanted;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Utils;
import ml.ikwid.transplantsmp.mixin.skintransplant.MixinScreenHandlerAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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
			if (this.getTransplantType() == TransplantType.ARM_TRANSPLANT) {
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
