/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.JSONObjectExt;
import pl.softmedica.smportal.common.utilities.Utilities;

/**
 *
 * @author Lucek
 */
public class AktualnieZalogowani implements Serializable, InterfaceJSON<AktualnieZalogowani> {

    private static final long serialVersionUID = 6110737346133644681L;
    
    String uuid;
    Date dataWygasniecia;
    String ip;
    String aplikacja = "portal";
    String email;

    public String getUuid() {
        return uuid;
    }

    public AktualnieZalogowani setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Date getDataWygasniecia() {
        return dataWygasniecia;
    }

    public AktualnieZalogowani setDataWygasniecia(Date dataWygasniecia) {
        this.dataWygasniecia = dataWygasniecia;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public AktualnieZalogowani setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getAplikacja() {
        return aplikacja;
    }

    public AktualnieZalogowani setAplikacja(String aplikacja) {
        this.aplikacja = aplikacja;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AktualnieZalogowani setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("uuid", this.uuid)
                .put("dataWygasniecia", Utilities.dateToString(this.getDataWygasniecia(), Utilities.DATE_TIME_SEC_FORMAT))
                .put("ip", this.ip)
                .put("aplikacja", this.aplikacja)
                .put("email", this.email)
                .build();
    }

    @Override
    public AktualnieZalogowani setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.uuid = jsone.getString("uuid");
            this.dataWygasniecia = jsone.getDateTimeSec("dataWygasniecia");
            this.ip = jsone.getString("ip");
            this.aplikacja = jsone.getString("aplikacja");
            this.email = jsone.getString("email");
        }
        return this;
    }
    
    public static Comparator<AktualnieZalogowani> COMPARATOR_BY_UUID = (AktualnieZalogowani o1, AktualnieZalogowani o2) -> {
        if (o1 != null && o2 != null) {
            return o1.getUuid().compareTo(o2.getUuid());
        }
        return 0;
    };
    
    
}
