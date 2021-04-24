package vip.creatio.clib.basic.annotation.processor;

import vip.creatio.clib.basic.annotation.Listener;
import vip.creatio.clib.basic.tools.ListenerRegister;
import vip.creatio.accessor.annotation.AnnotationProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ListenerProcessor implements AnnotationProcessor<Listener> {

    private final ListenerRegister register;

    public ListenerProcessor(ListenerRegister register) {
        this.register = register;
    }

    @Override
    public void process(Listener listener, Method mth) {
        if (Modifier.isStatic(mth.getModifiers())
                && !Modifier.isPrivate(mth.getModifiers())
                && mth.getParameterCount() == 1
                && Listener.PARENT.isAssignableFrom(mth.getParameterTypes()[0])) {
            register.register(mth);
        } else {
            System.err.println("[CreatioLib] Unable to register event listener for method " + mth);
        }
    }

    @Override
    public Class<Listener> getTargetClass() {
        return Listener.class;
    }
}
