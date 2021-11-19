/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.common.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.utilities.JSONBuilder;

/**
 *
 * @author chiefu
 * @param <T>
 */
public interface InterfaceJSON<T> {

    public static JSONArray convertHashMapList(ArrayList list) {
        JSONArray array = new JSONArray();
        if (list != null) {
            list.stream()
                    .filter((object) -> (object instanceof HashMap))
                    .forEachOrdered((object) -> {
                        array.add(convertHashMap((HashMap) object));
                    });
        }
        return array;
    }

    public static JSONObject convertHashMap(HashMap map) {
        JSONBuilder builder = new JSONBuilder();
        if (map != null) {
            map.keySet().forEach((key) -> {
                Object value = map.get(key);
                if (value instanceof HashMap) {
                    builder.put(key.toString(), convertHashMap((HashMap) value));
                } else if (value instanceof ArrayList) {
                    builder.put(key.toString(), convertHashMapList((ArrayList) value));
                } else {
                    builder.put(key.toString(), value);
                }
            });
        }
        return builder.build();
    }

    public static JSONArray convertInterfaceJsonList(List list) {
        JSONArray array = new JSONArray();
        if (list != null) {
            list.forEach(item -> {
                if (item instanceof InterfaceJSON) {
                    array.add(((InterfaceJSON) item).getJSON());
                }
            });
        }
        return array;
    }

    //-------------------------------------------------------------------------
    JSONObject getJSON();

    T setJSON(JSONObject json);
}
