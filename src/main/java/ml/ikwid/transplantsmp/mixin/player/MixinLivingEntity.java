package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
	private final LivingEntity self = (LivingEntity)(Object) this;

	@Inject(method = "getAttributeValue", at = @At("TAIL"), cancellable = true)
	private void changeAttributeReturns(EntityAttribute attribute, CallbackInfoReturnable<Double> cir) {
		if(self instanceof PlayerEntity playerEntity && ((ITransplantable) playerEntity).getTransplantType() == TransplantType.SKIN_TRANSPLANT) {
			ITransplantable transplantable = (ITransplantable) playerEntity;
			double ret = 0;
			boolean setRet = false;

			if(attribute == EntityAttributes.GENERIC_ARMOR) {
				setRet = true;

				for(int i = Constants.NEW_ARMOR_START_LOC; i <= Constants.NEW_ARMOR_START_LOC + 3; i++) {
					Item item = playerEntity.getInventory().getStack(i).getItem();
					// TransplantSMP.LOGGER.info("index " + i + " is a " + item.getName().getString());

					if(item instanceof ArmorItem armorItem) {
						ret += armorItem.getProtection();
					}
				}

				if (transplantable.getTransplantedAmount() > 0) {
					for (int i = Constants.EXTRA_ARMOR_START_LOC; i <= Constants.EXTRA_ARMOR_START_LOC + 3; i++) {
						Item item = playerEntity.getInventory().getStack(i).getItem();
						// TransplantSMP.LOGGER.info("index " + i + " is a " + item.getName().getString());

						if (item instanceof ArmorItem armorItem) {
							double prot = armorItem.getProtection();

							ret += prot;
						}
					}

				}
				ret = Math.min(ret, transplantable.getTransplantedAmount() + 20);
			} else if(attribute == EntityAttributes.GENERIC_ARMOR_TOUGHNESS) {
				setRet = true;

				for(int i = Constants.NEW_ARMOR_START_LOC; i <= Constants.NEW_ARMOR_START_LOC + 3; i++) {
					Item item = playerEntity.getInventory().getStack(i).getItem();
					// TransplantSMP.LOGGER.info("index " + i + " is a " + item.getName().getString());

					if(item instanceof ArmorItem armorItem) {
						ret += armorItem.getToughness();
					}
				}
				if(transplantable.getTransplantedAmount() < 0) {
					ret *= 1 + transplantable.getTransplantedAmount() / 20.0d; // transplanted amount always negative, this is the same as (20 + amount)/20, which scales down the return value
				} else if(transplantable.getTransplantedAmount() > 0){
					for(int i = Constants.EXTRA_ARMOR_START_LOC; i <= Constants.EXTRA_ARMOR_START_LOC + 3; i++) {
						Item item = playerEntity.getInventory().getStack(i).getItem();
						// TransplantSMP.LOGGER.info("index " + i + " is a " + item.getName().getString());

						if(item instanceof ArmorItem armorItem) {
							float toughness = armorItem.getToughness();
							ret += toughness;
						}
					}
					ret = Math.min(ret, (transplantable.getTransplantedAmount() * 12.0d / 20.0d) + 12); // caps it by scaling the transplanted amount (max 20) to a max of 12 (vanilla toughness) and multiplying.
				}
			} else if(attribute == EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) {
				setRet = true;

				for(int i = Constants.NEW_ARMOR_START_LOC; i < Constants.NEW_ARMOR_START_LOC + 3; i++) {
					Item item = playerEntity.getInventory().getStack(i).getItem();
					// TransplantSMP.LOGGER.info("index " + i + " is a " + item.getName().getString());

					if(item instanceof ArmorItem armorItem) {
						ret += armorItem.getMaterial().getKnockbackResistance();
					}
				}
			}

			if(setRet) {
				cir.setReturnValue(ret);
				cir.cancel();
			}
		}
	}

	@Redirect(method = "modifyAppliedDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getProtectionAmount(Ljava/lang/Iterable;Lnet/minecraft/entity/damage/DamageSource;)I"))
	private int scaleProtection(Iterable<ItemStack> equipment, DamageSource source) {
		if(this instanceof ITransplantable transplantable) {
			return (int) (EnchantmentHelper.getProtectionAmount(equipment, source) * (transplantable.getTransplantType() == TransplantType.SKIN_TRANSPLANT && transplantable.getTransplantedAmount() < 0 ? 1.0d + transplantable.getTransplantedAmount() / 20.0d : 1));
		}
		return EnchantmentHelper.getProtectionAmount(equipment, source);
	}
}
