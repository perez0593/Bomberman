/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;


public final class SimpleArrayList<E> implements List<E>, RandomAccess
{
    private E[] elements;
    private int size;
    
    public SimpleArrayList(E[] arrayBase, int initialSize)
            throws NullPointerException, IndexOutOfBoundsException
    {
        if(initialSize < 0 || initialSize >= arrayBase.length)
            throw new IndexOutOfBoundsException(initialSize<0?
                    ("initialSize = "+initialSize):
                    ("bounds = "+arrayBase.length+"; initialSize = "+initialSize));
        elements = arrayBase;
        size = initialSize;
    }
    
    public SimpleArrayList(E[] arrayBase)
    {
        this(arrayBase,0);
    }
    
    private static <E> E[] grow(E[] elements)
    {
        int newLen = elements.length * 2;
        return Arrays.copyOf(elements,newLen);
    }
    private static <E> E[] grow(E[] elements, int amount)
    {
        int newLen = elements.length + (amount<0?-amount:amount);
        return Arrays.copyOf(elements,newLen);
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o)
    {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<E> iterator()
    {
        return new Iterator<E>()
        {
            private int it = 0;
            
            @Override
            public boolean hasNext()
            {
                return it < size;
            }

            @Override
            public E next()
            {
                return elements[it++];
            }
        };
    }

    @Override
    public Object[] toArray()
    {
        return Arrays.copyOf(elements,size);
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        System.arraycopy(elements,0,a,0,a.length < size ? a.length : size);
        return a;
    }

    @Override
    public boolean add(E e)
    {
        if(size == elements.length)
            elements = grow(elements);
        elements[size] = e;
        size++;
        return true;
    }
    
    private static <E> void reorganize(E[] elements, int from, int distance)
    {
        System.arraycopy(elements,from+distance,elements,from,elements.length-(from+distance));
    }

    @Override
    public boolean remove(Object o)
    {
        int index = indexOf(o);
        if(index < 0)
            return false;
        elements[index] = null;
        reorganize(elements,index,1);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        for(Object o : c)
            if(!contains(o))
                return false;
        return true;
    }
    
    private static <E> E[] expandIn(E[] elements, int listSize, int offset, int amount)
    {
        if(listSize + (amount - 1) >= elements.length)
            elements = grow(elements,amount);
        System.arraycopy(elements,offset,elements,offset+amount,amount);
        return elements;
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        //elements = expandIn(elements,size,size,c.size());
        while(size + (c.size() - 1) >= elements.length)
            elements = grow(elements);
        for(E e : c)
            elements[size++] = e;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) throws IndexOutOfBoundsException
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException(index<0?
                    ("index = "+index):("bounds = "+size+"; index = "+index));
        elements = expandIn(elements,size,index,c.size());
        for(E e : c)
            elements[index++] = e;
        size += c.size();
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        boolean flag = true;
        for(Object o : c)
            if(!remove(o) && flag)
                flag = false;
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        boolean flag = true;
        for(E e : elements)
            if(c.contains(e))
                if(!remove(e) && flag)
                    flag = false;
        return flag;
    }

    @Override
    public void clear()
    {
        Arrays.fill(elements,null);
    }

    @Override
    public E get(int index) throws IndexOutOfBoundsException
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException(index<0?
                    ("index = "+index):("bounds = "+size+"; index = "+index));
        return elements[index];
    }

    @Override
    public E set(int index, E element) throws IndexOutOfBoundsException
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException(index<0?
                    ("index = "+index):("bounds = "+size+"; index = "+index));
        return elements[index] = element;
    }

    @Override
    public void add(int index, E element) throws IndexOutOfBoundsException
    {
        if(index < 0 || index > size)
            throw new IndexOutOfBoundsException(index<0?
                    ("index = "+index):("bounds = "+size+"; index = "+index));
        if(index == size)
        {
            add(element);
            return;
        }
        elements = expandIn(elements,size,index,1);
        elements[index] = element;
    }

    @Override
    public E remove(int index) throws IndexOutOfBoundsException
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException(index<0?
                    ("index = "+index):("bounds = "+size+"; index = "+index));
        E e = elements[index];
        elements[index] = null;
        reorganize(elements,index,1);
        return e;
    }

    @Override
    public int indexOf(Object o)
    {
        for(int i=0;i<elements.length;i++)
            if(elements[i].equals(o))
                return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object o)
    {
        int last = -1;
        for(int i=0;i<elements.length;i++)
            if(elements[i].equals(o))
                last = i;
        return last;
    }

    @Override
    public ListIterator<E> listIterator()
    {
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(final int index)
    {
        return new ListIterator<E>()
        {
            private int it = index;
            
            @Override
            public boolean hasNext()
            {
                return it < size;
            }

            @Override
            public E next()
            {
                return elements[it++];
            }

            @Override
            public boolean hasPrevious()
            {
                return it-1 >= 0;
            }

            @Override
            public E previous()
            {
                return elements[--it];
            }

            @Override
            public int nextIndex()
            {
                return it;
            }

            @Override
            public int previousIndex()
            {
                return it - 1;
            }

            @Override
            public void remove()
            {
                SimpleArrayList.this.remove(--it);
            }

            @Override
            public void set(E e)
            {
                elements[it-1] = e;
            }

            @Override
            public void add(E e)
            {
                SimpleArrayList.this.add(it++,e);
            }
        };
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex)
    {
        SimpleArrayList sal = new SimpleArrayList<>(Arrays.copyOfRange(elements,fromIndex,toIndex));
        sal.size = sal.elements.length;
        return sal;
    }
    
    @Override
    public void forEach(Consumer<? super E> consumer)
    {
        for(int i=0;i<elements.length;i++)
            consumer.accept(elements[i]);
    }
    
    @Override
    public Spliterator<E> spliterator()
    {
        return Spliterators.spliterator(iterator(),size,0);
    }
}
