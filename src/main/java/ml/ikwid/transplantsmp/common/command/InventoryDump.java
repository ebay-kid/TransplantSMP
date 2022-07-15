package ml.ikwid.transplantsmp.common.command;

import com.mojang.brigadier.context.CommandContext;
import ml.ikwid.transplantsmp.TransplantSMP;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

public class InventoryDump {
	public static int run(CommandContext<ServerCommandSource> ctx) {
		ServerPlayerEntity player = ctx.getSource().getPlayer();
		if(player == null) {
			throw new CommandException(Text.of("Cannot invdump a non-player"));
		}
		PlayerInventory inventory = player.getInventory();
		TransplantSMP.LOGGER.info("Inventory dump for " + player.getName().getString() + ":");

		TransplantSMP.LOGGER.info("Main: ");
		DefaultedList<ItemStack> main = inventory.main;
		for(int i = 0; i < main.size(); i++) {
			TransplantSMP.LOGGER.info(i + ": " + main.get(i));
		}

		TransplantSMP.LOGGER.info("Armor: ");
		DefaultedList<ItemStack> armor = inventory.armor;
		for(int i = 0; i < armor.size(); i++) {
			TransplantSMP.LOGGER.info(i + ": " + armor.get(i));
		}

		TransplantSMP.LOGGER.info("Off Hand: ");
		DefaultedList<ItemStack> offhand = inventory.offHand;
		for(int i = 0; i < offhand.size(); i++) {
			TransplantSMP.LOGGER.info(i + ": " + offhand.get(i));
		}

		return 1;
	}
}
