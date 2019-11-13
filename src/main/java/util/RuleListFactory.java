package util;
import grammar.Rule;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RuleListFactory {
    public static List<Rule> createRuleList(Character leftOperand, String[] rightOperands) {
        return Arrays.stream(rightOperands).map(x -> new Rule(leftOperand, x)).collect(Collectors.toList());
    }

    public static List<Rule> createRuleList(Character leftOperand,  Collection<Rule> ruleCollection) {
        return ruleCollection.stream().map(x -> {
            Rule rule = SerializationUtils.clone(x);
            rule.setLeftOperand(leftOperand);
            return rule;
        }).collect(Collectors.toList());
    }

    public static List<Rule> createNRule(int count, Character leftOperand, String rightOperand) {
        List<Rule> ruleList = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            ruleList.add(new Rule(leftOperand, rightOperand));
        }
        return ruleList;
    }

    public static List<Rule> createCopy(List<Rule> ruleList) {
        List<Rule> clonedRuleList = new ArrayList<>();
        ruleList.forEach(rule -> {
            List<Character> charList = new ArrayList<>(rule.getRightOperand());
            clonedRuleList.add(new Rule(rule.getLeftOperand(), charList));
        });
        return clonedRuleList;
    }
}
