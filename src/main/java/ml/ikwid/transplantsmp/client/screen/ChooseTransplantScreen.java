package ml.ikwid.transplantsmp.client.screen;

import ml.ikwid.transplantsmp.api.TransplantType;
import ml.ikwid.transplantsmp.api.TransplantTypes;
import ml.ikwid.transplantsmp.common.networking.NetworkingIDs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ChooseTransplantScreen extends Screen {
	private static final MinecraftClient client = MinecraftClient.getInstance();

	protected final boolean background;
	private int index = 0;

	protected final TransplantType[] transplantTypes = TransplantTypes.getTransplantTypes().toArray(new TransplantType[4]);

	public ChooseTransplantScreen() {
		super(Text.of("Choose a transplant"));
		this.background = true;
	}

	@SuppressWarnings("unused")
	@Override
	protected void init() {
		super.init();

		int height = Objects.requireNonNull(client.currentScreen).height;
		int width = client.currentScreen.width;

		ButtonWidget leftArrow = this.addDrawableChild(new ButtonWidget(5, height - 14, 10, 10, Text.of("<"), b -> {
			if (this.index > 0) {
				index--;
			}
		}));

		ButtonWidget rightArrow = this.addDrawableChild(new ButtonWidget(leftArrow.x + 15, leftArrow.y, 10, 10, Text.of(">"), b -> {
			if (this.index < this.transplantTypes.length - 1) {
				this.index++;
			}
		}));

		ButtonWidget choose = this.addDrawableChild(new ButtonWidget(width - 50, leftArrow.y, 40, 10, Text.of("Choose"), b -> {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeString(this.transplantTypes[index].toString());
			ClientPlayNetworking.send(NetworkingIDs.CHOOSE_TRANSPLANT_TYPE_C2S, buf);
		}));
	}

	@Override
	public void renderBackground(MatrixStack matrices, int vOffset) {
		if(background) {
			super.renderBackgroundTexture(vOffset);
		} else {
			super.renderBackground(matrices, vOffset);
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		String desc = this.transplantTypes[index].getDescription();
		String[] descLines = desc.split("\n");

		// Centers the description lines
		for(int i = 0; i < descLines.length; i++) {
			String descLine = descLines[i];
			textRenderer.draw(matrices, descLine, (Objects.requireNonNull(client.currentScreen).width - (descLine.length() * 5)) / 2.0f, ((client.currentScreen.height - (descLines.length * 17)) / 2.0f) + i * 17, 0xCCCCCC); // amazing code
		}
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
