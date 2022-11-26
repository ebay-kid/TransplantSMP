package ml.ikwid.transplantsmp.mixin.screen.ihateminecraftnotcallingsuper;

import ml.ikwid.transplantsmp.common.util.Constants;
import ml.ikwid.transplantsmp.mixin.screen.AccessorHandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LoomScreen.class)
public abstract class MixinLoomScreen extends HandledScreen<LoomScreenHandler> {
    public MixinLoomScreen(LoomScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Redirect(method = "isClickOutsideBounds", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/ingame/LoomScreen;backgroundHeight:I", opcode = Opcodes.GETFIELD))
    private int fixBackgroundHeight(LoomScreen instance) {
        return ((AccessorHandledScreen) instance).getBackgroundHeight() + Constants.HOTBAR_SPACE_IN_INV_SCREEN; // whyyyyyy is code randomly copied instead of calling super
    }
}
