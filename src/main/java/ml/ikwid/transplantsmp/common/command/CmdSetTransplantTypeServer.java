package ml.ikwid.transplantsmp.common.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import ml.ikwid.transplantsmp.api.TransplantTypes;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.command.CommandException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CmdSetTransplantTypeServer {
    public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ITransplantable transplantable = (ITransplantable)(EntityArgumentType.getPlayer(ctx, "player"));
        if(transplantable == null) {
            throw new CommandException(Text.of("Unable to cast player to an ITransplantable"));
        }
        transplantable.setTransplantType(TransplantTypes.get(StringArgumentType.getString(ctx, "type")), true);

        return 1;
    }
}
