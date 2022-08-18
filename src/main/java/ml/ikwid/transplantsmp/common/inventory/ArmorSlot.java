package ml.ikwid.transplantsmp.common.inventory;

import com.mojang.datafixers.util.Pair;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Utils;
import ml.ikwid.transplantsmp.mixin.AccessorPlayerScreenHandler;
import ml.ikwid.transplantsmp.mixin.screen.MixinPlayerScreenHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class ArmorSlot extends Slot {
	private final PlayerEntity owner;
	private final ITransplantable transplantable;
	private final EquipmentSlot equipmentSlot;

	public ArmorSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.owner = ((PlayerInventory)(inventory)).player;
		this.transplantable = (ITransplantable)(this.owner);
		this.equipmentSlot = AccessorPlayerScreenHandler.getEquipmentSlotOrder()[(index <= 39 ? 39 : 48) - index];
	}

	@Override
	public void setStack(ItemStack stack) {
		ItemStack itemStack = this.getStack();
		super.setStack(stack);
		owner.onEquipStack(this.equipmentSlot, itemStack, stack);
	}

	@Override
	public int getMaxItemCount() {
		return /*this.isEnabled() ? 1 : 0;*/ 1;
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return equipmentSlot == MobEntity.getPreferredEquipmentSlot(stack) /*&& this.isEnabled()*/;
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerEntity) {
		ItemStack itemStack = this.getStack();
		if (!itemStack.isEmpty() && !playerEntity.isCreative() && EnchantmentHelper.hasBindingCurse(itemStack)) {
			return false;
		}
		return super.canTakeItems(playerEntity) /*&& this.isEnabled()*/;
	}

	@Override
	public Pair<Identifier, Identifier> getBackgroundSprite() {
		return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AccessorPlayerScreenHandler.getEmptyArmorSlotTextures()[this.equipmentSlot.getEntitySlotId()]);
	}

	@Override
	public boolean isEnabled() {
		boolean bl = true;
		if(this.getIndex() > 39) {
			bl = transplantable.getTransplantType() == TransplantType.SKIN_TRANSPLANT;
		}
		return bl;
	}
}
