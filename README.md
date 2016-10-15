# Genie Custom Annotation Demo

This example shows how you can use the [Genie](https://github.com/osglworks/java-di) to implement Spring 4.3 Custom annotation Like feature

Nicky Moelholm has a [blog](https://moelholm.com/2016/10/15/spring-4-3-custom-annotations/) talking about how to 
customise annotation in Spring 4.3 application. I am curious to see how can I get the samething done using 
[Genie](https://github.com/osglworks/java-di). So it comes up with this project, which is literally a rewrite of
Nicky Moelholm's [spring43-custom-annotations](https://github.com/nickymoelholm/smallexamples/tree/master/spring43-custom-annotations) 
with [Genie](https://github.com/osglworks/java-di) 
   

First let's take a look at the `GreeterService` code:

```java
// Spring 4.3 Code
@BusinessService
public class GreeterService {
 
  @LocalizedMessage("greeterservice.greeting")
  private Message greetingMsg;
 
  public String sayHello(@NotNull String caller) {
    return greetingMsg.format(caller);
  }
 
}
```

In Genie version it is pretty much the same thing except we don't need the `@BusinessService` annotation:

```java
// Genie Code
import org.osgl.$;

public class GreeterService {

    @LocalizedMessage("greeterservice.greeting")
    private Message message;

    public String sayHello(String caller) {
        return message.format($.notNull(caller));
    }

}
```

Note instead of using `@NotNull` annotation to declare the logic and then having 
[a lot of code](https://github.com/nickymoelholm/smallexamples/blob/master/spring43-custom-annotations/src/main/java/com/moelholm/spring43/customannotations/NotNullParameterAspect.java)
to implement it, we just use the handy shortcut to <code>[Object.requireNotNull](http://https://docs.oracle.com/javase/7/docs/api/java/util/Objects.html#requireNonNull(T))</code>
to code our intention that the `caller` been passed in cannot be `null`. The aspect oriented implementation
coded in the original project is way too complicated and have very bad runtime performance.

Now come to the `LocalizedMessage` code:

In the Spring 4.3 project:

```java
// Spring 4.3 Code
@Autowired
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalizedMessage {

  String value() default "";

}
```

In the Genie version:

```java
// Genie Code
@InjectTag
@LoadValue(MessageLoader.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalizedMessage {

    String value() default "";

}
```

There is one more line `@LoadValue(MessageLoader.class)` found in the Genie version which declares 
how Genie should load the local `Message` instance: through the value loader class `MessageLoader`
 whose implementation is shown below:
 
```java
// Genie Code
public class MessageLoader extends ValueLoader.Base<Message> {

    @Inject
    private MessageSource messageSource;

    @Override
    public Message get() {
        return new Message(value(), messageSource);
    }

}
```

This value loading mechanism is a unique feature of Genie and it help us to avoid coding the runtime
dispatching logic using the `InjectionPoint`, in the original Spring 4.3 project, through the 
<code>[MessageConfig](https://github.com/nickymoelholm/smallexamples/blob/master/spring43-custom-annotations/src/main/java/com/moelholm/spring43/customannotations/MessageConfig.java)</code>
 class:
 
```java
// Spring 4.3 Code
@Configuration
public class MessageConfig {

  @Bean
  public MessageSource messageSource() {

    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");

    return messageSource;
  }

  @Bean
  @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public Message message(InjectionPoint ip) {

    LocalizedMessage localizedMessage = AnnotationUtils
        .getAnnotation(ip.getAnnotatedElement(), LocalizedMessage.class);

    String resourceBundleKey = localizedMessage.value();

    return new Message(messageSource(), resourceBundleKey);
  }

}
```

Obviously the Genie version with customized value loading mechanism beat the `InjectionPoint` approach as

* There is no runtime overhead to lookup annotations for each method call
* The code is cleaner

There are some other classes created in the Genie version which is mainly to 
emulate corresponding class in Springframework, e.g.

* <code>[MessageSource](https://github.com/greenlaw110/genie-custom-annotation-demo/blob/master/src/main/java/com/greenlaw110/MessageSource.java)</code>
* <code>[LocaleContextHolder](https://github.com/greenlaw110/genie-custom-annotation-demo/blob/master/src/main/java/com/greenlaw110/LocaleContextHolder.java)</code>

They are all very simple and straightforward.

Finally here is the `Application` main class:

```java
// Genie Code
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
```

And the unit test case is exactly the same with the one in the original 
Spring 4.3 project except the Runner annotation is different.