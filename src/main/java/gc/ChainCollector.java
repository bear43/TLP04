package gc;

import grammar.Grammar;
import grammar.Rule;
import org.apache.commons.lang3.CharSequenceUtils;
import util.ChainBuilder;
import util.RuleListFactory;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class ChainCollector implements IGarbageCollector {

    private final static int MIN_CHAIN_SIZE_TO_CUT = 2;


    private void doReduce(Grammar grammar, Character[] charArray) {
        Character firstChar = charArray[0];
        Character lastChar = charArray[charArray.length-1];
        if(charArray.length >= 2 && !charArray[0].equals(charArray[1])) {
            grammar.getRuleList().addAll(
                    RuleListFactory.createRuleList(firstChar, grammar.getRuleSetByNonTerminal(lastChar))
            );
            List<Rule> removeList = new ArrayList<>();
            for (int index = 0; index < charArray.length - 1; index++) {
                final int i = index;
                grammar.getRuleList().stream()
                        .filter(x ->
                                x != null &&
                                        x.getLeftOperand().equals(charArray[i]) &&
                                        x.isPureNonTerminalInRightOperand(grammar.getTerminalCharacterSet(), grammar.getNonTerminalCharacterSet()) &&
                                        x.doesRightOperandContainNonTerminal(charArray[i + 1]))
                        .forEach(x -> {
                            System.out.println("Removing chained rule " + x.toString());
                            removeList.add(x);
                        });
            }
            removeList.forEach(grammar.getRuleList()::remove);
        } else {
            grammar.getRuleSetByNonTerminal(firstChar).stream()
                    .filter(x -> x.doesRightOperandContainNonTerminal(lastChar))
                    .map(x -> {
                        List<Character> list = x.getRightOperand();
                        List<Character> newList = new ArrayList<>();
                        list.forEach(y -> {
                            if(!grammar.getNonTerminalCharacterSet().contains(y))
                                newList.add(y);
                        });
                        return newList;
                    })
                    .forEach(x -> {
                        StringBuilder sb = new StringBuilder();
                        x.forEach(sb::append);
                        System.out.println("Adding new rule for recursive rule " + firstChar.toString());
                        grammar.getRuleList().add(new Rule(firstChar, sb.toString()));
                    });
        }
    }

    @Override
    public void clean(Grammar grammar) {
        System.out.println("Chain collector starting...");
        ChainBuilder cb = new ChainBuilder(grammar);
        cb.buildCommonChain();
        cb.buildAllChain();
        List<List<Character>> list = new ArrayList<>();
        grammar.getNonTerminalCharacterSet().forEach( x-> {
            list.addAll(cb.getCharacterMap().getOrDefault(x, new ArrayList<>()).stream()
                    .filter(y ->
                            y.size() >= MIN_CHAIN_SIZE_TO_CUT)
                    .collect(Collectors.toList()));
        });
        list.sort(Comparator.comparingInt(List::size));
        list.forEach(chain -> {
            Character[] charArray = new Character[chain.size()];
            chain.toArray(charArray);
            doReduce(grammar, charArray);
        });
        System.out.println("Cleaner job is done");
    }
}
