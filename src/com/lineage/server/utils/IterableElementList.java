package com.lineage.server.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IterableElementList implements Iterable<Element> {
    IterableNodeList _list;

    /* access modifiers changed from: private */
    public class MyIterator implements Iterator<Element> {
        private Iterator<Node> _itr;
        private Element _next = null;

        public MyIterator(Iterator<Node> itr) {
            this._itr = itr;
            updateNextElement();
        }

        private void updateNextElement() {
            while (this._itr.hasNext()) {
                Node node = this._itr.next();
                if (node instanceof Element) {
                    this._next = (Element) node;
                    return;
                }
            }
            this._next = null;
        }

        public boolean hasNext() {
            return this._next != null;
        }

        @Override // java.util.Iterator
        public Element next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Element result = this._next;
            updateNextElement();
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public IterableElementList(NodeList list) {
        this._list = new IterableNodeList(list);
    }

    @Override // java.lang.Iterable
    public Iterator<Element> iterator() {
        return new MyIterator(this._list.iterator());
    }
}
