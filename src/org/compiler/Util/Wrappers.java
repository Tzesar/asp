package org.compiler.Util;

import java.util.function.Consumer;

public class Wrappers {
    public static <T> Consumer<T> rethrow(ConsumerCheckException<T> c){
        return elem -> {
            try {
                c.accept(elem);
            } catch (Exception ex){
                Wrappers.<RuntimeException>sneakyThrow(ex);
            }
        };
    }

    public static <T extends Throwable> T sneakyThrow(Throwable t) throws T{
        throw (T) t;
    }
}
