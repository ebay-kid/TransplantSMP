package ml.ikwid.transplantsmp.common.inventory;

import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
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
		boolean bl = this.getIndex() < this.transplantable.getHotbarDraws();
		// TransplantSMP.LOGGER.info("Hotbar slot " + this.getIndex() + " is " + bl);
		return bl;
	}

	/*
	@Override
	public boolean canTakePartial(PlayerEntity player) {
		return this.isEnabled() && super.canTakePartial(player);
	}
	*/
}
