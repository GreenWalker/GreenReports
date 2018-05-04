package br.com.empirelands.util;


public class Prefix {

    private static String prefixe;

    public Prefix(String prefix) {
        prefixe = prefix;
    }

    public static String getPrefix() {
        return prefixe;
    }

}
