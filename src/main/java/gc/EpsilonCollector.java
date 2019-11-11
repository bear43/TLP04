package gc;

import grammar.Grammar;
import grammar.Rule;
import util.RuleListFactory;

import java.util.List;

public class EpsilonCollector implements IGarbageCollector {

    @Override
    public void clean(Grammar grammar) {
        System.out.println("Epsilon cleaner starting...");
        List<Rule> removeRules = grammar.getEpsilonRules();
        if(removeRules.stream().anyMatch(x -> grammar.nonTerminalService.isDerivedByStartSymbol(x.getLeftOperand(), true))) {
            System.out.println("Subset has connection with start sym");
            Character newCharacter = grammar.getNewCharacterWithIncrement();
            List<Rule> newRules = RuleListFactory.createRuleList(newCharacter,
                    new String[]{grammar.getStartSymbol().toString(), Grammar.getEPSILON().toString() });
            grammar.setStartSymbol(newCharacter);
            newRules.forEach(grammar.getRuleSet()::add);
            System.out.println("New start sym has been added");
        }
        removeRules.forEach(grammar.getRuleSet()::remove);
        System.out.println("Obsolete rules have been removed");
        System.out.println("Cleaner job is done");
    }
}
