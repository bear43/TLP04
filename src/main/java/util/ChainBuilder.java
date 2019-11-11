package util;

import grammar.Grammar;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
class Node {
    private Node parent;
    private Character character;
    private boolean pureNonTerminal;
    private boolean isRecursive;
    private List<Node> children = new ArrayList<>();

    public Node(Node parent, Character character, List<Node> children) {
        this.parent = parent;
        this.character = character;
        if(children != null) this.children.addAll(children);
    }

    public Node() {}

    public boolean isOrphaned() {
        return children.isEmpty();
    }

    @Override
    public String toString() {
        return character.toString();
    }
}

@Data
public class ChainBuilder {
    private Grammar grammar;

    private List<List<Character>> characterList = new ArrayList<>();

    private Map<Character, List<List<Character>>> characterMap = new HashMap<>();

    private Node commonChain;

    private boolean pureNonTerminal;

    private boolean excludeRecursion;

    private boolean recursionDetector;

    public ChainBuilder(Grammar grammar) {
        this.grammar = grammar;
    }

    private Node makeNodeTree(Node parent, Character character) {
        recursionDetector = parent != null && parent.getCharacter().equals(character);
        Node node = new Node(parent, character, null);
        node.setRecursive(grammar.nonTerminalService.isRecursive(character));
        if(!recursionDetector) {
            Set<Character> charSet = grammar.nonTerminalService.getChildrenAsCharSet(character, excludeRecursion, false);
            node.setPureNonTerminal(grammar.nonTerminalService.hasRuleWithPureNonTerminalTransition(character));
            charSet.forEach(ch -> node.getChildren().add(makeNodeTree(node, ch)));
        }
        return node;
    }

    public void buildCommonChain() {
        commonChain = makeNodeTree(null, grammar.getStartSymbol());
    }

    private void recursionCutter(List<Character> characterList) {
        int len = characterList.size();
        if(
                len > 2 &&
                characterList.get(len-2).equals(characterList.get(len-1))
        ) {
            characterList.remove(len-1);
        }
    }

    private void addChain(List<Node> nodeList) {
        List<Character> newCharList = nodeList.stream().map(Node::getCharacter).collect(Collectors.toList());
        recursionCutter(newCharList);
        characterList.add(newCharList);
        if(!newCharList.isEmpty()) {
            Character ch = newCharList.get(0);
            characterMap.putIfAbsent(ch, new ArrayList<>());
            characterMap.get(ch).add(newCharList);
        }
    }

    private void makeChain(Node start, List<Node> nodeList) {
            nodeList.add(start);
            if (start.isOrphaned()) {
                addChain(nodeList);
            } else {
                start.getChildren().forEach(child -> {
                    makeChain(child, nodeList);
                });
            }
            nodeList.remove(start);
    }


    private Node findNodeBy(Node currentNode, Character nonTerminal) {
        if(currentNode.getCharacter().equals(nonTerminal)) return currentNode;
        for (Node child : currentNode.getChildren()) {
            Node result = findNodeBy(child, nonTerminal);
            if(result != null) return result;
        }
        return null;
    }

    public void buildSpecificChain(Character nonTerminal) {
        Node start = findNodeBy(commonChain, nonTerminal);
        makeChain(start, new ArrayList<>());
    }

    private boolean hasAlreadyRoute(Character ch) {
        return characterMap.containsKey(ch);
    }

    private void recursiveBuildAllNodeChain(Node currentNode) {
        makeChain(currentNode, new ArrayList<>());
        if(!currentNode.isOrphaned()) {
            currentNode.getChildren().forEach(x -> {
                if(!hasAlreadyRoute(x.getCharacter()))
                    recursiveBuildAllNodeChain(x);
            });
        }
    }


    public void buildAllChain() {
        characterList.clear();
        recursiveBuildAllNodeChain(commonChain);
        //makeChain(commonChain, new ArrayList<>());
    }
}
