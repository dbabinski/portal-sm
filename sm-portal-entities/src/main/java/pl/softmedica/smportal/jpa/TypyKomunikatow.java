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
@Table(schema = "slowniki", name = "typy_komunikatow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TypyKomunikatow.findAll", query = "SELECT t FROM TypyKomunikatow t"),
    @NamedQuery(name = "TypyKomunikatow.findById", query = "SELECT t FROM TypyKomunikatow t WHERE t.id = :id")})
public class TypyKomunikatow implements Serializable, InterfaceJSON<TypyKomunikatow>, InterfaceDatabaseObject {

    private static final long serialVersionUID = 6149188207063274283L;

    @Id
    @SequenceGenerator(name = "slowniki.typy_komunikatow_id_gen", sequenceName = "slowniki.typy_komunikatow_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slowniki.typy_komunikatow_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "opis")
    private String opis;

    public TypyKomunikatow() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public TypyKomunikatow setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getOpis() {
        return opis;
    }

    public TypyKomunikatow setOpis(String opis) {
        this.opis = opis;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.opis);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TypyKomunikatow)) {
            return false;
        }
        TypyKomunikatow other = (TypyKomunikatow) object;
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
        return "pl.softmedica.smportal.jpa.TypyKomunikatow[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("opis", this.opis)
                .build();
    }

    @Override
    public TypyKomunikatow setJSON(JSONObject json) {
        if (json != null) {
            this.opis = Utilities.stringToNull((String) json.get("opis"));
        }
        return this;
    }

    public static final JSONArray POLA_WYMAGANE = new JSONArrayBuilder()
            .add("opis")
            .build();

    public static final HashMap<String, String> MAPA_POL = new LinkedHashMapBuilder<String, String>()
            .put("opis", "Opis")
            .build();

}
