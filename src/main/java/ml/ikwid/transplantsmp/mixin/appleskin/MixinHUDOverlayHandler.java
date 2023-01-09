package ml.ikwid.transplantsmp.mixin.appleskin;

import ml.ikwid.transplantsmp.api.ITransplantable;
import ml.ikwid.transplantsmp.common.transplants.RegisterTransplants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import squeek.appleskin.client.HUDOverlayHandler;

import java.util.Objects;

@Mixin(HUDOverlayHandler.class)
public abstract class MixinHUDOverlayHandler {
    @Redirect(method = "drawSaturationOverlay(Lsqueek/appleskin/api/event/HUDOverlayEvent$Saturation;Lnet/minecraft/client/MinecraftClient;FF)V", at = @At(value = "INVOKE", target = "Lsqueek/appleskin/client/HUDOverlayHandler;drawSaturationOverlay(Lnet/minecraft/client/util/math/MatrixStack;FFLnet/minecraft/client/MinecraftClient;IIF)V", ordinal = 0))
    private void drawSecondSaturationRowIfNeeded(HUDOverlayHandler instance, MatrixStack matrices, float saturationGained, float saturation, MinecraftClient client, int x, int y, float alpha) {
        instance.drawSaturationOverlay(matrices, saturationGained, saturation, client, x, y, alpha);
        ITransplantable transplantable = (ITransplantable)(MinecraftClient.getInstance().player);
        if(Objects.requireNonNull(transplantable).getTransplantedAmount() > 0 && transplantable.getTransplantType() == RegisterTransplants.STOMACH_TRANSPLANT) {
            float overflowedSaturation = saturation + saturationGained <= 20 || saturation >= 20 ? saturationGained : saturation + saturationGained - 20;
            instance.drawSaturationOverlay(matrices, overflowedSaturation, saturation - 20, client, x, y - 10, alpha);
        }
    }

    @Redirect(method = "drawHungerOverlay(Lsqueek/appleskin/api/event/HUDOverlayEvent$HungerRestored;Lnet/minecraft/client/MinecraftClient;IFZ)V", at = @At(value = "INVOKE", target = "Lsqueek/appleskin/client/HUDOverlayHandler;drawHungerOverlay(Lnet/minecraft/client/util/math/MatrixStack;IILnet/minecraft/client/MinecraftClient;IIFZ)V", ordinal = 0))
    private void drawSecondHungerRowIfNeeded(HUDOverlayHandler instance, MatrixStack matrices, int hunger, int foodLevel, MinecraftClient client, int x, int y, float alpha, boolean useRottenTextures) {
        instance.drawHungerOverlay(matrices, hunger, foodLevel, client, x, y, alpha, useRottenTextures);
        ITransplantable transplantable = (ITransplantable)(MinecraftClient.getInstance().player);
        if(Objects.requireNonNull(transplantable).getTransplantedAmount() > 0 && transplantable.getTransplantType() == RegisterTransplants.STOMACH_TRANSPLANT) {
            int overflowedHunger = hunger + foodLevel <= 20 || foodLevel >= 20 ? hunger : hunger + foodLevel - 20;
            instance.drawHungerOverlay(matrices, overflowedHunger, foodLevel - 20, client, x, y - 10, alpha, useRottenTextures);
        }
    }
}
