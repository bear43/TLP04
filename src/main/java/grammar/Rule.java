package grammar;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import util.Util;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
public class Rule implements Serializable {
    private Character leftOperand;

    private List<Character> rightOperand;

    public Rule(Character leftOperand, List<Character> rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public void setRightOperand(List<Character> rightOperand) {
        this.rightOperand = rightOperand;
    }

    public void setRightOperand(Character[] rightOperand) {
        setRightOperand(Arrays.asList(rightOperand));
    }

    public void setRightOperand(String rightOperand) {
        char[] primitiveCharArray = rightOperand.toCharArray();
        Character[] charArray = new Character[primitiveCharArray.length];
        for(int index = 0; index < primitiveCharArray.length; index++){
            charArray[index] = primitiveCharArray[index];
        }
        setRightOperand(charArray);
    }

    public Rule(Character leftOperand, String rightOperand) {
        this(leftOperand, rightOperand.chars().boxed().map(x -> (char)x.intValue()).collect(Collectors.toList()));
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

    public boolean doesRightOperandContainNextSequence(String sequence) {
        StringBuilder sb = concatRightOperandToStringBuilder();
        return sb.toString().contains(sequence);
    }

    public String concatRightOperandToString() {
        return concatRightOperandToStringBuilder().toString();
    }

    public StringBuilder concatRightOperandToStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        rightOperand.forEach(stringBuilder::append);
        return stringBuilder;
    }

    public boolean isPureTerminalInRightOperand(Set<Character> terminalCharacterSet, Set<Character> nonTerminalCharacterSet) {
        return !getTerminals(terminalCharacterSet).isEmpty() && getNonTerminals(nonTerminalCharacterSet, false).isEmpty();
    }

    public boolean isPureNonTerminalInRightOperand(Set<Character> terminalCharacterSet, Set<Character> nonTerminalCharacterSet) {
        return getTerminals(terminalCharacterSet).isEmpty() && !getNonTerminals(nonTerminalCharacterSet, false).isEmpty();
    }

    public boolean isEpsilon(Character epsilonChar) {
        return rightOperand.size() == 1 && rightOperand.contains(epsilonChar);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        rightOperand.forEach(sb::append);
        return String.format("%s -> %s", leftOperand.toString(), sb.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return leftOperand.equals(rule.leftOperand) &&
                concatRightOperandToString().equals(rule.concatRightOperandToString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftOperand, concatRightOperandToString());
    }
}
