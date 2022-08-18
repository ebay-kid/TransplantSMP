package ml.ikwid.transplantsmp.common.inventory;

import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;

public class HotbarSlot extends Slot {
	private final ITransplantable transplantable;

	public HotbarSlot(PlayerInventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.transplantable = (ITransplantable) (inventory.player);
	}

	/*
	@Override
	public boolean canInsert(ItemStack stack) {
		return this.isEnabled();
	}

	@Override
	public int getMaxItemCount() {
		return this.isEnabled() ? super.getMaxItemCount() : 0;
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerEntity) {
		return this.isEnabled();
	}
	*/
	@Override
	public boolean isEnabled() {
		return this.getIndex() < this.transplantable.getHotbarDraws();
	}

	/*
	@Override
	public boolean canTakePartial(PlayerEntity player) {
		return this.isEnabled() && super.canTakePartial(player);
	}
	*/
}
