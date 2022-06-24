package ml.ikwid.transplantsmp.mixin.armtransplant;

import com.mojang.blaze3d.systems.RenderSystem;
import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryScreen.class)
public class MixinInventoryScreen {
	private final Identifier WIDGETS_TEXTURE = new Identifier("textures/gui/widgets.png");
	private final InventoryScreen self = (InventoryScreen)(Object) this;
	private final ITransplantable transplantable = (ITransplantable) (MinecraftClient.getInstance().player);

	@Redirect(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
	private void specialRender(InventoryScreen instance, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
		// draw hotbar ourselves with the exact same texture as the HUD because i'm slightly lazy
		RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
		int bottom = y + height;
		int draws = transplantable.getHotbarDraws();
		for(int i = 0; i < draws; i++) {
			self.drawTexture(matrices, x + transplantable.xShift() + (i * TransplantSMPClient.SLOT_WIDTH), bottom, 0, 0, TransplantSMPClient.SLOT_WIDTH, 22);
		}

		if(transplantable.getTransplantType() == TransplantType.SKIN_TRANSPLANT) { // Armor slots
			for(int i = 0; i < 4; i++) {
				self.drawTexture(matrices, 8 - TransplantSMPClient.SLOT_WIDTH, 8 + i * 22, 0, 0, TransplantSMPClient.SLOT_WIDTH, 22);
			}
		}

		RenderSystem.setShaderTexture(0, HandledScreen.BACKGROUND_TEXTURE); // fix it for the rest of the code
		self.drawTexture(matrices, x, y, u, v, width, height);
	}

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
}
