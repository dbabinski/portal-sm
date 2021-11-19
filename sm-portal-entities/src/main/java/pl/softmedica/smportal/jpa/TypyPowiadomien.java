/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.utilities.JSONArrayBuilder;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.JSONObjectExt;
import pl.softmedica.smportal.common.utilities.LinkedHashMapBuilder;

/**
 *
 * @author Damian Babiński <damian.babinski@softmedica.pl>
 */
@Entity
@Table(schema = "slowniki", name = "typy_powiadomien")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TypyPowiadomien.findAll", query = "SELECT t FROM TypyPowiadomien t"),
    @NamedQuery(name = "TypyPowiadomien.findById", query = "SELECT t FROM TypyPowiadomien t WHERE t.id = :id")})
public class TypyPowiadomien implements Serializable, InterfaceJSON<TypyPowiadomien>, InterfaceDatabaseObject {

    private static final long serialVersionUID = -4491829713090875041L;

    @Id
    @SequenceGenerator(name = "slowniki.typy_powiadomien_id_gen", sequenceName = "slowniki.typy_powiadomien_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slowniki.typy_powiadomien_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "opis")
    private String opis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "email")
    private boolean email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sms")
    private boolean sms;

    public TypyPowiadomien() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public TypyPowiadomien setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getOpis() {
        return opis;
    }

    public TypyPowiadomien setOpis(String opis) {
        this.opis = opis;
        return this;
    }

    public boolean getEmail() {
        return email;
    }

    public TypyPowiadomien setEmail(boolean email) {
        this.email = email;
        return this;
    }

    public boolean getSms() {
        return sms;
    }

    public TypyPowiadomien setSms(boolean sms) {
        this.sms = sms;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.opis);
        hash = 89 * hash + Objects.hashCode(this.email);
        hash = 89 * hash + Objects.hash(this.sms);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TypyPowiadomien)) {
            return false;
        }
        TypyPowiadomien other = (TypyPowiadomien) object;
        if (this.id != null && other.id != null) {
            if (!this.id.equals(other.id)) {
                return false;
            }
        } else if (System.identityHashCode(this) != System.identityHashCode(object)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.softmedica.smportal.jpa.TypyPowiadomien[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("opis", this.opis)
                .put("email", this.email)
                .put("sms", this.sms)
                .build();
    }

    @Override
    public TypyPowiadomien setJSON(JSONObject json) {
        if(json != null){
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.opis = jsone.getString("opis");
            this.email = jsone.getBooleanSimple("email");
            this.sms = jsone.getBooleanSimple("sms");
        }
        return this;
    }
    
    public static final JSONArray POLA_WYMAGANE = new JSONArrayBuilder()
            .add("opis")
            .build();
    
    public static final HashMap<String, String> MAPA_POL = new LinkedHashMapBuilder<String, String>()
            .put("opis", "Rodzaj powiadomienia")
            .put("email", "Powiadomienie e-mail")
            .put("sms", "Powiadomienie sms")
            .build();
    
}
