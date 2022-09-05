package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.KillCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KillCommand.class)
public abstract class MixinKillCommand {
    @Redirect(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;kill()V"))
    private static void checkIfPlayerIsSettingTransplant(Entity entity, ServerCommandSource source) {
        if(entity instanceof ServerPlayerEntity player) {
            if(((ITransplantable) player).getIsSettingTransplant()) {
                if(source.isExecutedByPlayer()) {
                    //noinspection ConstantConditions
                    source.getPlayer().sendMessage(Text.of("Can't /kill " + player.getName().getString() + " while they're setting transplant! (it will crash the server)"));
                } else {
                    TransplantSMP.LOGGER.info("Can't /kill " + player.getName().getString() + " while they're setting transplant! (it will crash the server)");
                }
                return;
            }
        }
        entity.kill();
    }
}
