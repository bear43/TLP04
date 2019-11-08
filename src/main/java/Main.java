import gc.EpsilonCollector;
import gc.UselessCollector;
import grammar.Grammar;
import util.RuleListFactory;

import java.util.Arrays;
import java.util.HashSet;

public class Main {

    private static Grammar grammar;

    public static void main(String[] args) {
        grammar = new Grammar(
                new HashSet<>(Arrays.asList('x', 'y', 'z', 'k', '#', '$')),
                new HashSet<>(Arrays.asList('S', 'X', 'Y', 'Z', 'K')),
                'S',
                RuleListFactory.createRuleList('S', new String[] { "X", "Y", "Z" }),
                RuleListFactory.createRuleList('X', new String[] {"x#X", "x#Y", Grammar.getEPSILON().toString() }),
                RuleListFactory.createRuleList('Y', new String[] {"Yy$", "Yz$", "$", Grammar.getEPSILON().toString() }),
                RuleListFactory.createRuleList('Z', new String[] { "Zz$" }),
                RuleListFactory.createRuleList('K', new String[] { "Kk$", "k$" }));
        System.out.println(grammar);
        new UselessCollector().clean(grammar);
        new EpsilonCollector().clean(grammar);
        System.out.println(grammar);
    }
}
