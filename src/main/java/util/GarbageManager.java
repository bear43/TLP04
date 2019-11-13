package util;

import gc.*;
import grammar.Grammar;
import grammar.Rule;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GarbageManager {
    private List<IGarbageCollector> garbageCollectorPool = new ArrayList<>();

    public GarbageManager() {
        this(Arrays.asList(
                new UselessCollector(),
                new EpsilonCollector(),
                new ChainCollector(),
                new PrefixCollector(),
                new RecursionCollector()
        ));
    }

    public GarbageManager(List<IGarbageCollector> garbageCollectorPool) {
        this.garbageCollectorPool.addAll(garbageCollectorPool);
    }

    public void doWork(Grammar grammar) {
        garbageCollectorPool.forEach(x -> x.clean(grammar));
        clearGrammarRulesFromDups(grammar);
    }

    private void clearGrammarRulesFromDups(Grammar grammar) {
        grammar.setRuleSet(grammar.getRuleSet().stream().collect(Collectors.toSet()));
/*        grammar.getNonTerminalCharacterSet().forEach(ch -> {
            Set<Rule> ruleSet = grammar.getRuleSetByNonTerminal(ch);
            int copyCounter;
            List<Rule> removeList = new ArrayList<>();
            for(Rule mainRule : ruleSet) {
                copyCounter = 0;
                for(Rule checkerRule : ruleSet) {
                    if(mainRule.equals(checkerRule)) {
                        copyCounter++;
                        if(copyCounter > 1) {
                            removeList.add(checkerRule);
                        }
                    }
                }
            }
            removeList.forEach(ruleSet::remove);
        });*/
    }

    public Grammar doWorkAndKeepOldGrammar(Grammar grammar) {
        for(IGarbageCollector gc : garbageCollectorPool) {
            grammar = gc.keepOldAndCleanNew(grammar);
        }
        return grammar;
    }
}
