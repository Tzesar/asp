package org.compiler;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayListUnique<E> extends ArrayList<E>{

    public void push(E e){
        add(e);
    }

    public E pop(){
        int index = size() - 1;

        E e = get(index);
        remove(e);
        return e;
    }

    @Override
    public boolean addAll(Collection<? extends E> c){
        int numNew = c.size();
        ensureCapacity(size() + numNew);
        c.stream().filter( e -> !contains(e) ).forEach(this::add);

        return true;
    }

    @Override
    public boolean add(E e){
        if ( !contains(e) ){
            ensureCapacity(size() + 1);
            super.add(e);
            return true;
        } else {
            return false;
        }
    }
}
