package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Constants;
import ml.ikwid.transplantsmp.common.util.Utils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
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
 * EXTRA ARMOR SLOTS: 45 - 48 (45 head -> 48 foot)
 * VANILLA ARMOR SLOTS: 49 - 52
 * OFF HAND: 53
 * </pre>
 *
 * See {@link Constants} for the impl.
 */
@Mixin(PlayerInventory.class)
public abstract class MixinPlayerInventory implements Inventory, Nameable {
	@Shadow @Final public PlayerEntity player;

	@Mutable @Shadow @Final public DefaultedList<ItemStack> main;

	@Mutable @Shadow @Final private List<DefaultedList<ItemStack>> combinedInventory;

	@Shadow
	public static boolean isValidHotbarIndex(int slot) {
		throw new AssertionError();
	}

	private ITransplantable transplantable;

	@Redirect(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;clear()V", ordinal = 0))
	private void noClear(DefaultedList<?> instance) { // LLLL even though i don't need it i don't think
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void expandCombinedInventory(PlayerEntity player, CallbackInfo ci) {
		var mutable = new ArrayList<>(this.combinedInventory);

		this.main = DefaultedList.ofSize(49, ItemStack.EMPTY); // 36 Vanilla + 9 Extra Hotbar + 4 Extra Armor = 49
		mutable.set(0, this.main);

		this.combinedInventory = mutable;
	}

	@Inject(method = "getHotbarSize", at = @At(value = "HEAD"), cancellable = true)
	private static void customHotbarSize(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(18);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void setPlayer(PlayerEntity player, CallbackInfo ci) {
		this.transplantable = (ITransplantable) player;
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
			if(Utils.isExtraArmorSlot(i)) {
				continue;
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

	@Inject(method = "damageArmor", at = @At("TAIL"))
	private void damageSecondSet(DamageSource damageSource, float amount, int[] slots, CallbackInfo ci) {
		if(((ITransplantable)(this.player)).getTransplantType() == TransplantType.SKIN_TRANSPLANT) {
			for(int i = Constants.EXTRA_ARMOR_START_LOC; i < Constants.EXTRA_ARMOR_START_LOC + 4; i++) {
				ItemStack itemStack = this.main.get(i);
				if (damageSource.isFire() && itemStack.getItem().isFireproof() || !(itemStack.getItem() instanceof ArmorItem)) {
					continue;
				}

				int finalI = i; // apparently i need this
				itemStack.damage((int)amount, this.player, player -> player.sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, finalI)));
			}
		}
	}
}
