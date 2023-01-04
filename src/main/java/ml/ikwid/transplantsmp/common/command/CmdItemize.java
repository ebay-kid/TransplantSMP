package ml.ikwid.transplantsmp.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import ml.ikwid.transplantsmp.api.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.item.ItemRegister;
import net.minecraft.command.CommandException;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CmdItemize {
	private static final CommandException nonPlayer = new CommandException(Text.of("Cannot take organs from a non-player"));
	private static final CommandException notEnough = new CommandException(Text.of("Not enough organs"));
	private static final CommandException badAmount = new CommandException(Text.of("Nice try bucko."));
	public static int run(CommandContext<ServerCommandSource> ctx) {
		int amount = 1;
		try {
			amount = IntegerArgumentType.getInteger(ctx, "amount");
		} catch (Exception e) {
			// im smart
		}
		if(amount < 0) {
			throw badAmount;
		}

		ServerPlayerEntity serverPlayerEntity = ctx.getSource().getPlayer();
		ITransplantable transplantable = (ITransplantable)(serverPlayerEntity);
		if(transplantable == null) {
			throw nonPlayer;
		}

		TransplantType type = transplantable.getTransplantType();

		if(!type.canTransplant(transplantable.getTransplantedAmount() - (amount * type.getDefaultChangeByAmount()))) {
			throw notEnough;
		}
		transplantable.setTransplantedAmount(transplantable.getTransplantedAmount() - (amount * type.getDefaultChangeByAmount()), true, false);

		if(amount > 32) {
			serverPlayerEntity.giveItemStack(new ItemStack(ItemRegister.ORGAN_ITEM, 16));
			amount -= 32;
		}
		serverPlayerEntity.giveItemStack(new ItemStack(ItemRegister.ORGAN_ITEM, amount));
		return 1;
	}
}
