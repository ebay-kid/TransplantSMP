package ml.ikwid.transplantsmp.common.item;

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
		transplantable.transplantOrgan(true);

		ItemStack stackInHand = playerEntity.getStackInHand(hand);
		stackInHand.decrement(1);

		return TypedActionResult.success(stackInHand);
	}
}
