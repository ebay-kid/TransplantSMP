package ml.ikwid.transplantsmp.common.item;

import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class OrganItem extends Item {
	public OrganItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
		if(world.isClient()) {
			return super.use(world, playerEntity, hand);
		}

		ITransplantable transplantable = (ITransplantable) playerEntity;
		ItemStack stack = playerEntity.getStackInHand(hand);
		if(transplantable.getTransplantType() == TransplantType.ARM_TRANSPLANT) {
			if(transplantable.getTransplantedAmount() >= 18) {
				return TypedActionResult.pass(stack);
			}
		} else {
			if(transplantable.getTransplantedAmount() >= 20) {
				return TypedActionResult.pass(stack);
			}
		}
		transplantable.transplantOrgan(true);

		stack.decrement(1);

		return TypedActionResult.success(stack);
	}
}
