package ml.ikwid.transplantsmp.common.command;

import com.mojang.brigadier.context.CommandContext;
import ml.ikwid.transplantsmp.TransplantSMP;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

@SuppressWarnings("unused")
public class CmdGetArmorInfo {
	public static int run(CommandContext<ServerCommandSource> ctx) {
		ServerPlayerEntity playerEntity = ctx.getSource().getPlayer();
		TransplantSMP.LOGGER.info("armor info -- active bars: " + Objects.requireNonNull(playerEntity).getArmor() + ", toughness: " + playerEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS) + ", anti kb: " + playerEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));

		return 0;
	}
}
