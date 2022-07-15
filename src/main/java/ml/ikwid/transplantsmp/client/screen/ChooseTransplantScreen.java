package ml.ikwid.transplantsmp.client.screen;

import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.networking.NetworkingConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class ChooseTransplantScreen extends Screen {
	private static final MinecraftClient client = MinecraftClient.getInstance();
	private static final int width = client.currentScreen.width;
	private static final int height = client.currentScreen.height;

	protected final boolean background;
	private int index = 0;

	public ChooseTransplantScreen() {
		super(Text.of("Choose a transplant"));
		this.background = true;
	}

	@Override
	protected void init() {
		super.init();

		addDrawableChild(new ButtonWidget(5, height - 20, 10, 10, Text.of("<"), b -> {
			if(this.index > 0) {
				index--;
			}
		}));

		addDrawableChild(new ButtonWidget(15, height - 20, 10, 10, Text.of(">"), b-> {
			if(this.index < TransplantType.transplantTypes.length - 1) {
				this.index++;
			}
		}));

		addDrawableChild(new ButtonWidget(width - 50, height - 20, 40, 10, Text.of("Choose"), b-> {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeString(TransplantType.transplantTypes[index].toString());
			ClientPlayNetworking.send(NetworkingConstants.CHOOSE_TRANSPLANT_TYPE, buf);
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
		String desc = TransplantType.transplantTypes[index].getDescription();
		String[] descLines = desc.split("\n");
		// TransplantSMP.LOGGER.info(descLines.length);
		for(int i = 0; i < descLines.length; i++) {
			textRenderer.draw(matrices, descLines[i], width / 3f, height / 2f + i * 20, 0xCCCCCC);
		}
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
