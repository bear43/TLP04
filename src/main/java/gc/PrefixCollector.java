package gc;

import grammar.Grammar;
import grammar.Rule;
import util.Util;
import util.prefix.DefaultSplitter;
import util.prefix.IPrefixSplitter;
import util.prefix.PrefixFinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrefixCollector implements IGarbageCollector {

    private PrefixFinder prefixFinder;

    private IPrefixSplitter prefixSplitter = new DefaultSplitter();

    private String getOptimalKey(Map<String, List<Rule>> map) {
        String optimalKey = null;
        int currentMaxSize = 0;
        for(Map.Entry<String, List<Rule>> entry : map.entrySet()) {
            if(currentMaxSize < entry.getValue().size()) {
                optimalKey = entry.getKey();
                currentMaxSize = entry.getValue().size();
            }
        }
        return optimalKey;
    }

    @Override
    public void clean(Grammar grammar) {
        System.out.println("Prefix collector starting...");
        prefixFinder = new PrefixFinder(grammar);
        Map<String, List<Rule>> map = prefixFinder.findPrefixRules();
        Map<Character, Map<String, List<Rule>>> rulePrefixList = new HashMap<>();
        for(Map.Entry<String, List<Rule>> entry : map.entrySet()) {
            if(!entry.getValue().isEmpty()) {
                grammar.getNonTerminalCharacterSet().forEach(nonTerminal -> {
                    Util.putNewElement(
                            rulePrefixList,
                            entry.getKey(),
                            entry.getValue().stream()
                                    .filter(x -> x.getLeftOperand().equals(nonTerminal))
                                    .collect(Collectors.toList()),
                            nonTerminal
                    );
                });
            }
        }
        for(Map.Entry<Character, Map<String, List<Rule>>> entry : rulePrefixList.entrySet()) {
            String optimalKey = getOptimalKey(entry.getValue());
            try {
                Character ch = grammar.getNewCharacterWithIncrement();
                System.out.println("Adding new non terminal character " + ch);
                List<Rule> newRules = prefixSplitter.createNewRules(entry.getValue().get(optimalKey), optimalKey, ch);
                prefixSplitter.addNewRuleCharacterIntoRuleList(entry.getValue().get(optimalKey), optimalKey, ch);
                grammar.getRuleSet().addAll(newRules);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
