package ml.ikwid.transplantsmp.common.transplants;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.api.TransplantType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;

public class HeartTransplant extends TransplantType {
    public HeartTransplant(String name, String description, int defaultChangeByAmount) {
        super(name, description, defaultChangeByAmount);
    }

    @Override
    public boolean canTransplant(int newAmount) {
        return newAmount >= -18 && newAmount <= 20;
    }

    @Override
    public void resetTransplantClient(ClientPlayerEntity player) { // nothing
    }

    @Override
    public void updateCountClient(ClientPlayerEntity player, int previousAmount, int newAmount) { // nothing
    }

    @Override
    public void resetTransplantServer(ServerPlayerEntity player) {
        EntityAttributeInstance attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (attribute == null) {
            TransplantSMP.LOGGER.warn("uh oh");
        } else {
            attribute.setBaseValue(20);
        }
    }

    @Override
    public void updateCountServer(ServerPlayerEntity player, int previousAmount, int newAmount) {
        EntityAttributeInstance attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (attribute == null) {
            TransplantSMP.LOGGER.warn("attribute shouldn't be null uhhh you're kinda screwed");
            return;
        }
        attribute.setBaseValue(20 + newAmount);

        if(newAmount < previousAmount && 20 + newAmount < player.getHealth()) {
            player.setHealth(20 + newAmount); // Set health properly after a command is used to decrease max amount.
        }
    }

    @Override
    public void onPlayerRespawn(ServerPlayerEntity player, int amount) {
        player.setHealth(20 + amount);
    }
}
