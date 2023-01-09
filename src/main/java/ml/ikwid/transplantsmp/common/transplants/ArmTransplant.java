package ml.ikwid.transplantsmp.common.transplants;

import ml.ikwid.transplantsmp.api.TransplantType;
import ml.ikwid.transplantsmp.api.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
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

    public int getHotbarDraws(PlayerEntity player) {
        return ((ITransplantable) player).getRawTransplantedAmount() + 9;
    }

    public int xShift(PlayerEntity player) {
        return -((this.getHotbarDraws(player) - 9) * Constants.OUTER_SLOT_WIDTH / 2);
    }
}
