package ml.ikwid.transplantsmp.common.util.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class HUDRenderUtil {
    public static void renderArmorBars(MatrixStack matrices, int scaledHeight, int renderHealthValue, int scaledWidth, int transplants) {
        InGameHud inGameHud = MinecraftClient.getInstance().inGameHud;
        int o = scaledHeight - 39;
        int x;
        int i = MathHelper.ceil(Objects.requireNonNull(MinecraftClient.getInstance().player).getHealth());
        int m = scaledWidth / 2 - 91;
        float f = Math.max((float) MinecraftClient.getInstance().player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH), (float) Math.max(renderHealthValue, i)); // renderHealthValue aka "j"
        int p = MathHelper.ceil(MinecraftClient.getInstance().player.getAbsorptionAmount());
        int q = MathHelper.ceil((f + (float) p) / 2.0F / 10.0F); // shifts up based on how many hearts
        int r = Math.max(10 - (q - 2), 3);
        int s = o - (q - 1) * r - 10; // height stuff which i don't understand
        int u = MinecraftClient.getInstance().player.getArmor() - 20; // shift back by 20 to handle already-drawn textures
        if(MinecraftClient.getInstance().player.getArmor() == 0) {
            return;
        }
        // Absorption doesn't get stacked when the player max health is <= 20 (10 hearts, aka vanilla).
        // So, calculate the shift from 0 absorption -> 4 absorption at 20 health
        // 0 absorption, s = o + 10
        // 4 absorption, s = o + 20
        // so in theory, we can call InGameHud.drawTexture(matrices, x, s + 10, u, v, width, height) with this
        for (int w = 0; w < transplants; w++) { // render however many extra there are
            x = m + w * 8;
            if (w * 2 + 1 < u) {
                inGameHud.drawTexture(matrices, x, s - 10, 34, 9, 9, 9);
            }
            if (w * 2 + 1 == u) {
                inGameHud.drawTexture(matrices, x, s - 10, 25, 9, 9, 9);
            }
            if (w * 2 + 1 > u) {
                inGameHud.drawTexture(matrices, x, s - 10, 16, 9, 9, 9);
            }
        }
    }

    public static void renderHungerBars(MatrixStack matrices, LivingEntity riddenEntity, int scaledHeight, int scaledWidth, int transplants, int ticks, Random random) {
        if(riddenEntity != null) {
            return;
        }
        int o = scaledHeight - 39;

        // Hopefully I can straight copy-paste this crap into here and just shift it up
        int k = Objects.requireNonNull(MinecraftClient.getInstance().player).getHungerManager().getFoodLevel() - 20;

        int n = scaledWidth / 2 + 91;
        int y;
        int z;
        int aa;
        int ab;
        int ac;
        for(y = 0; y < transplants; ++y) {
            z = o;
            aa = 16;
            ab = 0;
            if (MinecraftClient.getInstance().player.hasStatusEffect(StatusEffects.HUNGER)) {
                aa += 36;
                ab = 13;
            }

            if (MinecraftClient.getInstance().player.getHungerManager().getSaturationLevel() <= 0.0F && ticks % (k * 3 + 1) == 0) {
                z = o + (random.nextInt(3) - 1);
            }

            ac = n - y * 8 - 9;
            // why 10? it apparently works for the previous one so YOLO (also it shifts around by 10)
            MinecraftClient.getInstance().inGameHud.drawTexture(matrices, ac, z - 10, 16 + ab * 9, 27, 9, 9); // wtf why is this different
            if (y * 2 + 1 < k) {
                MinecraftClient.getInstance().inGameHud.drawTexture(matrices, ac, z - 10, aa + 36, 27, 9, 9);
            }

            if (y * 2 + 1 == k) {
                MinecraftClient.getInstance().inGameHud.drawTexture(matrices, ac, z - 10, aa + 45, 27, 9, 9);
            }
        }
    }
}
