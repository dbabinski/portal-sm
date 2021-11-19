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
import pl.softmedica.smportal.common.utilities.LinkedHashMapBuilder;
import pl.softmedica.smportal.common.utilities.Utilities;

/**
 *
 * @author Lucek
 */
@Entity
@Table(schema = "slowniki", name = "typy_grup")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TypyGrup.findAll", query = "SELECT t FROM TypyGrup t"),
    @NamedQuery(name = "TypyGrup.findById", query = "SELECT t FROM TypyGrup t WHERE t.id = :id")})
public class TypyGrup implements Serializable, InterfaceJSON<TypyGrup>, InterfaceDatabaseObject {

    private static final long serialVersionUID = 3421681769425335563L;

    @Id
    @SequenceGenerator(name = "slowniki.typy_grup_id_gen", sequenceName = "slowniki.typy_grup_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slowniki.typy_grup_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nazwa")
    private String nazwa;

    public TypyGrup() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public TypyGrup setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNazwa() {
        return nazwa;
    }

    public TypyGrup setNazwa(String nazwa) {
        this.nazwa = nazwa;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.nazwa);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TypyGrup)) {
            return false;
        }
        TypyGrup other = (TypyGrup) object;
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
        return "pl.softmedica.smportal.jpa.TypyGrup[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("nazwa", this.nazwa)
                .build();
    }

    @Override
    public TypyGrup setJSON(JSONObject json) {
        if (json != null) {
            this.nazwa = Utilities.stringToNull((String) json.get("nazwa"));
        }
        return this;
    }

    public static final JSONArray POLA_WYMAGANE = new JSONArrayBuilder()
            .add("nazwa")
            .build();

    public static final HashMap<String, String> MAPA_POL = new LinkedHashMapBuilder<String, String>()
            .put("nazwa", "Nazwa")
            .build();

}
