package vip.creatio.clib.basic.tools;

public interface Wrapper<T> {

    T unwrap();

    Class<? extends T> wrappedClass();

}
