package ml.ikwid.transplantsmp.mixin.screen;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.inventory.ArmorSlot;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerScreenHandler.class)
public abstract class MixinPlayerScreenHandler extends ScreenHandler {

	protected MixinPlayerScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
		super(type, syncId);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void addExtraArmor(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
		ITransplantable transplantable = (ITransplantable) owner;

		if(transplantable.getTransplantType() == TransplantType.SKIN_TRANSPLANT) {
			TransplantSMP.LOGGER.info("adding 4 extra armor slots");
			for(int i = 0; i < 4; i++) {
				this.addSlot(new ArmorSlot(inventory, Constants.EXTRA_ARMOR_START_LOC + 3 - i, 30, 9 + i * 18));
			}
		}
	}

	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 2))
	private Slot antiAnonymous(Slot slot) {
		return new ArmorSlot(slot.inventory, slot.getIndex(), slot.x, slot.y);
	}

	/*
	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 39))
	private int armorIndex(int constant) {
		return Constants.NEW_ARMOR_START_LOC + 3; // for SOME REASON it goes backwards instead of forwards
	}

	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 40))
	private int offHandIndex(int constant) {
		return Constants.OFF_HAND;
	}

	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 9, ordinal = 2))
	private int changeDrawnSlotCounts(int constant) {
		return 18;
	}
	*/

	/*
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 4))
	private Slot changeHotbarSlotLocation(PlayerScreenHandler instance, Slot slot) {
		((ISlotTransplanted) slot).setX(slot.x + Utils.innerSlotXShift(this.owner));
		return ((MixinScreenHandlerAccessor) self).addSlotAccess(slot);
	}
	*/

	/*
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 3))
	public Slot changeInventorySlotIndices(PlayerScreenHandler instance, Slot slot) {
		return ((MixinScreenHandlerAccessor) self).addSlotAccess(new Slot(slot.inventory, slot.getIndex() + 9, slot.x, slot.y));
	}
	*/
}
