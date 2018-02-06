package com.github.android.flux.actions;

/**
 * Created by zlove on 2018/2/5.
 */

public class SingerActions {

    public interface ActionType {
        String CREATE_ACTION = "create-action";
        String DELETE_ACTION = "delete-action";
    }
    public interface EventObjectKey {
        String KEY_SINGER = "key_singer";
        String KEY_SINGER_ID = "key_singer_id";
    }
}
