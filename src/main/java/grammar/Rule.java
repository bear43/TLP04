package grammar;

import lombok.Data;
import util.Util;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
public class Rule implements Serializable {
    private Character leftOperand;

    private Set<Character> rightOperand;

    public Rule(Character leftOperand, Set<Character> rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public Rule(Character leftOperand, String rightOperand) {
        this(leftOperand, rightOperand.chars().boxed().map(x -> (char)x.intValue()).collect(Collectors.toSet()));
    }

    public Set<Character> getTerminals(Set<Character> terminalSet) {
        return Util.getSymbolsBySet(terminalSet, rightOperand);
    }

    public Set<Character> getNonTerminals(Set<Character> nonTerminalSet, boolean excludeLeftOperand) {
        Set<Character> clonedSet = new HashSet<>(nonTerminalSet);
        if(excludeLeftOperand) clonedSet.remove(leftOperand);
        return Util.getSymbolsBySet(clonedSet, rightOperand);
    }

    public boolean doesRightOperandContainTerminal(Character terminal) {
        return rightOperand.contains(terminal);
    }

    public boolean doesRightOperandContainNonTerminal(Character nonTerminal) {
        return rightOperand.contains(nonTerminal);
    }

    public boolean isPureTerminalInRightOperand(Set<Character> terminalCharacterSet, Set<Character> nonTerminalCharacterSet) {
        return !getTerminals(terminalCharacterSet).isEmpty() && getNonTerminals(nonTerminalCharacterSet, false).isEmpty();
    }

    public boolean isEpsilon(Character epsilonChar) {
        return rightOperand.size() == 1 && rightOperand.contains(epsilonChar);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        rightOperand.forEach(sb::append);
        return String.format("%s -> %s", leftOperand.toString(), sb.toString());
    }
}
