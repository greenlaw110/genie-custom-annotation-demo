package com.greenlaw110;

import org.osgl.inject.ValueLoader;

import javax.inject.Inject;

public class MessageLoader extends ValueLoader.Base<Message> {

    @Inject
    private MessageSource messageSource;

    @Override
    public Message get() {
        return new Message(value(), messageSource);
    }

}
