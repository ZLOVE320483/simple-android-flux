package com.github.android.flux.model;

import android.support.annotation.NonNull;

/**
 * Created by zlove on 2018/2/4.
 */

public class Singer implements Cloneable, Comparable<Singer> {

    private long id;
    private String name;
    private String gender;
    private int age;

    public Singer(long id, String name, String gender, int age) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    @Override
    public Singer clone() {
        return new Singer(id, name, gender, age);
    }

    @Override
    public int compareTo(@NonNull Singer singer) {
        if (id == singer.getId()) {
            return 0;
        } else if (id < singer.getId()) {
            return -1;
        } else {
            return 1;
        }
    }
}
