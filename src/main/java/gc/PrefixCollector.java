package gc;

import grammar.Grammar;
import util.prefix.PrefixFinder;

public class PrefixCollector implements IGarbageCollector {

    private PrefixFinder prefixFinder;

    @Override
    public void clean(Grammar grammar) {
        prefixFinder = new PrefixFinder(grammar);
        prefixFinder.findPrefixRules();
    }
}
