package ml.ikwid.transplantsmp.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.command.CommandException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class SetTransplants {
	public static int run(CommandContext<ServerCommandSource> ctx) {
		ITransplantable transplantable = (ITransplantable)(ctx.getSource().getPlayer());
		if(transplantable == null) {
			throw new CommandException(Text.of("Unable to cast player to an ITransplantable"));
		}
		transplantable.setTransplantedAmount(IntegerArgumentType.getInteger(ctx, "count"));

		return 0;
	}
}
