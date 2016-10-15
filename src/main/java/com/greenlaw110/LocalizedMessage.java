package com.greenlaw110;

import org.osgl.inject.annotation.InjectTag;
import org.osgl.inject.annotation.LoadValue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@InjectTag
@LoadValue(MessageLoader.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalizedMessage {

    String value() default "";

}