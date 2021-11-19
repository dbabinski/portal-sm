/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.softmedica.smportal.common.utilities;

import java.text.Collator;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;

/**
 *
 * @author chiefu
 */
public class JSONArrayBuilder {
    
    private JSONArray jsonArray = new JSONArray();

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public JSONArrayBuilder() {
    }

    public JSONArrayBuilder(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    public JSONArray build() {
        return jsonArray;
    }

    public JSONArrayBuilder add(Object object) {
        jsonArray.add(object);
        return this;
    }

    public JSONArrayBuilder add(Object object, boolean condition) {
        if (condition) {
            jsonArray.add(object);
        }
        return this;
    }

    public JSONArrayBuilder addAll(List objects) {
        if (!objects.isEmpty()) {
            objects.stream()
                    .filter(object -> object instanceof InterfaceJSON)
                    .map(object -> ((InterfaceJSON) object).getJSON())
                    .forEach(jsonObject -> {
                        add(jsonObject);
                    });
        }
        return this;
    }

    public boolean isEmpty() {
        return jsonArray.isEmpty();
    }

    public List<JSONObject> getAsList() {
        if (jsonArray != null) {
            return (List<JSONObject>) jsonArray
                    .stream()
                    .map(object -> {
                        return (JSONObject) object;
                    })
                    .collect(Collectors.toCollection(LinkedList::new));
        }
        return new LinkedList<>();
    }
    
    public List<JSONObjectExt> getAsListExt() {
        if (jsonArray != null) {
            return (List<JSONObjectExt>) jsonArray
                    .stream()
                    .map(object -> {
                        return new JSONObjectExt(object);
                    })
                    .collect(Collectors.toCollection(LinkedList::new));
        }
        return new LinkedList<>();
    }

    public JSONArrayBuilder sort(Comparator<JSONObject> comparator) {
        if (comparator != null) {
            JSONArrayBuilder builder = new JSONArrayBuilder();
            getAsList().stream().sorted(comparator).forEachOrdered(jsonObject -> {
                builder.add(jsonObject);
            });
            this.jsonArray = builder.build();
        }
        return this;
    }

    public JSONArrayBuilder sort(String key) {
        if (key != null) {
            Comparator<JSONObject> comparator = new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject objectA, JSONObject objectB) {
                    int compare = 0;
                    if (objectA != null && objectB != null) {
                        JSONObjectExt jsonObjectA = new JSONObjectExt(objectA);
                        JSONObjectExt jsonObjectB = new JSONObjectExt(objectB);
                        if (jsonObjectA != null && jsonObjectB != null) {
                            String keyA = jsonObjectA.getString(key);
                            String keyB = jsonObjectB.getString(key);                            
                            if (keyA != null && keyB != null) {
                                Collator collator = Collator.getInstance(Locale.getDefault());
                                compare = collator.compare(keyA.toLowerCase(), keyB.toLowerCase());
//                                compare = keyA.compareToIgnoreCase(keyB);
                            } else if (keyA != null) {
                                compare = -1;
                            } else if (keyB != null) {
                                compare = 1;
                            }
                        }
                    }
                    return compare;
                }
            };
            return sort(comparator);
        }
        return this;
    }
}
