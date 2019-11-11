package util.prefix;

import grammar.Grammar;
import grammar.Rule;
import lombok.Data;

import java.util.*;

@Data
public class PrefixFinder {
    private Grammar grammar;

    private IPrefixFinderAlg prefixFinderAlg = new PrimitivePrefixFinder();

    public PrefixFinder(Grammar grammar) {
        this.grammar = grammar;
    }

    public Map<String, List<Rule>> findPrefixRules() {
        Map<String, List<Rule>> resultMap = new HashMap<>();
        for(Map.Entry<Character, Collection<Rule>> entry : grammar.getCharacterRuleMap().entrySet()) {
            resultMap.putAll(prefixFinderAlg.find(entry.getValue()));
        }
        return resultMap;
    }
}
