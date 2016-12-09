/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.util;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *
 * @author Marc
 * @param <T>
 * @param <LIST>
 */
public class DynamicIterableList<T,LIST extends List<T>> implements List<T>
{
    private final LIST list;
    public ListIterator<T> it;
    
    public DynamicIterableList(Supplier<LIST> list)
    {
        this.list = list.get();
        it = null;
    }
    
    public final boolean isInDynamicIteration()
    {
        return it != null;
    }

    @Override
    public int size()
    {
        return list.size();
    }

    @Override
    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return list.contains(o);
    }

    @Override
    public java.util.Iterator<T> iterator()
    {
        return it = list.listIterator();
    }

    @Override
    public Object[] toArray()
    {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        return list.toArray(a);
    }

    @Override
    public boolean add(T e)
    {
        if(it == null)
            return list.add(e);
        it.add(e);
        return true;
    }
    
    public void remove()
    {
        if(it != null)
            it.remove();
    }

    @Override
    public boolean remove(Object o)
    {
        if(it != null) return false;
        return list.remove(o);
    }
    
    @Override
    public boolean removeIf(Predicate<? super T> filter)
    {
        if(it != null) return false;
        return list.removeIf(filter);
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        if(it != null) return false;
        return list.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        if(it != null) return false;
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        if(it != null) return false;
        return list.retainAll(c);
    }

    @Override
    public void clear()
    {
        if(it != null) return;
        list.clear();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c)
    {
        if(it != null) return false;
        return list.addAll(index,c);
    }

    @Override
    public T get(int index)
    {
        return list.get(index);
    }

    @Override
    public T set(int index, T element)
    {
        if(it != null) return null;
        return list.set(index,element);
    }

    @Override
    public void add(int index, T element)
    {
        if(it != null) return;
        list.add(index,element);
    }

    @Override
    public T remove(int index)
    {
        if(it != null) return null;
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o)
    {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o)
    {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator()
    {
        return it = list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index)
    {
        return it = list.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex)
    {
        return list.subList(fromIndex,toIndex);
    }
    
    @Override
    public final void forEach(Consumer<? super T> action)
    {
        it = list.listIterator();
        if(it == null) return;
        for(;it.hasNext();)
            action.accept(it.next());
        it = null;
    }
    
    public final void clearIterator() { it = null; }
}
