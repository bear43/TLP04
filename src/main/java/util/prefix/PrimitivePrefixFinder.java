package util.prefix;

import grammar.Rule;
import util.Util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrimitivePrefixFinder implements IPrefixFinderAlg {

    private static final int MIN_PREFIX_CHARACTER_COUNT = 1;

    private static final int MIN_RULE_COUNT = 2;

    @Override
    public Map<String, List<Rule>> find(Collection<Rule> ruleList) {
        int counter = 1;
        int maxCounterOnCycle = 1;
        String currentRightOperand = null;
        String checkingRightOperand = null;
        boolean enteredLoop = false;
        boolean errorFlag = false;
        Map<String, List<Rule>> localStorageMap = new HashMap<>();
        for (Rule mainRule : ruleList) {
            currentRightOperand = mainRule.concatRightOperandToString();
            for (Rule checkingRule : ruleList) {
                checkingRightOperand = checkingRule.concatRightOperandToString();
                while (
                        counter <= currentRightOperand.length() &&
                        counter <= checkingRightOperand.length()) {
                    if(currentRightOperand.substring(0, counter).equals(checkingRightOperand.substring(0, counter))) {
                        counter++;
                        if(errorFlag) break;
                    } else {
                        if(counter > 0) {
                            counter--;
                            errorFlag = true;
                        }
                        else break;
                    }
                    enteredLoop = true;
                }
                if(enteredLoop) {
                    counter--;
                    if(counter != 0) maxCounterOnCycle = counter;
                    enteredLoop = false;
                    errorFlag = false;
                }
            }
            if(maxCounterOnCycle >= MIN_PREFIX_CHARACTER_COUNT) {
                String foundPrefix = currentRightOperand.substring(0, maxCounterOnCycle);
                List<Rule> stream = ruleList.stream().filter(x ->
                        x.doesRightOperandContainNextSequence(foundPrefix) &&
                        !x.concatRightOperandToString().equals(foundPrefix)
                ).collect(Collectors.toList());
                if(stream.size() >= MIN_RULE_COUNT) {
                    System.out.println(String.format("Found prefix '%s'. It contains in %d rules of %s", foundPrefix, stream.size(), mainRule.getLeftOperand().toString()));
                    Util.putNewElement(localStorageMap, foundPrefix, stream);
                }
            }
            counter = 1;
            enteredLoop = false;
        }
        return localStorageMap;
    }
}
