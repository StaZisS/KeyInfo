package org.example.key_info.core.util;

import java.util.Locale;

public class QueryTool {
    public static String getNormalizedString(String source) {
        return source.toLowerCase(Locale.ROOT).replace(" ", "");
    }
}
