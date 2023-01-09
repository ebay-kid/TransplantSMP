package ml.ikwid.transplantsmp.common.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.context.CommandContext;
import ml.ikwid.transplantsmp.TransplantSMP;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public class CmdInventoryDump {
	public static int run(CommandContext<ServerCommandSource> ctx) {
		ServerPlayerEntity player = ctx.getSource().getPlayer();
		if(player == null) {
			throw new CommandException(Text.of("Cannot invdump a non-player"));
		}
		PlayerInventory inventory = player.getInventory();
		TransplantSMP.LOGGER.info("Inventory dump for " + player.getName().getString() + ":");

		List<DefaultedList<ItemStack>> combinedInventory = ImmutableList.of(inventory.main, inventory.armor, inventory.offHand);
		int idx = 0;

		for (DefaultedList<ItemStack> defaultedList : combinedInventory) {
			for (ItemStack itemStack : defaultedList) {
				TransplantSMP.LOGGER.info(idx++ + ": " + itemStack);
			}
		}

		return 1;
	}
}
