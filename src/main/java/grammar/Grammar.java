package grammar;

import lombok.Data;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class Grammar implements Cloneable, Serializable {
    private Set<Character> terminalCharacterSet;

    private Set<Character> nonTerminalCharacterSet;

    private Set<Rule> ruleSet;

    private Character startSymbol;

    private Character newNonTerminalCharacter = 'Ψ';

    public class TerminalService implements Serializable {
        public Set<Character> getParentsAsCharSet(Character terminal) {
            return ruleSet.stream().filter(x ->
                    x.doesRightOperandContainTerminal(terminal)
            ).map(Rule::getLeftOperand).collect(Collectors.toSet());
        }

        public Set<Rule> getParentsAsRuleSet(Character terminal) {
            return getRuleSetByNonTerminalCharStream(getParentsAsCharSet(terminal).stream());
        }

        public Set<Set<Rule>> getParentsAsSetOfRuleSet(Character terminal) {
            return getParentsAsCharSet(terminal).stream().map(Grammar.this::getRuleSetByNonTerminal).collect(Collectors.toSet());
        }

        public Set<Character> getChildrenAsCharSet(Character nonTerminal, boolean pureTerminal) {
            return ruleSet.stream()
                    .filter(x -> x.getLeftOperand().equals(nonTerminal) &&
                            (!pureTerminal ||
                                    x.isPureTerminalInRightOperand(terminalCharacterSet, nonTerminalCharacterSet)))
                    .map(x -> x.getTerminals(terminalCharacterSet))
                    .reduce((a, b) -> {
                        a.addAll(b);
                        return a;
                    })
                    .orElse(new HashSet<>());
        }

        public boolean isDerivedByStartSymbol(Character terminal) {
            Set<Character> charSet = getParentsAsCharSet(terminal);
            for(Character ch : charSet) {
                if(nonTerminalService.isDerivedByStartSymbol(ch, true)) return true;
            }
            return false;
        }
    }

    public class NonTerminalService implements Serializable {
        public Set<Character> getParentsAsCharSet(Character nonTerminal, boolean excludeRecursion) {
            return ruleSet.stream().filter(
                    x ->
                            x.doesRightOperandContainNonTerminal(nonTerminal) &&
                                    !(excludeRecursion && x.getLeftOperand().equals(nonTerminal))
            ).map(Rule::getLeftOperand).collect(Collectors.toSet());
        }

        public Set<Rule> getParentsAsRuleSet(Character nonTerminal, boolean excludeRecursion) {
            return getRuleSetByNonTerminalCharStream(getParentsAsCharSet(nonTerminal, excludeRecursion).stream());
        }

        public Set<Set<Rule>> getParentsAsSetOfRuleSet(Character nonTerminal, boolean excludeRecursion) {
            return getParentsAsCharSet(nonTerminal, excludeRecursion).stream().map(Grammar.this::getRuleSetByNonTerminal).collect(Collectors.toSet());
        }

        public Set<Character> getChildrenAsCharSet(Character nonTerminal, boolean excludeRecursion, boolean pureNonTerminal) {
            return ruleSet.stream()
                    .filter(x -> x.getLeftOperand().equals(nonTerminal) &&
                            (!pureNonTerminal ||
                              x.isPureNonTerminalInRightOperand(terminalCharacterSet, nonTerminalCharacterSet)))
                    .map(x -> x.getNonTerminals(nonTerminalCharacterSet, excludeRecursion))
                    .reduce((a, b) -> {
                        a.addAll(b);
                        return a;
                    })
                    .orElse(new HashSet<>());
        }

        public Set<Rule> getChildrenAsRuleSet(Character nonTerminal, boolean excludeRecursion, boolean pureNonTerminal) {
            return getRuleSetByNonTerminalCharStream(getChildrenAsCharSet(nonTerminal, excludeRecursion, pureNonTerminal).stream());
        }

        public boolean leadsToTerminal(Character nonTerminal, boolean strictly) {
            if(!terminalService.getChildrenAsCharSet(nonTerminal, true).isEmpty()) {
                if(strictly) {
                    Set<Rule> rules = nonTerminalService.getChildrenAsRuleSet(nonTerminal, false, false);
                    return rules.isEmpty() || nonTerminalService.getChildrenAsRuleSet(nonTerminal, false, false).stream()
                            .anyMatch(x -> x.isPureTerminalInRightOperand(terminalCharacterSet, nonTerminalCharacterSet));
                }
                return true;
            }
            Set<Character> childrenCharSet = getChildrenAsCharSet(nonTerminal, true, false);
            if(!childrenCharSet.isEmpty()) {
                for(Character ch : childrenCharSet) {
                    if(leadsToTerminal(ch, strictly)) return true;
                }
            }
            return false;
        }

        public boolean isDerivedByStartSymbol(Character nonTerminal, boolean excludeRecursion) {
            if(nonTerminal.equals(startSymbol)) return true;
            Set<Character> characterSet = getParentsAsCharSet(nonTerminal, excludeRecursion);
            if(!characterSet.isEmpty())
                for(Character ch : characterSet) {
                    if (isDerivedByStartSymbol(ch, excludeRecursion)) return true;
                }
            return false;
        }

        public boolean hasRuleWithPureTerminalTransition(Character nonTerminal) {
            return getRuleSetByNonTerminal(nonTerminal).stream()
                    .anyMatch(x -> x.isPureTerminalInRightOperand(terminalCharacterSet, nonTerminalCharacterSet));
        }

        public boolean hasRuleWithPureNonTerminalTransition(Character nonTerminal) {
            return getRuleSetByNonTerminal(nonTerminal).stream()
                    .anyMatch(x -> x.isPureNonTerminalInRightOperand(terminalCharacterSet, nonTerminalCharacterSet));
        }

        public boolean isRecursive(Character nonTerminal) {
            return getRuleSetByNonTerminal(nonTerminal).stream()
                    .anyMatch(x -> x.doesRightOperandContainNonTerminal(nonTerminal));
        }
    }

    public TerminalService terminalService = new TerminalService();

    public NonTerminalService nonTerminalService = new NonTerminalService();

    private static final Character EPSILON = 'ε';

    public Grammar(Set<Character> terminalCharacterSet, Set<Character> nonTerminalCharacterSet, Character startSymbol, List<Rule>... ruleSet) {
        this.terminalCharacterSet = terminalCharacterSet;
        this.nonTerminalCharacterSet = nonTerminalCharacterSet;
        this.ruleSet = new HashSet<>();
        for (List<Rule> rules : ruleSet) {
            this.ruleSet.addAll(rules);
        }
        this.startSymbol = startSymbol;
    }

    public Grammar() {}

    public static Character getEPSILON() {
        return EPSILON;
    }

    public Character getNewCharacterWithIncrement() {
        Character ch = newNonTerminalCharacter;
        nonTerminalCharacterSet.add(ch);
        newNonTerminalCharacter++;
        return ch;
    }

    public Set<Rule> getRuleSetByNonTerminal(Character nonTerminal) {
        return ruleSet.stream().filter(x -> x.getLeftOperand().equals(nonTerminal)).collect(Collectors.toSet());
    }

    public void removeNonTerminalAndItsRules(Character nonTerminal) {
        getRuleSetByNonTerminal(nonTerminal).forEach(ruleSet::remove);
    }

    public void removeNonTerminal(Character nonTerminal) {
        nonTerminalService.getParentsAsRuleSet(nonTerminal, true).stream()
                .filter(x -> x.doesRightOperandContainNonTerminal(nonTerminal))
                .forEach(x -> {
                    ruleSet.remove(x);
                });
        removeNonTerminalAndItsRules(nonTerminal);
        nonTerminalCharacterSet.remove(nonTerminal);
    }

    public void removeTerminal(Character terminal) {
        ruleSet.stream()
                .filter(x -> x.doesRightOperandContainTerminal(terminal) && x.isPureTerminalInRightOperand(terminalCharacterSet, nonTerminalCharacterSet))
                .forEach(ruleSet::remove);
        terminalCharacterSet.remove(terminal);
    }

    public List<Rule> getEpsilonRules() {
        return ruleSet.stream()
                .filter(x -> x.isEpsilon(EPSILON))
                .collect(Collectors.toList());
    }

    private Set<Rule> getRuleSetByNonTerminalCharStream(Stream<Character> charStream) {
        return charStream.map(this::getRuleSetByNonTerminal).reduce((a, b) -> {
            a.addAll(b);
            return a;
        }).orElse(new HashSet<>());
    }

    public Map<Character, Collection<Rule>> getCharacterRuleMap() {
        Map<Character, Collection<Rule>> map = new HashMap<>();
        nonTerminalCharacterSet.forEach( x-> {
            map.put(x, getRuleSetByNonTerminal(x));
        });
        return map;
    }

    public Grammar clone() {
        try {
            return (Grammar) super.clone();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String toString() {
        StringBuilder
                nonTerminals = new StringBuilder(),
                terminals = new StringBuilder(),
                rules = new StringBuilder();
        nonTerminalCharacterSet.forEach(x -> nonTerminals.append(x).append("\n"));
        terminalCharacterSet.forEach(x -> terminals.append(x).append("\n"));
        ruleSet.forEach(x -> rules.append(x.toString()).append("\n"));
        return String.format("NonTerminals:\n%s\nTerminals:\n%s\nStartSym: %s\nRules:\n%s",
                nonTerminals.toString(), terminals.toString(), startSymbol.toString(), rules.toString());
    }

}
