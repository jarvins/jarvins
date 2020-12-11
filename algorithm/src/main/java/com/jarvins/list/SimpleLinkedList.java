package com.jarvins.list;

import java.util.*;

/**
 * 继承List<E>接口实现的简单的LinkedList
 * 这里没有保证修改的一致性!
 *
 * @param <E>
 */
public class SimpleLinkedList<E> implements List<E> {

    //头指针
    Node<E> head = null;

    //尾指针
    Node<E> tail = null;

    //真实容量
    int size;


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        Node<E> node = head;
        while (node != null) {
            array[index++] = node.element;
            node = node.next;
        }

        return array;
    }

    /**
     * @see com.jarvins.collection.CollectionInterfaceTest#test_question_3()
     */
    @Override
    @Deprecated
    public <T> T[] toArray(T[] a) {
//        if (a.length < size) {
//            a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
//        }
//        Object[] result = a;
//        int index = 0;
//        for (Node<E> node = head; node != null; node = node.next) {
//            result[index++] = node.element;
//        }
//
//        return a;
        throw new UnsupportedOperationException("toArray(T[] a) is a bad method !");
    }

    @Override
    public boolean add(E e) {
        linkedLast(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (indexOf(o) == -1) {
            return false;
        }
        remove(indexOf(o));
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E e : c) {
            add(index++, e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (c.contains(iterator.next())) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        Node<E> next;
        Node<E> e = head;
        while (e != null) {
            next = e.next;
            e.pre = null;
            e.element = null;
            e.next = null;
            e = next;
        }

        head = tail = null;
        size = 0;

    }

    @Override
    public E get(int index) {
        if (!(index > -1 && index < size)) {
            throw new IndexOutOfBoundsException();
        }
        return node(index).element;
    }

    @Override
    public E set(int index, E element) {
        if (!(index > -1 && index < size)) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> node = node(index);
        E ele = node.element;
        node.element = element;
        return ele;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == size) {
            linkedLast(element);
        } else {
            linkedBefore(element, node(index));
        }
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> node = node(index);
        return unlink(node);
    }

    @Override
    public int indexOf(Object o) {
        Node<E> node = head;
        for (int i = 0; i < size && node != null; i++) {
            if (o == null) {
                if (node.element == null) {
                    return i;
                }
            } else {
                if (o.equals(node.element)) {
                    return i;
                }
            }
            node = node.next;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<E> node = tail;
        for (int i = size - 1; size >= 0 && node != null; i--) {
            if (o == null) {
                if (node.element == null) {
                    return i;
                }
            } else {
                if (o.equals(node.element)) {
                    return i;
                }
            }
            node = node.pre;
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new Itr(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new Itr(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        List<E> result = new SimpleLinkedList<>();
        Object[] array = toArray();
        for (int i = fromIndex; i < toIndex; i++) {
            @SuppressWarnings("unchecked")
            E o = (E) array[i];
            result.add(o);
        }
        return result;
    }

    //下标检查应该仅发生在最外层调用，而内层不检查
    private Node<E> node(int index) {
        if (index >= size >> 1) {
            Node<E> x = tail;
            for (int i = size; i > index; i--) {
                x = x.pre;
            }
            return x;
        } else {
            Node<E> x = head;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        }
    }

    private E unlink(Node<E> e) {
        E element = e.element;
        Node<E> pre = e.pre;
        Node<E> next = e.next;

        if (pre == null) {
            head = next;
        } else {
            pre.next = next;
            e.pre = null;
        }

        if (next == null) {
            tail = pre;
        } else {
            next.pre = pre;
            e.next = null;
        }

        size--;
        e.element = null;

        return element;
    }

    private void linkedLast(E e) {
        Node<E> node = new Node<>(e, tail, null);
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
        size++;
    }

    private void linkedFirst(E e) {
        Node<E> node = new Node<>(e, null, head);
        if (head == null) {
            tail = node;
        } else {
            head.pre = node;
        }
        head = node;
        size++;
    }

    private void linkedBefore(E e, Node<E> node) {
        Node<E> pre = node.pre;
        Node<E> eNode = new Node<>(e, pre, node);
        node.pre = eNode;
        if (pre == null) {
            head = eNode;
        } else {
            pre.next = eNode;
        }
        size++;
    }

    private static class Node<E> {
        E element;
        Node<E> pre;
        Node<E> next;

        private Node(E e, Node<E> pre, Node<E> next) {
            this.element = e;
            this.pre = pre;
            this.next = next;
        }
    }

    private class Itr implements ListIterator<E> {

        Node<E> next;
        Node<E> lastReturned;
        int nextIndex;


        Itr(int index) {
            next = index >= size ? null : node(index);
            nextIndex = index;
        }

        @Override
        public boolean hasNext() {
            return size > nextIndex;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.element;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            lastReturned = next = (next == null ? tail : next.pre);
            nextIndex--;
            return lastReturned.element;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            unlink(lastReturned);
            Node<E> n = lastReturned.next;
            if (lastReturned == next) {
                next = n;
            } else {
                nextIndex--;
            }
            lastReturned = null;
        }

        @Override
        public void set(E e) {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            lastReturned.element = e;
        }

        @Override
        public void add(E e) {
            if (next == null) {
                linkedLast(e);
            } else {
                linkedBefore(e, next);
            }
        }
    }
}
