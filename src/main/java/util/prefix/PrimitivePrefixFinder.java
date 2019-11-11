package util.prefix;

import grammar.Rule;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrimitivePrefixFinder implements IPrefixFinderAlg {

    private static final int MIN_PREFIX_CHARACTER_COUNT = 2;

    private static final int MIN_RULE_COUNT = 2;

    @Override
    public Map<String, List<Rule>> find(Collection<Rule> ruleList) {
        int counter = 1;
        String currentRightOperand = null;
        String checkingRightOperand = null;
        boolean enteredLoop = false;
        Map<String, List<Rule>> map = new HashMap<>();
        for (Rule mainRule : ruleList) {
            currentRightOperand = mainRule.concatRightOperandToString();
            for (Rule checkingRule : ruleList) {
                checkingRightOperand = checkingRule.concatRightOperandToString();
                while (
                        counter <= currentRightOperand.length() &&
                        counter <= checkingRightOperand.length() &&
                        currentRightOperand.substring(0, counter).equals(checkingRightOperand.substring(0, counter))) {
                    counter++;
                    enteredLoop = true;
                }
                if(enteredLoop) {
                    counter--;
                    enteredLoop = false;
                }
            }
            if(counter >= MIN_PREFIX_CHARACTER_COUNT) {
                String foundPrefix = currentRightOperand.substring(0, counter);
                if(!map.containsKey(foundPrefix))
                    map.put(foundPrefix, ruleList.stream().filter(x -> x.doesRightOperandContainNextSequence(foundPrefix)).collect(Collectors.toList()));
            }
            counter = 1;
            enteredLoop = false;
        }
        return map;
    }
}
