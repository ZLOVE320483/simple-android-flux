package com.github.android.flux.stores;

import com.github.android.flux.actions.Action;
import com.github.android.flux.actions.SingerActions;
import com.github.android.flux.dispatcher.Dispatcher;
import com.github.android.flux.model.Singer;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zlove on 2018/2/5.
 */

public class SingerStore extends Store {

    private static SingerStore instance;
    private final List<Singer> singers;

    public SingerStore(Dispatcher dispatcher) {
        super(dispatcher);
        singers = new ArrayList<>();
    }

    public static SingerStore get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new SingerStore(dispatcher);
        }
        return instance;
    }

    public List<Singer> getSingers() {
        return singers;
    }

    @Override
    @Subscribe
    public void onAction(Action action) {
        switch (action.getType()) {
            case SingerActions.ActionType.CREATE_ACTION:
                Singer singer =((Singer) action.getData().get(SingerActions.EventObjectKey.KEY_SINGER));
                addElement(singer);
                emitStoreChange();
                break;
            case SingerActions.ActionType.DELETE_ACTION:
                long id = ((long) action.getData().get(SingerActions.EventObjectKey.KEY_SINGER_ID));
                deleteElement(id);
                emitStoreChange();
                break;
        }
    }


    private void addElement(Singer clone) {
        singers.add(clone);
        Collections.sort(singers);
    }

    private void deleteElement(long id) {
        Iterator<Singer> iterator = singers.iterator();
        while (iterator.hasNext()) {
            Singer singer = iterator.next();
            if (singer.getId() == id) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    StoreChangeEvent changeEvent() {
        return new SingerStoreChangeEvent();
    }

    public class SingerStoreChangeEvent implements StoreChangeEvent {
    }
}
