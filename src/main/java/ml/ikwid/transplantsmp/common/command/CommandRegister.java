package ml.ikwid.transplantsmp.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.argument.EntityArgumentType;
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

		/* Debugging commands
		LiteralCommandNode<ServerCommandSource> setTransplantCount = CommandManager
			.literal("setcount")
				.then(
					CommandManager
						.argument("count", IntegerArgumentType.integer())
							.executes(CmdSetTransplantCount::run)
				)
			.build();
		LiteralCommandNode<ServerCommandSource> setTransplantType = CommandManager
			.literal("settype")
				.then(
					CommandManager
						.argument("type", StringArgumentType.greedyString())
							.executes(CmdSetTransplantType::run)
				)
			.build();
		 */
		LiteralCommandNode<ServerCommandSource> setTransplantTypeServer = CommandManager
				.literal("setplayertype")
					.then(
						CommandManager
							.argument("player", EntityArgumentType.player())
								.then(
									CommandManager
										.argument("type", StringArgumentType.greedyString())
											.executes(CmdSetTransplantTypeServer::run)
								)
					)
				.build();
		LiteralCommandNode<ServerCommandSource> getOrganItem = CommandManager
			.literal("itemize")
				.then(
					CommandManager
						.argument("amount", IntegerArgumentType.integer())
							.executes(CmdGetTransplantItem::run)
				)
			.executes(CmdGetTransplantItem::run)
			.build();
		LiteralCommandNode<ServerCommandSource> dumpInventory = CommandManager
			.literal("invdump")
			.executes(CmdInventoryDump::run)
			.build();
		LiteralCommandNode<ServerCommandSource> getArmorInfo = CommandManager
			.literal("armor")
			.executes(CmdGetArmorInfo::run)
			.build();

		dispatcher.getRoot().addChild(transplantSMP);
		dispatcher.getRoot().addChild(alias);

		//transplantSMP.addChild(setTransplantCount);
		//transplantSMP.addChild(setTransplantType);
		transplantSMP.addChild(getOrganItem);
		transplantSMP.addChild(dumpInventory);
		transplantSMP.addChild(getArmorInfo);
	}
}
