package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.IPlayerInventoryTransplanted;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.inventory.ArmorSlot;
import ml.ikwid.transplantsmp.common.util.Utils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
	private final LivingEntity self = (LivingEntity)(Object) this;

	@ModifyArgs(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"))
	private void changeArmorBarsAndToughness(Args args) {
		if(self instanceof ServerPlayerEntity serverPlayerEntity && ((ITransplantable) serverPlayerEntity).getTransplantType() == TransplantType.SKIN_TRANSPLANT) {
			ITransplantable transplantable = (ITransplantable) serverPlayerEntity;
			int armorBars = args.get(1);
			double toughness = args.get(2);

			if(transplantable.getTransplantedAmount() > 20) {
				for (ItemStack itemStack : ((IPlayerInventoryTransplanted) (serverPlayerEntity.getInventory())).getSecondaryArmor()) {
					ArmorItem armorItem = (ArmorItem) (itemStack.getItem());
					armorBars += armorItem.getProtection();
					toughness += armorItem.getToughness();
				}
			} else {
				toughness = Utils.scaleArmorToughness(toughness, transplantable.getTransplantedAmount() + 20);
			}
			args.set(1, Math.min(transplantable.getTransplantedAmount() + 20, armorBars));
			args.set(2, toughness);

			TransplantSMP.LOGGER.info("armorbars: " + armorBars + ", toughness: " + toughness);
		}
	}
}
