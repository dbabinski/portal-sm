/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.softmedica.smportal.common.utilities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author chiefu
 */
public class JSONObjectExt extends JSONObject {
    
    public JSONObjectExt() {
        super();
    }
    
    public JSONObjectExt(String content) throws ParseException {
        this();
        if (content != null) {
            JSONObject object = (JSONObject) new JSONParser().parse(content);
            if (object != null) {
                for (Object entry : object.entrySet()) {
                    if (entry instanceof Map.Entry) {
                        Map.Entry mapEntry = (Map.Entry) entry;
                        this.put(mapEntry.getKey(), mapEntry.getValue());
                    }
                }
            }
        }
    }
    
    public JSONObjectExt(Object object) {
        this(object instanceof JSONObject ? (JSONObject) object : null);
    }
    
    public JSONObjectExt(JSONObject object) {
        this();
        if (object != null) {
            for (Object entry : object.entrySet()) {
                if (entry instanceof Map.Entry) {
                    Map.Entry mapEntry = (Map.Entry) entry;
                    this.put(mapEntry.getKey(), mapEntry.getValue());
                }
            }
        }
    }
    
    public boolean isNull(Object key) {
        return getString(key) == null;
    }
    
    public JSONObjectExt getObject(Object key) {
        return new JSONObjectExt(super.get(key));
    }
    
    public JSONArray getArray(Object key) {
        Object object = super.get(key);
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        return new JSONArray();
    }
    
    public String getString(Object key) {
        Object object = super.get(key);
        if (object != null) {
            return Utilities.stringToNull(object.toString());
        }
        return null;
    }
    
    public String getString(Object key, boolean notNull) {
        return notNull
                ? Utilities.nullToString(getString(key)).toString()
                : getString(key);
    }
    
    public Date getDate(Object key) {
        Object object = super.get(key);
        if (object != null) {
            return Utilities.stringToDate(object.toString());
        }
        return null;
    }
    
    public Date getTime(Object key) {
        Object object = super.get(key);
        if (object != null) {
            return Utilities.stringToDate(object.toString(), Utilities.TIME_FORMAT);
        }
        return null;
    }
    
    public Date getDateTime(Object key) {
        Object object = super.get(key);
        if (object != null) {
            return Utilities.stringToDate(object.toString(), Utilities.DATE_TIME_FORMAT);
        }
        return null;
    }
    
    public Date getDateTimeSec(Object key) {
        Object object = super.get(key);
        if (object != null) {
            return Utilities.stringToDate(object.toString(), Utilities.DATE_TIME_SEC_FORMAT);
        }
        return null;
    }
    
    public Integer getInteger(Object key) {
        Object object = super.get(key);
        if (object != null) {
            try {
                return Integer.parseInt(object.toString());
            } catch (NumberFormatException ex) {
            }
        }
        return null;
    }
    
    public Float getFloat(Object key) {
        Object object = super.get(key);
        if (object != null) {
            try {
                return Float.parseFloat(object.toString());
            } catch (NumberFormatException ex) {
            }
        }
        return null;
    }
    
    public Boolean getBoolean(Object key) {
        Object object = super.get(key);
        if (object != null) {
            return Boolean.parseBoolean(object.toString());
        }
        return null;
    }
    
    public boolean getBooleanSimple(Object key) {
        Object object = super.get(key);
        if (object != null) {
            return Boolean.parseBoolean(object.toString());
        }
        return false;
    }
    
    public BigDecimal getBigDecimal(Object key) {
        Object object = super.get(key);
        if (object != null) {
            try {
                return new BigDecimal(object.toString().replaceAll(",", "."));
            } catch (NumberFormatException ex) {
            }
        }
        return null;
    }
    
    public JSONObjectExt extractEntity() {
        if (this.containsKey("entity")) {
            try {
                return new JSONObjectExt(this.getString("entity"));
            } catch (ParseException ex) {
            }
        }
        return this;
    }
}
