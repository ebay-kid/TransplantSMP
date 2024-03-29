package ml.ikwid.transplantsmp.common.inventory;

import com.mojang.datafixers.util.Pair;
import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.gamerule.GameruleRegister;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Constants;
import ml.ikwid.transplantsmp.mixin.AccessorPlayerScreenHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Unique;

public class ArmorSlot extends Slot {
	private final PlayerEntity owner;
	private final ITransplantable transplantable;
	private final EquipmentSlot equipmentSlot;

	private final boolean isExtraArmor;
	public ArmorSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.owner = ((PlayerInventory)(inventory)).player;
		this.transplantable = (ITransplantable)(this.owner);

		// 45, 46, 47, 48 <- new, 49, 50, 51, 52 <- vanilla, 53 <- offhand
		this.isExtraArmor = index <= Constants.EXTRA_ARMOR_START_LOC + 3;
		// TransplantSMP.LOGGER.info("index: " + index + ", isExtraArmor: " + isExtraArmor);
		int subtractIndex = (isExtraArmor ? Constants.EXTRA_ARMOR_START_LOC : Constants.NEW_ARMOR_START_LOC) + 3;
		// TransplantSMP.LOGGER.info("subtractIdx: " + subtractIndex);

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
		// first, make sure the item stack is the correct thing (helm, ...)
		// then, if it's not extra armor then the first expr -> true and whole thing is true, so rest is ignored
		// if it is extra armor, then the first expr -> false and check if the gamerule for banning secondary set of netherite armor is true
		// if the gamerule is false, then 2nd expr -> true and whole thing is true, so rest is ignored
		// if the gamerule is true, then 2nd expr -> false and check if the armor piece is netherite
		return equipmentSlot == MobEntity.getPreferredEquipmentSlot(stack) && (!this.isExtraArmor || !this.owner.world.getGameRules().getBoolean(GameruleRegister.NO_NETHERITE_SKIN_SECONDARY) || !stackIsNetherite(stack));
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
		return !this.isExtraArmor || this.transplantable.getTransplantType() == TransplantType.SKIN_TRANSPLANT;
	}

	@Unique
	public static boolean stackIsNetherite(ItemStack stack) {
		if(stack.getItem() instanceof ArmorItem armorItem) {
			return armorItem.getMaterial() == ArmorMaterials.NETHERITE;
		}
		return false;
	}
}
