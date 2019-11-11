package util.prefix;

import grammar.Grammar;
import grammar.Rule;
import lombok.Data;
import util.Util;

import java.util.*;

@Data
public class PrefixFinder {
    private Grammar grammar;

    private IPrefixFinderAlg prefixFinderAlg = new PrimitivePrefixFinder();

    private Map<String, List<Rule>> storage = new HashMap<>();

    public PrefixFinder(Grammar grammar) {
        this.grammar = grammar;
    }

    public Map<String, List<Rule>> findPrefixRules() {
        grammar.getNonTerminalCharacterSet().forEach(nonTerminal -> {
            for(Map.Entry<String, List<Rule>> entry : prefixFinderAlg.find(grammar.getRuleSetByNonTerminal(nonTerminal)).entrySet()) {
                Util.putNewElement(storage, entry.getKey(), entry.getValue());
            }
        });
        return storage;
    }
}
