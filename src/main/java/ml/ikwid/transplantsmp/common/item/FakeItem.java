package ml.ikwid.transplantsmp.common.item;

import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FakeItem extends Item {
	public FakeItem() {
		super(new FabricItemSettings().maxCount(1));
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(entity instanceof PlayerEntity player) {
			ITransplantable transplantable = (ITransplantable) player;

			if(transplantable.getTransplantType() != TransplantType.ARM_TRANSPLANT) {
				return;
			}
			int transplants = transplantable.getTransplantedAmount();
			if(transplants < 9 || transplants > 40) {
				if(transplants >= slot) {
					remove(player, slot);
				}
			}
		}
	}

	private void remove(PlayerEntity player, int slot) {
		player.getInventory().setStack(slot, ItemStack.EMPTY);
	}
}
