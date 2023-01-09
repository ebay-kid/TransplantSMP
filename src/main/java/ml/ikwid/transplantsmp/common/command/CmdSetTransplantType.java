package ml.ikwid.transplantsmp.common.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import ml.ikwid.transplantsmp.api.TransplantTypes;
import ml.ikwid.transplantsmp.api.ITransplantable;
import net.minecraft.command.CommandException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CmdSetTransplantType {
	public static int run(CommandContext<ServerCommandSource> ctx) {
		ITransplantable transplantable = (ITransplantable)(ctx.getSource().getPlayer());
		if(transplantable == null) {
			throw new CommandException(Text.of("Unable to cast player to an ITransplantable"));
		}
		transplantable.setTransplantType(TransplantTypes.get(StringArgumentType.getString(ctx, "type")), true);

		return 1;
	}
}
