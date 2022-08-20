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
	@Override
	public boolean isEnabled() {
		return this.getIndex() < this.transplantable.getHotbarDraws();
	}
}
