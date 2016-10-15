package com.greenlaw110;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Locale;

@org.junit.runner.RunWith(GenieRunner.class)
public class GreeterServiceTest extends Assert {

    @Inject
    private GreeterService greeterService;
    @Test
    public void sayHello_whenInvoked_thenReturnsEnglishGreeting() {

        // Given
        String caller = "Duke";

        // When
        String greeting = greeterService.sayHello(caller);

        // Then
        assertEquals("Hello World, Duke", greeting);
    }

    @Test(expected = NullPointerException.class)
    public void sayHello_whenInvokedWithNullArgument_thenThrowsIllegalArgumentException() {

        // Given
        String caller = null;

        // When
        greeterService.sayHello(caller);

        // Then
        // ( kapOOOf )
    }

    @Test
    public void sayHello_whenLocaleIsDanish_andInvoked_thenReturnsDanishGreeting() {

        // Given
        LocaleContextHolder.set(new Locale("da", "DK"));
        String caller = "Duke";

        // When
        String greeting = greeterService.sayHello(caller);

        // Then
        assertEquals("Hej Verden, Duke", greeting);
    }

    @Before
    @After
    public void resetLocaleBeforeAndAfterEachTestCase() {
        LocaleContextHolder.set(Locale.ENGLISH);
    }
}