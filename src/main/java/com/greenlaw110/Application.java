package com.greenlaw110;

import org.osgl.inject.Genie;

import javax.inject.Inject;
import java.util.Locale;

public class Application {

    @Inject
    GreeterService greeter;

    public void run() {
        System.out.println("-- Default locale --");
        System.out.println(greeter.sayHello("Duke"));

        System.out.println("-- Danish --");
        LocaleContextHolder.set(new Locale("da", "DK"));
        System.out.println(greeter.sayHello("Duke"));

        System.out.println("-- Australia --");
        LocaleContextHolder.set(new Locale("en", "AU"));
        System.out.println(greeter.sayHello("Duke"));
    }

    public static void main(String[] args) {
        Genie genie = Genie.create();
        Application app = genie.get(Application.class);
        app.run();
    }

}
