package ml.ikwid.transplantsmp.mixin.armtransplant;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class MixinPlayerInventory {
	@Shadow @Final public PlayerEntity player;
	@Shadow public int selectedSlot;

	@Shadow @Final public DefaultedList<ItemStack> main;

	@Shadow @Final public DefaultedList<ItemStack> armor;
	private ITransplantable transplantable;

	@Inject(method = "getHotbarSize", at = @At(value = "HEAD"), cancellable = true)
	private static void customHotbarSize(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(18);
	}

	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;ofSize(ILjava/lang/Object;)Lnet/minecraft/util/collection/DefaultedList;", ordinal = 0), index = 0)
	private int fixInvSize(int size) {
		return 45;
	}

	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;ofSize(ILjava/lang/Object;)Lnet/minecraft/util/collection/DefaultedList;", ordinal = 1), index = 0)
	private int fixArmorSlots(int size) {
		return 8;
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void setPlayer(PlayerEntity player, CallbackInfo ci) {
		this.transplantable = (ITransplantable) player;
	}

	/**
	 * @author 6Times
	 * @reason cannot inject into bottom of method due to the conditional with return
	 */
	@Overwrite
	public void damageArmor(DamageSource damageSource, float amount, int[] slots) {
		if(amount <= 0.0f) {
			return;
		}
		if((amount /= 4.0f) < 1.0f) {
			amount = 1.0f;
		}
		for(int i : slots) {
			ItemStack inv1 = this.armor.get(i);
			ItemStack inv2 = this.armor.get(i * 2);
			if(!(damageSource.isFire() && inv1.getItem().isFireproof() || !(inv1.getItem() instanceof ArmorItem))) {
				inv1.damage((int)amount, this.player, player -> player.sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i)));
			}
			if(!(damageSource.isFire() && inv2.getItem().isFireproof() || !(inv2.getItem() instanceof ArmorItem))) {
				inv2.damage((int)amount, this.player, player -> player.sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i * 2)));
			}
		}
	}

	/**
	 * @author 6Times
	 * @reason Handle my unwillingness to change the other slot indices
	 */
	@Overwrite
	public void scrollInHotbar(double scrollAmount) {
		if(this.transplantable == null) {
			this.selectedSlot = 0;
			TransplantSMP.LOGGER.info("get screwed no scrolling for you bozo");
			return;
		}
		int i = (int)(Math.signum(scrollAmount));
		this.selectedSlot -= i;

		int slots = this.transplantable.getHotbarDraws();
		while(this.selectedSlot < 0) {
			if(slots <= 9) {
				this.selectedSlot += slots;
			} else {
				this.selectedSlot = TransplantSMP.NEW_HOTBAR_START_LOC + 17 - transplantable.getHalvedTransplantedAmount(); // 49 is highest index, so we do 9 - halvedTransplantedAmount() to see how many less than the max the player has.
			}
		}
		while((this.selectedSlot > 8 && this.selectedSlot < TransplantSMP.NEW_HOTBAR_START_LOC) || this.selectedSlot > TransplantSMP.NEW_HOTBAR_START_LOC + 8) {
			this.selectedSlot = 0;
		}
	}

	/**
	 * @author 6Times
	 * @reason complete rewrite
	 */
	@Overwrite
	public static boolean isValidHotbarIndex(int slot) {
		return (slot >= 0 && slot < 9) || (slot >= 41 && slot < 50);
	}

	/**
	 * @author 6Times
	 * @reason mostly rewritten. also what does this even do???
	 */
	@Overwrite
	public int getSwappableHotbarSlot() {
		if(transplantable == null) {
			TransplantSMP.LOGGER.info("REEEEEEEEEEEEE NO HOTBAR SWAP FOR YOU IDIOT");
			return selectedSlot;
		}
		int i, j;
		for(i = 0; i < transplantable.getHotbarDraws(); i++) {
			j = (selectedSlot + i) % transplantable.getHotbarDraws();
			if(main.get(j).isEmpty() || !main.get(j).hasEnchantments()) {
				return j;
			}
		}
		return selectedSlot;
	}


	/**
	 * @author 6Times
	 * @reason mostly rewritten
	 */
	@Overwrite
	public int getEmptySlot() {
		for(int i = 0; i < this.main.size(); i++) {
			if(!this.main.get(i).isEmpty()) {
				continue; // not empty so skip
			}
			if(isValidMainHotbar(i)) { // if it's a valid hotbar then do this
				if(transplantable.getTransplantType() != TransplantType.ARM_TRANSPLANT) { // not arm transplant
					if(i > 8) {
						continue; // the special hotbar slots that we can't touch
					}
					return i; // otherwise return
				}
				// is arm transplant
				if(transplantable.getHalvedTransplantedAmount() <= 0) { // if you've lost a slot or gained none
					if(i > 8 + transplantable.getHalvedTransplantedAmount()) { // if it's a slot that's hidden
						continue;
					}
					return i; // otherwise return
				}
				// slots gained
				if(i > 36 + transplantable.getHalvedTransplantedAmount()) { // slots gained >= 1
					continue;
				}
				return i + 5; // otherwise return, +5 to account for the other crap
			}
			return i; // otherwise return
		}
		return -1; // none found
	}

	public boolean isValidMainHotbar(int slot) {
		return slot < 9 || slot >= 36;
	}
}
