package ml.ikwid.transplantsmp.client.screen;

import ml.ikwid.transplantsmp.common.TransplantType;
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

	private int height = client.getWindow().getHeight();
	private int width = client.getWindow().getWidth();

	private final ButtonWidget leftArrow = new ButtonWidget(5, this.height - 14, 10, 10, Text.of("<"), b -> {
		if(this.index > 0) {
			index--;
		}
	});

	private final ButtonWidget rightArrow = new ButtonWidget(leftArrow.x + 15, leftArrow.y, 10, 10, Text.of(">"), b -> {
		if (this.index < TransplantType.transplantTypes.length - 1) {
			this.index++;
		}
	});

	private final ButtonWidget choose = new ButtonWidget(this.width - 50, leftArrow.y, 40, 10, Text.of("Choose"), b -> {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeString(TransplantType.transplantTypes[index].toString());
		ClientPlayNetworking.send(NetworkingIDs.CHOOSE_TRANSPLANT_TYPE_C2S, buf);
	});

	private final ButtonWidget[] widgets = { leftArrow, rightArrow, choose };

	public ChooseTransplantScreen() {
		super(Text.of("Choose a transplant"));
		this.background = true;
	}

	@Override
	protected void init() {
		super.init();

		addDrawableChild(this.leftArrow);
		addDrawableChild(this.rightArrow);
		addDrawableChild(this.choose);
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
		if(Objects.requireNonNull(client.currentScreen).height != this.height || client.currentScreen.width != this.width) {
			for(ButtonWidget widget : widgets) {
				widget.y = client.currentScreen.height - 14;
			}
			this.choose.x = client.currentScreen.width - 50;

			this.width = client.currentScreen.width;
			this.height = client.currentScreen.height;
		}

		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		String desc = TransplantType.transplantTypes[index].getDescription();
		String[] descLines = desc.split("\n");

		for(int i = 0; i < descLines.length; i++) {
			String descLine = descLines[i];
			textRenderer.draw(matrices, descLine, (client.currentScreen.width - (descLine.length() * 5)) / 2.0f, ((client.currentScreen.height - (descLines.length * 17)) / 2.0f) + i * 17, 0xCCCCCC);
		}
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
