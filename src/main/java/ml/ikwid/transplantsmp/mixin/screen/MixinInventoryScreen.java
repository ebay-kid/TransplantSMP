package ml.ikwid.transplantsmp.mixin.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
	public MixinInventoryScreen(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Inject(method = "drawBackground", at = @At("TAIL"))
	private void drawArmorSlots(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		if(((ITransplantable)(Objects.requireNonNull(Objects.requireNonNull(this.client).player))).getTransplantType() == TransplantType.SKIN_TRANSPLANT) {
			RenderSystem.setShaderTexture(0, InventoryScreen.BACKGROUND_TEXTURE);
			for(int i = 0; i < 4; i++) {
				this.drawTexture(matrices, -14, 6 + i * Constants.OUTER_SLOT_HEIGHT, 0, this.backgroundHeight - Constants.HOTBAR_SPACE_IN_INV_SCREEN, Constants.OUTER_SLOT_WIDTH, Constants.OUTER_SLOT_HEIGHT);
			}
		}
	}
	/*
	@Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", ordinal = 0))
	private Element screwTheRecipeBook(InventoryScreen instance, Element element) {
		return null;
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;isOpen()Z", ordinal = 0))
	private boolean itIsNotOpen(RecipeBookWidget instance) {
		return false;
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", ordinal = 1))
	private void screwTheRecipeBook2(RecipeBookWidget instance, MatrixStack matrices, int mouseX, int mouseY, float delta) {
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;drawGhostSlots(Lnet/minecraft/client/util/math/MatrixStack;IIZF)V", ordinal = 0))
	private void screwTheRecipeBook3(RecipeBookWidget instance, MatrixStack matrices, int x, int y, boolean bl, float delta) {
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;drawTooltip(Lnet/minecraft/client/util/math/MatrixStack;IIII)V", ordinal = 0))
	private void screwTheRecipeBook4(RecipeBookWidget instance, MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
	}
	*/
}
