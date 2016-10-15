package com.greenlaw110;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Emulate `org.springframework.context.MessageSource`
 */
public class MessageSource {

    private ConcurrentMap<Locale, ResourceBundle> bundles = new ConcurrentHashMap<>();

    public String get(Locale locale, String key, Object ... args) {
        return MessageFormat.format(bundleOf(locale).getString(key), args);
    }

    private ResourceBundle bundleOf(Locale locale) {
        ResourceBundle bundle = bundles.get(locale);
        if (null == bundle) {
            bundle = ResourceBundle.getBundle("messages", locale);
            bundles.putIfAbsent(locale, bundle);
        }
        return bundle;
    }
}
