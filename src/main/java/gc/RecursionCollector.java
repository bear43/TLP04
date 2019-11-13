package gc;

import grammar.Grammar;
import grammar.Rule;
import util.prefix.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecursionCollector implements IGarbageCollector {

    private IPrefixSplitter prefixSplitter = new DefaultSplitter();

    @Override
    public void clean(Grammar grammar) {
        System.out.println("Recursion collector starting...");
        grammar.getNonTerminalCharacterSet().forEach(nonTerminal -> {
            try {
                List<Rule> oldRules = new ArrayList<>(grammar.getRuleSetByNonTerminal(nonTerminal));
                Character newCharacter = grammar.getNewCharacterWithIncrement();
                List<Rule> newRules = prefixSplitter.createNewRules(
                        oldRules,
                        nonTerminal.toString(),
                        newCharacter
                );
                if(!newRules.isEmpty()) {
                    System.out.println(String.format("Found recrusion in %s -> "));
                    prefixSplitter.addNewRuleCharacterIntoRuleList(
                            newRules,
                            nonTerminal.toString(),
                            newCharacter
                    );
                }
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        System.out.println("Recursion collector job is done");
    }
}
