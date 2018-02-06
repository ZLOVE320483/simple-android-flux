package com.github.android.flux.actions;

import com.github.android.flux.dispatcher.Dispatcher;
import com.github.android.flux.model.Singer;

/**
 * Created by zlove on 2018/2/5.
 */

public class ActionsCreator {

    private static ActionsCreator instance;
    final Dispatcher dispatcher;

    ActionsCreator(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public static ActionsCreator get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new ActionsCreator(dispatcher);
        }
        return instance;
    }

    public void create(Singer singer) {
        dispatcher.dispatch(SingerActions.ActionType.CREATE_ACTION,
                SingerActions.EventObjectKey.KEY_SINGER,
                singer);
    }

    public void delete(long id) {
        dispatcher.dispatch(SingerActions.ActionType.DELETE_ACTION,
                SingerActions.EventObjectKey.KEY_SINGER_ID,
                id);
    }
}
