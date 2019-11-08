package gc;

import grammar.Grammar;

import java.util.ArrayList;
import java.util.List;

public class UselessCollector implements IGarbageCollector {

    private void deleteNonTerminalsDoesntLeadsToTerminal(Grammar grammar) {
        List<Character> removeList = new ArrayList<>();
        grammar.getNonTerminalCharacterSet().forEach(x -> {
            if(grammar.nonTerminalService.leadsToTerminal(x, true)) {
                System.out.println("Non terminal '"+ x + "' leads to terminal");
            } else {
                System.out.println("Non terminal '"+ x + "' doesn't lead to terminal");
                removeList.add(x);
            }
        });
        removeList.forEach(grammar::removeNonTerminal);
    }

    private void deleteNonTerminalsDoesntLeadsToStartSymbol(Grammar grammar) {
        List<Character> removeList = new ArrayList<>();
        grammar.getNonTerminalCharacterSet().forEach(x -> {
            if(grammar.nonTerminalService.isDerivedByStartSymbol(x, true)) {
                System.out.println("Non terminal '"+ x + "' is derived from start symbol");
            } else {
                System.out.println("Non terminal '"+ x + "' isn't derived from start symbol");
                removeList.add(x);
            }
        });
        removeList.forEach(grammar::removeNonTerminal);
    }

    private void deleteUnreachableTerminals(Grammar grammar) {
        List<Character> removeList = new ArrayList<>();
        grammar.getTerminalCharacterSet().forEach(x -> {
            if(grammar.terminalService.isDerivedByStartSymbol(x)) {
                System.out.println("Terminal '"+ x + "' is derived from start symbol");
            } else {
                System.out.println("Terminal '"+ x + "' isn't derived from start symbol");
                removeList.add(x);
            }
        });
        removeList.forEach(grammar::removeTerminal);
    }

    @Override
    public void clean(Grammar grammar) {
        System.out.println("Useless cleaner starting...");
        deleteNonTerminalsDoesntLeadsToTerminal(grammar);
        deleteNonTerminalsDoesntLeadsToStartSymbol(grammar);
        deleteUnreachableTerminals(grammar);
        System.out.println("Cleaner job is done");
    }
}
