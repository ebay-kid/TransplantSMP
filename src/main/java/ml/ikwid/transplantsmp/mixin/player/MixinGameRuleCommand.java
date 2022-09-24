package ml.ikwid.transplantsmp.mixin.player;

import com.mojang.brigadier.context.CommandContext;
import ml.ikwid.transplantsmp.common.networking.NetworkingUtil;
import net.minecraft.server.command.GameRuleCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * The gamerule change is only logged server-side, but it needs to be accessed client-side for the ARM_HASTE_BALANCE_AMOUNT in {@link ml.ikwid.transplantsmp.common.gamerule.GameruleRegister} to work visually.
 */

@Mixin(GameRuleCommand.class)
public class MixinGameRuleCommand {
    @Inject(method = "executeSet", at = @At("TAIL"))
    private static <T extends GameRules.Rule<T>> void sendToClients(CommandContext<ServerCommandSource> context, GameRules.Key<T> key, CallbackInfoReturnable<Integer> cir) {
        if(key.getTranslationKey().equals("gamerule.armBalanceAmount")) {
            double val = context.getArgument("value", Double.class);
            List<ServerPlayerEntity> players = context.getSource().getServer().getPlayerManager().getPlayerList();
            NetworkingUtil.sendArmHasteBalanceAmountUpdate(players, val);
        } else if(key.getTranslationKey().equals("gamerule.armBalancing")) {
            boolean val = context.getArgument("value", Boolean.class);
            List<ServerPlayerEntity> players = context.getSource().getServer().getPlayerManager().getPlayerList();
            NetworkingUtil.sendArmBalanceToggleUpdate(players, val);
        }
    }
}
