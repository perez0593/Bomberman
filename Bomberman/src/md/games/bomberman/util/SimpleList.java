/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 *
 * @author Marc
 */
public class SimpleList<E> implements List<E>
{
    private final class Node
    {
        private E data;
        private Node previous, next;
        
        private Node(E data)
        {
            this.data = data;
            previous = next = null;
        }
    }
    
    private final Node ghost;
    private Node header;
    private int size;
    
    public SimpleList()
    {
        header = ghost = new Node(null);
        size = 0;
    }
    
    @Override
    public boolean add(E e)
    {
        if(header == ghost)
        {
            Node node = new Node(e);
            node.next = ghost;
            ghost.previous = node;
            size++;
            return true;
        }
        Node node = new Node(e);
        node.next = ghost;
        node.previous = ghost.previous;
        ghost.previous.next = node;
        ghost.previous = node;
        size++;
        return true;
    }
    
    public void addFirst(E e)
    {
        Node node = new Node(e);
        node.next = header;
        header.previous = node;
        size++;
    }
    
    public void addLast(E e)
    {
        add(e);
    }
    
    @Override
    public void add(int index, E e)
    {
        if(index < 0 || index > size)
            throw new IndexOutOfBoundsException();
        if(index == 0)
        {
            addFirst(e);
            return;
        }
        if(index == size)
        {
            addLast(e);
            return;
        }
        Node node = new Node(e);
        Node old = header;
        for(int i=0;i<index;i++)
            old = old.next;
        node.next = old;
        node.previous = old.previous;
        old.previous.next = node;
        old.previous = node;
        size++;
    }
    
    public E removeFirst()
    {
        Node node = header.next;
        node.previous = null;
        header.next = null;
        header = node;
        size--;
        return node.data;
    }
    
    public E removeLast()
    {
        Node node = ghost.previous;
        node.next.previous = node.previous;
        node.previous.next = node.next;
        node.next = null;
        node.previous = null;
        size--;
        return node.data;
    }
    
    @Override
    public E remove(int index)
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        if(index == 0)
        {
            return removeFirst();
        }
        if(index == size)
        {
            return removeLast();
        }
        Node node = header;
        for(int i=0;i<index;i++)
            node = node.next;
        node.next.previous = node.previous;
        node.previous.next = node.next;
        node.next = null;
        node.previous = null;
        size--;
        return node.data;
    }
    
    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    @Override
    public boolean contains(Object o)
    {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<E> iterator()
    {
        return new ListIteratorImpl(0);
    }

    @Override
    public Object[] toArray()
    {
        if(isEmpty())
            return new Object[0];
        Object[] array = new Object[size];
        Node node = header;
        for(int i=0;i<size;i++)
        {
            array[i] = node.data;
            node = node.next;
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        if(isEmpty())
            return a;
        Node node = header;
        for(int i=0;i<size&&i<a.length;i++)
        {
            a[i] = (T) node.data;
            node = node.next;
        }
        return a;
    }

    @Override
    public boolean remove(Object o)
    {
        int index = indexOf(o);
        if(index < 0)
            return false;
        remove(index);
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

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        for(E e : c)
            add(e);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c)
    {
        for(E e : c)
            add(index,e);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        boolean ok = true;
        for(Object o : c)
            if(!remove(o))
                ok = false;
        return ok;
        
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        boolean ok = false;
        for(Object o : c)
            if(contains(o))
            {
                remove(o);
                ok = true;
            }
        return ok;
    }

    @Override
    public void clear()
    {
        if(isEmpty())
            return;
        Node node = header;
        while(node != ghost)
        {
            node = node.next;
            node.previous.data = null;
            node.previous.next = null;
            node.previous = null;
        }
        size = 0;
    }

    @Override
    public E get(int index)
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        Node node = header;
        for(int i=0;i<index;i++)
            node = node.next;
        return node.data;
    }

    @Override
    public E set(int index, E element)
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        Node node = header;
        for(int i=0;i<index;i++)
            node = node.next;
        node.data = element;
        return node.data;
    }

    @Override
    public int indexOf(Object o)
    {
        if(isEmpty())
            return -1;
        Node node = header;
        int count = 0;
        while(node.next != ghost)
        {
            if(Objects.equals(node.data,o))
                return count;
            count++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o)
    {
        if(isEmpty())
            return -1;
        Node node = header;
        int count = 0, index = -1;
        while(node.next != ghost)
        {
            if(Objects.equals(node.data,o))
                index = count;
            count++;
        }
        return index;
    }

    @Override
    public ListIterator<E> listIterator()
    {
        return new ListIteratorImpl(0);
    }

    @Override
    public ListIterator<E> listIterator(int index)
    {
        return new ListIteratorImpl(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex)
    {
        if(fromIndex < 0 || fromIndex >= size)
            throw new IndexOutOfBoundsException();
        if(toIndex < 1 || toIndex > size)
            throw new IndexOutOfBoundsException();
        if(fromIndex > toIndex)
            throw new IllegalArgumentException();
        SimpleList<E> list = new SimpleList<>();
        Node node = header;
        for(int i=0;i<fromIndex;i++)
            node = node.next;
        for(int i=fromIndex;i<toIndex;i++)
        {
            list.add(node.data);
            node = node.next;
        }
        return list;
    }
    
    private final class ListIteratorImpl implements ListIterator<E>
    {
        private Node node = header;
        private int __size = size, it = 0;
        
        private ListIteratorImpl(int index)
        {
            if(index < 0 || index >= size)
                throw new IndexOutOfBoundsException();
            if(index > 0)
            {
                for(int i=0;i<index;i++)
                    node = node.next;
                it = index;
            }
        }
        
        @Override
        public boolean hasNext()
        {
            return node == ghost;
        }

        @Override
        public E next()
        {
            Node n = node;
            node = node.next;
            it++;
            return n.data;
        }

        @Override
        public boolean hasPrevious()
        {
            return node.previous != null;
        }

        @Override
        public E previous()
        {
            if(node.previous == null)
                throw new NullPointerException();
            node = node.previous;
            it--;
            return node.data;
        }

        @Override
        public int nextIndex()
        {
            if(node == ghost || node.next == ghost)
                return size;
            if(size != __size)
                return indexOf(node.data);
            return it;
        }

        @Override
        public int previousIndex()
        {
            if(node == header)
                return -1;
            if(size != __size)
                return indexOf(node.previous.data);
            return it - 1;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(E e)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(E e)
        {
            throw new UnsupportedOperationException();
        }
        
    }
}
