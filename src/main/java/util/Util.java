package util;

import java.util.Set;
import java.util.stream.Collectors;

public class Util {
    public static Set<Character> getSymbolsBySet(Set<Character> filterSet, Set<Character> symbolSet) {
        return symbolSet.stream().filter(filterSet::contains).collect(Collectors.toSet());
    }
}
