package ml.ikwid.transplantsmp.mixin.skintransplant;

import com.mojang.datafixers.util.Pair;
import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerScreenHandler.class)
public class MixinPlayerScreenHandler {
	@Shadow @Final private PlayerEntity owner;
	@Shadow @Final private static EquipmentSlot[] EQUIPMENT_SLOT_ORDER;
	@Shadow @Final private static Identifier[] EMPTY_ARMOR_SLOT_TEXTURES;

	private final ITransplantable transplantable = (ITransplantable) owner;

	private final PlayerScreenHandler self = (PlayerScreenHandler)(Object) this;
	private final int SLOT_WIDTH = 18;

	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 9, ordinal = 2))
	private int drawMoreOrLessSlots(int constant) {
		return transplantable.getHotbarDraws();
	}

	// I don't know why MCDev complains... it looks perfectly fine
	// A redirect doesn't work either, the @At refuses to accept it exists?
	// Any number below 3 works for the other ones but not 3.
	@ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;<init>(Lnet/minecraft/inventory/Inventory;III)V", ordinal = 3))
	private void setXandIndices(Args args) {
		int index = args.get(1);
		if(index > 8) {
			args.set(1, index - 9 + TransplantSMP.NEW_HOTBAR_START_LOC); // n slots in inventory, so index - 9 + n to occupy next available slot at index n
		}
		// it appears the shift is 18 for this one, so...
		args.set(2, (int)(args.get(2)) + this.xShift());
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void addSecondSet(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
		if(transplantable.getTransplantType() == TransplantType.SKIN_TRANSPLANT) {
			for(int i = 0; i < 4; i++) { // copy the code
				final EquipmentSlot equipmentSlot = EQUIPMENT_SLOT_ORDER[i];
				((MixinScreenHandler) self).addSlotAccess(new Slot(inventory, 39 - i, 8, 8 + i * 18){
					@Override
					public void setStack(ItemStack stack) {
						ItemStack itemStack = this.getStack();
						super.setStack(stack);
						owner.onEquipStack(equipmentSlot, itemStack, stack);
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
						return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, EMPTY_ARMOR_SLOT_TEXTURES[equipmentSlot.getEntitySlotId()]);
					}
				});
			}
		}
	}

	private int xShift() {
		return -(transplantable.getHotbarDraws() * SLOT_WIDTH / 2);
	}
}
