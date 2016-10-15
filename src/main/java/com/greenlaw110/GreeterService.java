package com.greenlaw110;

import org.osgl.$;

public class GreeterService {

    @LocalizedMessage("greeterservice.greeting")
    private Message message;

    public String sayHello(String caller) {
        return message.format($.notNull(caller));
    }

}
