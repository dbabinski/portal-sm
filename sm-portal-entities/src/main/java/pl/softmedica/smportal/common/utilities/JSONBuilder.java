/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.softmedica.smportal.common.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;


/**
 *
 * @author chiefu
 */
public class JSONBuilder {
    
    private JSONObject jsonObject = new JSONObject();

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public JSONBuilder() {
    }

    public JSONBuilder(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    @Deprecated
    /**
     * @deprecated replaced by {@link #build()}
     */
    public JSONObject getJSONObject() {
        return jsonObject;
    }

    public JSONObject build() {
        return jsonObject;
    }

    public JSONBuilder remove(String key) {
        jsonObject.remove(key);
        return this;
    }

    public JSONBuilder put(String key, Object object) {
        jsonObject.put(key, object instanceof InterfaceJSON ? ((InterfaceJSON) object).getJSON() : object);
        return this;
    }

    public JSONBuilder put(String key, Object object, boolean condition) {
        if (condition) {
            jsonObject.put(key, object instanceof InterfaceJSON ? ((InterfaceJSON) object).getJSON() : object);
        }
        return this;
    }

    public Object get(String key) {
        return jsonObject.get(key);
    }

    public JSONBuilder set(HashMap<String, Object> map) {
        if (map != null) {
            jsonObject = convert(map);
        }
        return this;
    }

    /**
     * Rekurencyjna konwersja HashMap -> JSONObject
     *
     * @param map
     * @return jSONObject
     */
    public static JSONObject convert(HashMap map) {
        JSONObject jsonObject = new JSONObject();
        if (map != null) {
            for (Object key : map.keySet()) {
                if (map.get(key) instanceof ArrayList) {
                    JSONArray array = new JSONArray();
                    for (Object item : (ArrayList) map.get(key)) {
                        if (item instanceof HashMap) {
                            array.add(convert((HashMap) item));
                        }
                    }
                    jsonObject.put(key, array);
                } else {
                    //przepisanie wartości
                    jsonObject.put(key, map.get(key));
                }
            }
        }
        return jsonObject;
    }
}
