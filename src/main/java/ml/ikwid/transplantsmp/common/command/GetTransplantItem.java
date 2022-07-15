package ml.ikwid.transplantsmp.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.item.ItemRegister;
import net.minecraft.command.CommandException;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class GetTransplantItem {
	private static final CommandException nonPlayer = new CommandException(Text.of("Cannot take organs from a non-player..."));
	private static final CommandException notEnough = new CommandException(Text.of("Not enough organs"));
	public static int run(CommandContext<ServerCommandSource> ctx) {
		int amount = 2;
		try {
			amount = IntegerArgumentType.getInteger(ctx, "amount") * 2;
		} catch(IllegalArgumentException e) {
			// none
		}
		ServerPlayerEntity serverPlayerEntity = ctx.getSource().getPlayer();
		ITransplantable transplantable = (ITransplantable)(serverPlayerEntity);
		if(transplantable == null) {
			throw nonPlayer;
		}
		int transplanted = transplantable.getTransplantedAmount();
		if(transplantable.getTransplantType() == TransplantType.ARM_TRANSPLANT) {
			amount /= 2;
			if(transplantable.getHalvedTransplantedAmount() - amount > -9) {
				amount *= 2;
				transplantable.setTransplantedAmount(transplanted - amount, true, false);
			} else {
				throw notEnough;
			}
		} else {
			if(transplanted - amount > -18) {
				transplantable.setTransplantedAmount(transplanted - amount, true, false);
			} else {
				throw notEnough;
			}
		}
		serverPlayerEntity.giveItemStack(new ItemStack(ItemRegister.ORGAN_ITEM, amount / 2));
		return 1;
	}
}
