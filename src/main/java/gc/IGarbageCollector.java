package gc;

import grammar.Grammar;
import org.apache.commons.lang3.SerializationUtils;

public interface IGarbageCollector {
    void clean(Grammar grammar);
    default Grammar keepOldAndCleanNew(Grammar grammar) {
        Grammar newGrammar = SerializationUtils.clone(grammar);
        clean(newGrammar);
        return newGrammar;
    }
}
