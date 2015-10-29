package org.compiler.Util;

@FunctionalInterface
public interface ConsumerCheckException<T> {
    void accept(T elem) throws Exception;
}
