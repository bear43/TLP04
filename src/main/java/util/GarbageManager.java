package util;

import gc.*;
import grammar.Grammar;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GarbageManager {
    private List<IGarbageCollector> garbageCollectorPool = new ArrayList<>();

    public GarbageManager() {
        this(Arrays.asList(
                new UselessCollector(),
                new EpsilonCollector(),
                new ChainCollector(),
                new PrefixCollector()
        ));
    }

    public GarbageManager(List<IGarbageCollector> garbageCollectorPool) {
        this.garbageCollectorPool.addAll(garbageCollectorPool);
    }

    public void doWork(Grammar grammar) {
        garbageCollectorPool.forEach(x -> x.clean(grammar));
    }

    public Grammar doWorkAndKeepOldGrammar(Grammar grammar) {
        for(IGarbageCollector gc : garbageCollectorPool) {
            grammar = gc.keepOldAndCleanNew(grammar);
        }
        return grammar;
    }
}
