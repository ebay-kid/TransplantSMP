package ml.ikwid.transplantsmp.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.server.command.ServerCommandSource;

public class SetTransplants {
	public static int run(CommandContext<ServerCommandSource> ctx) {
		ITransplantable transplantable = (ITransplantable)(ctx.getSource().getPlayer());
		transplantable.setTransplantedAmount(IntegerArgumentType.getInteger(ctx, "count"));

		return 0;
	}
}
