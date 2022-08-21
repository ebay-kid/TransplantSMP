package ml.ikwid.transplantsmp.common.inventory;

import com.mojang.datafixers.util.Pair;
import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Constants;
import ml.ikwid.transplantsmp.mixin.AccessorPlayerScreenHandler;
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

		// 45, 46, 47, 48 <- new, 49, 50, 51, 52 <- vanilla, 53 <- offhand
		boolean isExtraArmor = index <= Constants.EXTRA_ARMOR_START_LOC + 3;
		TransplantSMP.LOGGER.info("index: " + index + ", isExtraArmor: " + isExtraArmor);
		int subtractIndex = (isExtraArmor ? Constants.EXTRA_ARMOR_START_LOC : Constants.NEW_ARMOR_START_LOC) + 3;
		TransplantSMP.LOGGER.info("subtractIdx: " + subtractIndex);

		this.equipmentSlot = AccessorPlayerScreenHandler.getEquipmentSlotOrder()[subtractIndex - index];
	}

	@Override
	public void setStack(ItemStack stack) {
		ItemStack itemStack = this.getStack();
		super.setStack(stack);
		owner.onEquipStack(this.equipmentSlot, itemStack, stack);
	}

	@Override
	public int getMaxItemCount() {
		return 1;
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return equipmentSlot == MobEntity.getPreferredEquipmentSlot(stack);
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerEntity) {
		ItemStack itemStack = this.getStack();
		if (!itemStack.isEmpty() && !playerEntity.isCreative() && EnchantmentHelper.hasBindingCurse(itemStack)) {
			return false;
		}
		return super.canTakeItems(playerEntity);
	}

	@Override
	public Pair<Identifier, Identifier> getBackgroundSprite() {
		return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AccessorPlayerScreenHandler.getEmptyArmorSlotTextures()[this.equipmentSlot.getEntitySlotId()]);
	}

	@Override
	public boolean isEnabled() {
		boolean bl = true;
		if(this.getIndex() >= Constants.EXTRA_ARMOR_START_LOC) {
			bl = transplantable.getTransplantType() == TransplantType.SKIN_TRANSPLANT;
		}
		return bl;
	}
}
