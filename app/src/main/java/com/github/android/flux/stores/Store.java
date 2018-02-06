package com.github.android.flux.stores;

import com.github.android.flux.actions.Action;
import com.github.android.flux.dispatcher.Dispatcher;

/**
 * Created by zlove on 2018/2/5.
 */

public abstract class Store {

    final Dispatcher dispatcher;

    protected Store(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    void emitStoreChange() {
        dispatcher.emitChange(changeEvent());
    }

    abstract StoreChangeEvent changeEvent();

    public abstract void onAction(Action action);

    public interface StoreChangeEvent {}
}
