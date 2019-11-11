package util;

import com.sun.istack.internal.NotNull;
import grammar.Rule;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RuleListFactory {
    public static List<Rule> createRuleList(Character leftOperand, @NotNull String[] rightOperands) {
        return Arrays.stream(rightOperands).map(x -> new Rule(leftOperand, x)).collect(Collectors.toList());
    }

    public static List<Rule> createRuleList(Character leftOperand, @NotNull  Collection<Rule> ruleCollection) {
        return ruleCollection.stream().map(x -> {
            Rule rule = SerializationUtils.clone(x);
            rule.setLeftOperand(leftOperand);
            return rule;
        }).collect(Collectors.toList());
    }
}
