package ml.ikwid.transplantsmp.common.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.server.command.ServerCommandSource;

public class SetTransplantType {
	public static int run(CommandContext<ServerCommandSource> ctx) {
		ITransplantable transplantable = (ITransplantable)(ctx.getSource().getPlayer());
		transplantable.setTransplantType(TransplantType.valueOf(StringArgumentType.getString(ctx, "type")));

		return 0;
	}
}
