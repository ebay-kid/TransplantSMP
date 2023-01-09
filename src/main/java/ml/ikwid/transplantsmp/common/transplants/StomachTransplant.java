package ml.ikwid.transplantsmp.common.transplants;

import ml.ikwid.transplantsmp.api.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.IStomachTransplanted;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class StomachTransplant extends TransplantType {
    public StomachTransplant(String name, String description, int defaultChangeByAmount) {
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
    public void onUpdateCountClient(ClientPlayerEntity player, int previousAmount, int newAmount) {
        updateHungerManager(player, newAmount);
    }

    @Override
    public void onResetTransplantServer(ServerPlayerEntity player) {
        player.getHungerManager().setFoodLevel(20);
    }

    @Override
    public void onUpdateCountServer(ServerPlayerEntity player, int previousAmount, int newAmount) {
        updateHungerManager(player, newAmount);
    }

    @Override
    public void onPlayerRespawn(ServerPlayerEntity player, int amount) {
        player.getHungerManager().setFoodLevel(20 + amount);
    }

    private void updateHungerManager(PlayerEntity player, int newAmount) {
        ((IStomachTransplanted) (player.getHungerManager())).setMaxFoodLevel(20 + newAmount);
    }
}
