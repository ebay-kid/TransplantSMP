package ml.ikwid.transplantsmp.mixin.skintransplant;

import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
	private final LivingEntity self = (LivingEntity)(Object) this;

	@ModifyArg(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"), index = 2)
	private float forceToughness(float damage) {
		if(self instanceof ServerPlayerEntity) {
			return MathHelper.clamp(damage, 0, ((ITransplantable) self).getTransplantedAmount() * 0.8f);
		}
		return damage;
	}
}
