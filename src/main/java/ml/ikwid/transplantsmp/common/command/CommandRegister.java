package ml.ikwid.transplantsmp.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CommandRegister {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralCommandNode<ServerCommandSource> transplantSMP = CommandManager
			.literal("transplantsmp")
			.build();
		LiteralCommandNode<ServerCommandSource> alias = CommandManager
			.literal("ts")
			.redirect(transplantSMP)
			.build();

		LiteralCommandNode<ServerCommandSource> setTransplantCount = CommandManager
			.literal("setcount")
				.then(
					CommandManager
						.argument("count", IntegerArgumentType.integer())
							.executes(SetTransplants::run)
				)
			.build();
		LiteralCommandNode<ServerCommandSource> setTransplantType = CommandManager
			.literal("settype")
				.then(
					CommandManager
						.argument("type", StringArgumentType.greedyString())
							.executes(SetTransplantType::run)
				)
			.build();
		LiteralCommandNode<ServerCommandSource> getOrganItem = CommandManager
			.literal("itemize")
				.then(
					CommandManager
						.argument("amount", IntegerArgumentType.integer())
							.executes(GetTransplantItem::run)
				)
			.executes(GetTransplantItem::run)
			.build();

		dispatcher.getRoot().addChild(transplantSMP);
		dispatcher.getRoot().addChild(alias);

		transplantSMP.addChild(setTransplantCount);
		transplantSMP.addChild(setTransplantType);
		transplantSMP.addChild(getOrganItem);
	}
}
