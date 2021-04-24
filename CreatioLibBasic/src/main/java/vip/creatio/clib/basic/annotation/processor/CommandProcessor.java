package vip.creatio.clib.basic.annotation.processor;

import vip.creatio.accessor.annotation.AnnotationProcessor;
import vip.creatio.clib.basic.annotation.Command;

import java.lang.reflect.Method;

public class CommandProcessor implements AnnotationProcessor<Command> {

    @Override
    public void process(Command instance, Method mth) {
        AnnotationProcessor.super.process(instance, mth);
    }

    @Override
    public void process(Command instance, Class<?> c) {
        AnnotationProcessor.super.process(instance, c);
    }

    @Override
    public void onProcessEnd(Class<?> c) {
        AnnotationProcessor.super.onProcessEnd(c);
    }

    @Override
    public Class<Command> getTargetClass() {
        return Command.class;
    }
}
