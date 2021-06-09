package spinja.util;

import java.util.Iterator;

public abstract class UnModifiableIterator <T> implements Iterator<T> {
    
    public UnModifiableIterator() {
        init();
    }

    public abstract void init();

	public void remove() {
		throw new UnsupportedOperationException();
	}
}

