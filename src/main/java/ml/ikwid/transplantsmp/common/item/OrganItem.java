package ml.ikwid.transplantsmp.common.item;

import ml.ikwid.transplantsmp.api.TransplantType;
import ml.ikwid.transplantsmp.api.ITransplantable;
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
		TransplantType transplantType = transplantable.getTransplantType();
		if(!transplantType.canTransplant(transplantable.getTransplantedAmount() + transplantType.getDefaultChangeByAmount())) {
			return TypedActionResult.pass(stack);
		}

		transplantable.transplantOrgan(true);

		stack.decrement(1);

		return TypedActionResult.success(stack);
	}
}
