package util;

import com.sun.istack.internal.NotNull;
import grammar.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RuleListFactory {
    public static List<Rule> createRuleList(Character leftOperand, @NotNull String[] rightOperands) {
        return Arrays.stream(rightOperands).map(x -> new Rule(leftOperand, x)).collect(Collectors.toList());
    }
}
