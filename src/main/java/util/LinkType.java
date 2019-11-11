package util;

public enum LinkType {
    PURE_NONTERMINAL,//S -> X
    NON_TERMINAL;//S -> Xx
    public boolean isPureNonTerminal() {
        return this == LinkType.PURE_NONTERMINAL;
    }
}
