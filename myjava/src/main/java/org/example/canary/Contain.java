package org.example.canary;

import java.util.List;

public class Contain {
    // Contains checks whether a given element is present in the list.
    public static boolean contains(List<String> list, String value) {
        return list.contains(value);
    }

    // ContainsAny checks whether any of the given elements are present in the list.
    public static boolean containsAny(List<String> list, String... values) {
        for (String val : values) {
            if (list.contains(val)) {
                return true;
            }
        }
        return false;
    }

    // ContainsAll checks whether all of the given elements are present in the list.
    public static boolean containsAll(List<String> list, String... values) {
        for (String val : values) {
            if (!list.contains(val)) {
                return false;
            }
        }
        return true;
    }
}
