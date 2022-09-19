package ml.ikwid.transplantsmp.common.gamerule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.GameRules;

public class GameruleRegister {
    public static GameRules.Key<GameRules.BooleanRule> SHOULD_BALANCE_ARMOR;
    public static GameRules.Key<DoubleRule> ARMOR_BAR_BALANCE_AMOUNT;
    public static GameRules.Key<DoubleRule> ARMOR_TOUGHNESS_INCREASING_BALANCE_AMOUNT;
    public static GameRules.Key<DoubleRule> ARMOR_TOUGHNESS_DECREASING_BALANCE_AMOUNT;

    public static GameRules.Key<GameRules.BooleanRule> SHOULD_BALANCE_ARM;
    public static GameRules.Key<DoubleRule> ARM_HASTE_BALANCE_AMOUNT;

    public static void register() {
        SHOULD_BALANCE_ARMOR = GameRuleRegistry.register("armorBalancing", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
        ARMOR_BAR_BALANCE_AMOUNT = GameRuleRegistry.register("armorBalanceAmount", GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(1.0));
        ARMOR_TOUGHNESS_INCREASING_BALANCE_AMOUNT = GameRuleRegistry.register("armorToughnessIncBalanceAmount", GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(1.0));
        ARMOR_TOUGHNESS_DECREASING_BALANCE_AMOUNT = GameRuleRegistry.register("armorToughnessDecBalanceAmount", GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(1.0));

        SHOULD_BALANCE_ARM = GameRuleRegistry.register("armBalancing", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
        ARM_HASTE_BALANCE_AMOUNT = GameRuleRegistry.register("armHasteBalanceAmount", GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(1.0));
    }
}