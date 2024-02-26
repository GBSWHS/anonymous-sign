package com.threlease.base.functions.maps;

import com.threlease.base.utils.Session;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

public class Data {
    private HashMap<String, Session> map;

    public void check() {
        map.forEach((key, data) -> {
            if (data.created_At.getTime() + (1000 * 60 * 60 * 24 * 24) < new Date(System.currentTimeMillis()).getTime()) {
                this.del(key);
            }
        });
    }

    public Optional<Session> get(String id) {
        check();

        if (map.containsKey(id)) {
            return Optional.of(map.get(id));
        } else {
            return Optional.empty();
        }
    }

    public boolean put(String id, Session data) {
        if (map.containsKey(id)) {
            return false;
        } else {
            map.put(id, data);
            return true;
        }
    }

    public boolean del(String id) {
        if (map.containsKey(id)) {
            map.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public void update(String id, Session data) {
        map.replace(id, data);
    }
}
