package util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {
    public static Set<Character> getSymbolsBySet(Set<Character> filterSet, Set<Character> symbolSet) {
        return symbolSet.stream().filter(filterSet::contains).collect(Collectors.toSet());
    }

    public static Set<Character> getSymbolsBySet(Set<Character> filterSet, List<Character> symbolSet) {
        return symbolSet.stream().filter(filterSet::contains).collect(Collectors.toSet());
    }

    public static List<Character> getSymbolsByList(Set<Character> filterSet, List<Character> symbolList) {
        return symbolList.stream().filter(filterSet::contains).collect(Collectors.toList());
    }
}
