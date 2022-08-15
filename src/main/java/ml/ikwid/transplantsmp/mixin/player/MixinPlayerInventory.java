package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/**
 * In vanilla, here are the normal slot number layouts:
 *
 * <pre>
 * HOTBAR: 0 - 8
 * MAIN: 9 - 35
 * ARMOR: 36 - 39 (36 head -> 39 foot)
 * OFF HAND: 40
 * </pre>
 * Under this mixin, here are the new slot number layouts:
 *
 * <pre>
 * VANILLA HOTBAR: 0 - 8
 * ADDON HOTBAR: 9 - 17
 * MAIN: 18 - 44
 * ARMOR: 45 - 48 (45 head -> 48 foot)
 * OFF HAND: 49 <-- this took me way too long to figure out because apparently packing the extra armor behind it was causing sync issues.
 * EXTRA ARMOR SLOTS: 50 - 53 (50 head -> 53 foot)
 *
 * </pre>
 *
 * See {@link Constants} for the impl.
 */
@Mixin(PlayerInventory.class)
public abstract class MixinPlayerInventory {
	@Shadow @Final public PlayerEntity player;

	@Shadow @Final public DefaultedList<ItemStack> armor;

	@Shadow @Final public DefaultedList<ItemStack> main;

	@Mutable @Shadow @Final private List<DefaultedList<ItemStack>> combinedInventory;

	@Unique
	public final DefaultedList<ItemStack> secondaryArmor = DefaultedList.ofSize(4, ItemStack.EMPTY);

	@Shadow
	public static boolean isValidHotbarIndex(int slot) {
		throw new AssertionError();
	}

	private ITransplantable transplantable;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void expandCombinedInventory(PlayerEntity player, CallbackInfo ci) {
		var mutable = new ArrayList<>(this.combinedInventory);
		mutable.add(this.secondaryArmor); // h

		this.combinedInventory = mutable;
	}

	@Inject(method = "getHotbarSize", at = @At(value = "HEAD"), cancellable = true)
	private static void customHotbarSize(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(18);
	}

	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;ofSize(ILjava/lang/Object;)Lnet/minecraft/util/collection/DefaultedList;", ordinal = 0), index = 0)
	private int fixInvSize(int size) {
		return 45;
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void setPlayer(PlayerEntity player, CallbackInfo ci) {
		this.transplantable = (ITransplantable) player;
	}

	@Inject(method = "writeNbt", at = @At("TAIL"))
	private void writeExtraArmor(NbtList nbtList, CallbackInfoReturnable<NbtList> cir) {
		for (ItemStack itemStack : this.secondaryArmor) {
			if (itemStack.isEmpty()) {
				continue;
			}
			NbtCompound nbtCompound = new NbtCompound();
			itemStack.writeNbt(nbtCompound); // why does it return when it... writes straight to it?
			nbtList.add(nbtCompound);
		}
	}

	@Redirect(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtList;size()I"))
	private int returnLess(NbtList instance) {
		return instance.size() - 4; // loool
	}

	@Inject(method = "readNbt", at = @At("TAIL"))
	private void readExtraArmor(NbtList nbtList, CallbackInfo ci) {
		int size = nbtList.size();
		for(int i = 0; i < 4; i++) { // more scuff
			NbtCompound nbtCompound = nbtList.getCompound(size - 4 + i);
			this.secondaryArmor.set(i, ItemStack.fromNbt(nbtCompound));
		}
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
			// ItemStack inv2 = this.armor.get(i * 2);
			if(!(damageSource.isFire() && inv1.getItem().isFireproof() || !(inv1.getItem() instanceof ArmorItem))) {
				inv1.damage((int)amount, this.player, player -> player.sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i)));
			}
			/*
			if(!(damageSource.isFire() && inv2.getItem().isFireproof() || !(inv2.getItem() instanceof ArmorItem))) {
				inv2.damage((int)amount, this.player, player -> player.sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i * 2)));
			}
			*/
		}
	}

	@ModifyConstant(method = "isValidHotbarIndex", constant = @Constant(intValue = 9, ordinal = 0))
	private static int increaseCheckedHotbar(int constant) {
		return 18; // This doesn't matter because the hotkeys won't work for slots above the actually allowed amount.
	}

	/**
	 * @author 6Times
	 * @reason Pretty much an entire rewrite
	 */
	@Overwrite
	public int getEmptySlot() {
		for(int i = 0; i < this.main.size(); i++) {
			if(isValidHotbarIndex(i)) {
				if(i > this.transplantable.getHotbarDraws() - 1) {
					continue;
				}
			}
			if(this.main.get(i).isEmpty()) {
				return i;
			}
		}
		return -1;
	}

	@ModifyConstant(method = "getSwappableHotbarSlot", constant = @Constant(intValue = 9, ordinal = 0))
	private int increaseCheckedHotbar2(int constant) {
		return transplantable.getHotbarDraws();
	}

	@ModifyConstant(method = "getSwappableHotbarSlot", constant = @Constant(intValue = 9, ordinal = 1))
	private int increaseCheckedHotbar3(int constant) {
		return transplantable.getHotbarDraws();
	}

	@ModifyConstant(method = "getSwappableHotbarSlot", constant = @Constant(intValue = 9, ordinal = 2))
	private int increaseCheckedHotbar4(int constant) {
		return transplantable.getHotbarDraws();
	}

	@ModifyConstant(method = "getSwappableHotbarSlot", constant = @Constant(intValue = 9, ordinal = 3))
	private int increaseCheckedHotbar5(int constant) {
		return transplantable.getHotbarDraws();
	}

	@ModifyConstant(method = "scrollInHotbar", constant = @Constant(intValue = 9, ordinal = 0))
	private int increaseCheckedHotbar6(int constant) {
		return transplantable.getHotbarDraws();
	}

	@ModifyConstant(method = "scrollInHotbar", constant = @Constant(intValue = 9, ordinal = 1))
	private int increaseCheckedHotbar7(int constant) {
		return transplantable.getHotbarDraws();
	}

	@ModifyConstant(method = "scrollInHotbar", constant = @Constant(intValue = 9, ordinal = 2))
	private int increaseCheckedHotbar8(int constant) {
		return transplantable.getHotbarDraws();
	}

	@ModifyConstant(method = "getOccupiedSlotWithRoomForStack", constant = @Constant(intValue = 40, ordinal = 0))
	private int changeOffHandSlot(int constant) {
		return Constants.OFF_HAND;
	}

	@ModifyConstant(method = "getOccupiedSlotWithRoomForStack", constant = @Constant(intValue = 40, ordinal = 1))
	private int changeOffHandSlot2(int constant) {
		return Constants.OFF_HAND;
	}
}
