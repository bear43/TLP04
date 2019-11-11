package util.prefix;

import grammar.Rule;

import java.util.List;

public interface IPrefixSplitter {
    List<Rule> createNewRules(List<Rule> prefixedRules, String prefix, Character newRuleCharacter) throws Exception;
    void split(List<Rule> prefixedRules, String prefix) throws Exception;
    void addNewRuleCharacterIntoRuleList(List<Rule> ruleList, String prefix, Character newRuleCharacter) throws Exception;
}
