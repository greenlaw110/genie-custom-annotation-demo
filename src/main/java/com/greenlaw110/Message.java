package com.greenlaw110;

import org.osgl.$;

public class Message {

    private String messageKey;
    private MessageSource messageSource;

    public Message(String messageKey, MessageSource messageSource) {
        this.messageKey = $.notNull(messageKey);
        this.messageSource = messageSource;
    }

    public String format(Object ... args) {
        return messageSource.get(LocaleContextHolder.get(), messageKey, args);
    }

}
