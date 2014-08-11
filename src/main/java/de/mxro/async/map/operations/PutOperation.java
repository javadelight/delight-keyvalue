package de.mxro.async.map.operations;

import de.mxro.async.callbacks.SimpleCallback;

public class PutOperation<T> {

	private final T valueToBePut;

	private final SimpleCallback callback;

	public T getValueToBePut() {
		return valueToBePut;
	}

	public SimpleCallback getCallback() {
		return callback;
	}

	public PutOperation(T valueToBePut, SimpleCallback callback) {
		super();
		this.valueToBePut = valueToBePut;
		this.callback = callback;
	}

}
