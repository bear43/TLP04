package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static <T1, T2> void putNewElement(Map<T1, List<T2>> map, T1 key, List<T2> value) {
        if(map.containsKey(key)) {
            map.get(key).addAll(value);
        } else {
            map.put(key, value);
        }
    }

    public static <T1, T2, T3> void putNewElement(Map<T1, Map<T2, List<T3>>> map, T2 entryKey, List<T3> entryValue, T1 key) {
        if(!entryValue.isEmpty()) {
            if(!map.containsKey(key)) {
                map.put(key, new HashMap<>());
            }
            putNewElement(map.get(key), entryKey, entryValue);
        }
    }
}
