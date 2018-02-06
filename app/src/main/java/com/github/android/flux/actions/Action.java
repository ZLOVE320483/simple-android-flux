package com.github.android.flux.actions;

import java.util.HashMap;

/**
 * Created by zlove on 2018/2/4.
 */

public class Action {
    private final String type;
    private final HashMap<String, Object> data;

    Action(String type, HashMap<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    public static Builder type(String type) {
        return new Builder().with(type);
    }

    public String getType() {
        return type;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public static class Builder {

        private String type;
        private HashMap<String, Object> data;

        Builder with(String type) {
            if (type == null) {
                throw new IllegalArgumentException("Type may not be null.");
            }
            this.type = type;
            this.data = new HashMap<>();
            return this;
        }

        public Builder bundle(String key, Object value) {
            if (key == null) {
                throw new IllegalArgumentException("Key may not be null.");
            }

            if (value == null) {
                throw new IllegalArgumentException("Value may not be null.");
            }
            data.put(key, value);
            return this;
        }

        public Action build() {
            if (type == null || type.isEmpty()) {
                throw new IllegalArgumentException("At least one key is required.");
            }
            return new Action(type, data);
        }
    }
}
