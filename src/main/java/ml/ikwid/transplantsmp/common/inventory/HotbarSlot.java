package ml.ikwid.transplantsmp.common.inventory;

import ml.ikwid.transplantsmp.api.ITransplantable;
import ml.ikwid.transplantsmp.common.transplants.RegisterTransplants;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class HotbarSlot extends Slot {
	private final ITransplantable transplantable;

	public HotbarSlot(PlayerInventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.transplantable = (ITransplantable) (inventory.player);
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return this.isEnabled();
	}

	@Override
	public boolean isEnabled() {
		return this.transplantable.getTransplantType() == RegisterTransplants.ARM_TRANSPLANT ? this.getIndex() < this.transplantable.getTransplantedAmount() + 9 : this.getIndex() < 9;
	}
}
