package util.prefix;

import grammar.Rule;
import util.RuleListFactory;
import util.Util;

import java.util.ArrayList;
import java.util.List;

public class DefaultSplitter implements IPrefixSplitter {

    private int[] getStartAndEndIndexOfPrefixInARule(StringBuilder rule, String prefix) throws Exception {
        if(prefix.isEmpty()) throw new Exception("Attempt to find start and end index with empty prefix");
        int index = rule.indexOf(prefix);
        if(index == -1) throw new Exception("Trying to split prefixed rule when there is no prefix!");
        return new int[] {index, index + prefix.length()-1};
    }

    @Override
    public List<Rule> createNewRules(List<Rule> prefixedRules, String prefix, Character newRuleCharacter) throws Exception{
        List<Rule> ruleList = RuleListFactory.createCopy(prefixedRules);
        split(ruleList, prefix);
        ruleList.forEach(rule -> rule.setLeftOperand(newRuleCharacter));
        return ruleList;
    }

    @Override
    public void split(List<Rule> prefixedRules, String prefix) throws Exception {
        for(Rule rule : prefixedRules) {
            StringBuilder rightOperand = rule.concatRightOperandToStringBuilder();
            int[] indices = getStartAndEndIndexOfPrefixInARule(rightOperand, prefix);
            String oldRightOperand = rightOperand.toString();
            rightOperand.delete(indices[0], indices[1]+1);
            rule.setRightOperand(rightOperand.toString());
            System.out.println("Splat rule right operand '" + oldRightOperand + "' with its prefix '" + prefix + "'");
        }
    }

    @Override
    public void addNewRuleCharacterIntoRuleList(List<Rule> ruleList, String prefix, Character newRuleCharacter) throws Exception {
        for(Rule rule : ruleList) {
            int[] indices = getStartAndEndIndexOfPrefixInARule(rule.concatRightOperandToStringBuilder(), prefix);
            String newRightOperand;
            if(indices[0] > 0) {
                newRightOperand = newRuleCharacter.toString() + prefix;
            } else {
                newRightOperand = prefix + newRuleCharacter;
            }
            rule.setRightOperand(newRightOperand);
        }
    }
}
