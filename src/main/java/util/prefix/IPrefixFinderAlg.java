package util.prefix;

import grammar.Rule;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IPrefixFinderAlg {
    Map<String, List<Rule>> find(Collection<Rule> ruleList);
}
