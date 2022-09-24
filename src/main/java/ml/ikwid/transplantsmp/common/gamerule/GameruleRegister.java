package ml.ikwid.transplantsmp.common.gamerule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.GameRules;


public class GameruleRegister {

    public static GameRules.Key<GameRules.BooleanRule> SHOULD_BALANCE_ARM;
    public static GameRules.Key<DoubleRule> ARM_HASTE_BALANCE_AMOUNT;

    public static void register() {
        SHOULD_BALANCE_ARM = GameRuleRegistry.register("armBalancing", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
        ARM_HASTE_BALANCE_AMOUNT = GameRuleRegistry.register("armBalanceAmount", GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(1.0));
    }
}