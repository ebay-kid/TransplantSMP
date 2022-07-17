package ml.ikwid.transplantsmp.common.inventory;

import ml.ikwid.transplantsmp.common.imixins.ISlotTransplanted;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class HotbarSlot extends Slot {
	private final int originalX;
	private boolean isEnabled = false;
	private final ITransplantable transplantable;

	public HotbarSlot(PlayerInventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.transplantable = (ITransplantable) (inventory.player);
		this.originalX = x;
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return this.isEnabled;
	}

	@Override
	public int getMaxItemCount() {
		return this.isEnabled ? super.getMaxItemCount() : 0;
	}

	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}

	@Override
	public boolean canTakePartial(PlayerEntity player) {
		return this.isEnabled && super.canTakePartial(player);
	}

	public void updateEnabledState() {
		this.isEnabled = this.transplantable.getHotbarDraws() < this.getIndex();
		((ISlotTransplanted) this).setX(this.originalX + ((Constants.INNER_SLOT_WIDTH / 2) * (this.transplantable.getHotbarDraws() - 9)));
	}
}
