package ml.ikwid.transplantsmp.mixin.screen;

import ml.ikwid.transplantsmp.TransplantSMP;
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
		TransplantSMP.LOGGER.info("adding 4 extra armor slots");
		for(int i = 0; i < 4; i++) {
			this.addSlot(new ArmorSlot(inventory, Constants.EXTRA_ARMOR_START_LOC + 3 - i, 8 - Constants.HOTBAR_SPACE_IN_INV_SCREEN, 8 + i * 18));
		}
	}

	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 2))
	private Slot antiAnonymous(Slot slot) {
		return new ArmorSlot(slot.inventory, -(39 - slot.getIndex()) + Constants.NEW_ARMOR_START_LOC + 3, slot.x, slot.y); // we do it here because it initializes here and i don't wanna deal with it somewhere else
	}
}
