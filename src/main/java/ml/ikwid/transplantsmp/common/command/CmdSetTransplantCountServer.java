package ml.ikwid.transplantsmp.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import ml.ikwid.transplantsmp.api.ITransplantable;
import net.minecraft.command.CommandException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CmdSetTransplantCountServer {
    public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ITransplantable transplantable = (ITransplantable)(EntityArgumentType.getPlayer(ctx, "player"));
        if(transplantable == null) {
            throw new CommandException(Text.of("Unable to cast player to an ITransplantable"));
        }
        transplantable.setTransplantedAmount(IntegerArgumentType.getInteger(ctx, "amount"), true);

        return 1;
    }
}
