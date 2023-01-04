package ml.ikwid.transplantsmp.common.transplants;

import ml.ikwid.transplantsmp.api.TransplantType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Arm Transplant.
 * All logic for this is handled elsewhere based on the transplanted amounts so the methods in this class are empty.
 */
public class ArmTransplant extends TransplantType {
    public ArmTransplant(String name, String description, int defaultChangeByAmount) {
        super(name, description, defaultChangeByAmount);
    }

    @Override
    public boolean canTransplant(int newOffset) {
        return newOffset >= -8 && newOffset <= 9;
    }

    @Override
    public void resetTransplantClient(ClientPlayerEntity player) { // nothing
    }

    @Override
    public void updateCountClient(ClientPlayerEntity player, int previousAmount, int newAmount) { // nothing
    }

    @Override
    public void resetTransplantServer(ServerPlayerEntity player) { // nothing
    }

    @Override
    public void updateCountServer(ServerPlayerEntity player, int previousAmount, int newAmount) { // nothing
    }

    @Override
    public void onPlayerRespawn(ServerPlayerEntity player, int amount) { // nothing
    }
}
