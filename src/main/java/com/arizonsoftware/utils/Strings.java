package com.arizonsoftware.utils;

public class Strings {

    /**
     * Replaces all occurrences of the ampersand character '&' with the section
     * symbol '§' in the given string.
     *
     * @param string the string to be parsed
     * @return the parsed string with ampersands replaced by section symbols
     */
    public static String parse(String string) {
        return string.replaceAll("&", "§");
    }

}
