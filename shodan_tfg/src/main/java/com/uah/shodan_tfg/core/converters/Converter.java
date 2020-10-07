package com.uah.shodan_tfg.core.converters;

public interface Converter<T, E> {

    public E convert(T item);

    public T invert(E item);
}
