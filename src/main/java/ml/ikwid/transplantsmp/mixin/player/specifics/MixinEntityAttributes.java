package ml.ikwid.transplantsmp.mixin.player.specifics;

import ml.ikwid.transplantsmp.TransplantSMP;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(EntityAttributes.class)
public abstract class MixinEntityAttributes {
	// how does one target something that's not in a method
	@Inject(method = "register", at = @At("HEAD"), cancellable = true)
	private static void changeGenericArmor(String id, EntityAttribute attribute, CallbackInfoReturnable<EntityAttribute> cir) {
		TransplantSMP.LOGGER.info("injected into register of EntityAttributes");
		if(Objects.equals(id, "generic.armor")) {
			cir.setReturnValue(Registry.register(Registry.ATTRIBUTE, id, new ClampedEntityAttribute("attribute.name.generic.armor", 0.0, 0.0, 40.0).setTracked(true)));
		} else if(Objects.equals(id, "generic.armor_toughness")) {
			cir.setReturnValue(Registry.register(Registry.ATTRIBUTE, id, new ClampedEntityAttribute("attribute.name.generic.armor_toughness", 0.0, 0.0, 24.0).setTracked(true)));
		}
	}
}
