package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
	@Shadow public abstract ItemStack getEquippedStack(EquipmentSlot var1);

	private final LivingEntity self = (LivingEntity)(Object) this;

	@ModifyArg(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"), index = 2)
	private float forceToughness(float damage) {
		if(self instanceof ServerPlayerEntity) {
			return MathHelper.clamp(damage, 0, ((ITransplantable) self).getTransplantedAmount() * 0.8f);
		}
		return damage;
	}

	/**
	 * @author 6Times
	 * @reason testing, TODO: Remove
	 */
	@Overwrite
	public ItemStack getOffHandStack() {
		ItemStack offHand = this.getEquippedStack(EquipmentSlot.OFFHAND);
		TransplantSMP.LOGGER.info(offHand.getName().getString() + " " + offHand.getCount());

		return offHand;
	}
}
