/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.util;

import java.util.Iterator;
import java.util.function.Function;

/**
 *
 * @author Asus
 */
public final class CriteriaIterator<T> implements Iterator<T>
{
    private final Iterator<T> it;
    private final Function<Iterator<T>, T> generator;
    private T next;
    
    public CriteriaIterator(Iterator<T> it, Function<Iterator<T>, T> generator)
    {
        if(it == null || generator == null)
            throw new NullPointerException();
        this.it = it;
        this.generator = generator;
        next = generator.apply(it);
    }
    public CriteriaIterator(Iterable<T> iterable, Function<Iterator<T>, T> generator)
    {
        this(iterable.iterator(),generator);
    }
    
    @Override
    public final boolean hasNext() { return next != null; }

    @Override
    public final T next()
    {
        if(next == null)
            return null;
        T aux = next;
        next = generator.apply(it);
        return aux;
    }
}
