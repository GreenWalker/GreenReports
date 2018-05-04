package br.com.empirelands.util;

import lombok.Getter;

/**
 * Criado por Floydz.
 */
public enum UnicodeSymbols {

    HAND_RIGHT("[->]", "☞"), HAND_RIGHT_NG("[-->]", "☛"), SKULL_WHIT_BONES("[X]", "☠"),
    DIAMOND("[D]", "♦"), LARGEST_BOLL("[.]", "⚫"), TRIANGLE_RIGHT("[Y]", "►"), TRIANGLE_LEFT("[G]", "◄"),
    SMALL_BOLL("[..]", "·");

    @Getter
    private String toCallSymbol;
    @Getter
    private String symbol;

    UnicodeSymbols(String toCallSymbol, String symbols) {
        this.symbol = symbols;
        this.toCallSymbol = toCallSymbol;
    }
}
