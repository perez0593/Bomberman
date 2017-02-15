/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.swing.AbstractListModel;

/**
 *
 * @author Marc
 * @param <E>
 */
public final class SortedListModel<E extends Comparable<E>> extends AbstractListModel<E> implements List<E>
{
    private final ArrayList<E> elements;
    
    public SortedListModel()
    {
        elements = new ArrayList<>();
    }
    
    public final void ensureCapacity(int capacity)
    {
        elements.ensureCapacity(capacity);
    }
    
    @Override
    public int getSize()
    {
        return elements.size();
    }

    @Override
    public E getElementAt(int index)
    {
        return elements.get(index);
    }

    @Override
    public Iterator<E> iterator()
    {
        return new Iterator<E>()
        {
            private final Iterator<E> it = elements.iterator();
            
            @Override
            public boolean hasNext()
            {
                return it.hasNext();
            }

            @Override
            public E next()
            {
                return it.next();
            }
        };
    }

    @Override
    public int size()
    {
        return elements.size();
    }

    @Override
    public boolean isEmpty()
    {
        return elements.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return elements.contains(o);
    }

    @Override
    public Object[] toArray()
    {
        return elements.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        return elements.toArray(a);
    }

    @Override
    public boolean add(E e)
    {
        boolean b = elements.add(e);
        Collections.sort(elements);
        int index = elements.indexOf(e);
        fireIntervalAdded(this,index,index);
        return b;
    }

    @Override
    public boolean remove(Object o)
    {
        int index = indexOf(o);
        boolean rv = elements.remove(o);
        if(index >= 0)
            fireIntervalRemoved(this, index, index);
        return rv;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return elements.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        c.stream().forEach((e) -> {
            add(e);
        });
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c)
    {
        return addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        c.stream().forEach((o) -> {
            remove(o);
        });
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }
    
    public final void removeAllElements()
    {
        clear();
    }

    @Override
    public void clear()
    {
        int index1 = elements.size()-1;
        elements.clear();
        if (index1 >= 0) {
            fireIntervalRemoved(this,0,index1);
        }
    }

    @Override
    public E get(int index)
    {
        return elements.get(index);
    }

    @Override
    public E set(int index, E element)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element)
    {
        add(element);
    }

    @Override
    public E remove(int index)
    {
        E rv = elements.remove(index);
        fireIntervalRemoved(this, index, index);
        return rv;
    }

    @Override
    public int indexOf(Object o)
    {
        return elements.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o)
    {
        return elements.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex)
    {
        throw new UnsupportedOperationException();
    }
    
}
