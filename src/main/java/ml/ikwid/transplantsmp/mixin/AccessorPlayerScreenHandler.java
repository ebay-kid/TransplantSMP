package ml.ikwid.transplantsmp.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerScreenHandler.class)
public interface AccessorPlayerScreenHandler {
	@Accessor("EQUIPMENT_SLOT_ORDER")
	static EquipmentSlot[] getEquipmentSlotOrder() {
		throw new AssertionError();
	}

	@Accessor("EMPTY_ARMOR_SLOT_TEXTURES")
	static Identifier[] getEmptyArmorSlotTextures() {
		throw new AssertionError();
	}
}
