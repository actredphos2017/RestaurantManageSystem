package com.sakuno.restaurantmanagesystem;

import java.util.HashMap;
import java.util.Map;

public class Global {

    private static Global instance = null;

    public static Global getInstance() {
        if (instance == null)
            synchronized (Global.class) {
                instance = new Global();
            }
        return instance;
    }

    private Global() {
    }

    private final Map<String, Boolean> orderListUpdated = new HashMap<>();

    public boolean orderUpdated(String id) {
        boolean result = orderListUpdated.getOrDefault(id, false);
        if (result) {
            discoverUpdate(id);
            return true;
        } else return false;
    }

    public void updateOrder(String id) {
        orderListUpdated.put(id, true);
    }

    public void discoverUpdate(String id) {
        orderListUpdated.put(id, false);
    }

}