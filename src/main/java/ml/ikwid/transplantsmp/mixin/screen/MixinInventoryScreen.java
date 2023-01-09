package ml.ikwid.transplantsmp.mixin.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import ml.ikwid.transplantsmp.api.ITransplantable;
import ml.ikwid.transplantsmp.common.transplants.RegisterTransplants;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
	public MixinInventoryScreen(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Inject(method = "drawBackground", at = @At("TAIL"))
	private void drawArmorSlots(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		if(((ITransplantable)(Objects.requireNonNull(Objects.requireNonNull(this.client).player))).getTransplantType() == RegisterTransplants.SKIN_TRANSPLANT) {
			RenderSystem.setShaderTexture(0, InventoryScreen.BACKGROUND_TEXTURE);
			for(int i = 0; i < 4; i++) {
				this.drawTexture(matrices, this.x - Constants.OUTER_SLOT_WIDTH + 4, this.y + 7 + i * (Constants.OUTER_SLOT_HEIGHT - 4), 7, this.backgroundHeight - 25, Constants.OUTER_SLOT_WIDTH - 4, Constants.OUTER_SLOT_HEIGHT - 5);
			}
		}
	}

	@Redirect(method = "isClickOutsideBounds", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;backgroundHeight:I", opcode = Opcodes.GETFIELD, ordinal = 0))
	private int fixBackgroundHeight(InventoryScreen instance) {
		return ((AccessorHandledScreen) instance).getBackgroundHeight() + Constants.HOTBAR_SPACE_IN_INV_SCREEN; // whyyyyyy is code randomly copied instead of calling super
	}
}
