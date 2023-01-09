package ml.ikwid.transplantsmp.common.transplants;

import ml.ikwid.transplantsmp.api.TransplantType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class SkinTransplant extends TransplantType {
    public SkinTransplant(String name, String description, int defaultChangeByAmount) {
        super(name, description, defaultChangeByAmount);
    }

    @Override
    public boolean canTransplant(int newOffset) {
        return newOffset >= -18 && newOffset <= 20;
    }

    @Override
    public void onResetTransplantClient(ClientPlayerEntity player) { // nothing
    }

    @Override
    public void onUpdateCountClient(ClientPlayerEntity player, int previousAmount, int newAmount) { // nothing
    }

    @Override
    public void onResetTransplantServer(ServerPlayerEntity player) { // nothing
    }

    @Override
    public void onUpdateCountServer(ServerPlayerEntity player, int previousAmount, int newAmount) { // nothing
    }

    @Override
    public void onPlayerRespawn(ServerPlayerEntity player, int amount) { // nothing
    }
}
