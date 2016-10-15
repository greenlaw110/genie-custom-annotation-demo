package com.greenlaw110;

import org.osgl.$;

import java.util.Locale;

/**
 * emulate `org.springframework.context.i18n.LocaleContextHolder`
 */
public class LocaleContextHolder {

    private static final ThreadLocal<Locale> current = new ThreadLocal<>();

    public static Locale get() {
        Locale locale = current.get();
        return null == locale ? Locale.getDefault() : locale;
    }

    public static void set(Locale locale) {
        current.set($.notNull(locale));
    }

}
