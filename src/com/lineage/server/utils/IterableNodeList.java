package com.lineage.server.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IterableNodeList implements Iterable<Node> {
    private final NodeList _list;

    /* access modifiers changed from: private */
    public class MyIterator implements Iterator<Node> {
        private int _idx;

        private MyIterator() {
            this._idx = 0;
        }

        /* synthetic */ MyIterator(IterableNodeList iterableNodeList, MyIterator myIterator) {
            this();
        }

        public boolean hasNext() {
            return this._idx < IterableNodeList.this._list.getLength();
        }

        @Override // java.util.Iterator
        public Node next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            NodeList nodeList = IterableNodeList.this._list;
            int i = this._idx;
            this._idx = i + 1;
            return nodeList.item(i);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public IterableNodeList(NodeList list) {
        this._list = list;
    }

    @Override // java.lang.Iterable
    public Iterator<Node> iterator() {
        return new MyIterator(this, null);
    }
}
