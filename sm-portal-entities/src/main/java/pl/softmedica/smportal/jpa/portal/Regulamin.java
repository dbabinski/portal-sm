/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa.portal;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
import pl.softmedica.smportal.common.utilities.Utilities;

/**
 *
 * @author Lucek
 */
@Entity
@Table(schema = "portal", name = "regulamin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Regulamin.findAll", query = "SELECT t FROM Regulamin t"),
    @NamedQuery(name = "Regulamin.findById", query = "SELECT t FROM Regulamin t WHERE t.id = :id")})
public class Regulamin implements Serializable, InterfaceJSON<Regulamin>, InterfaceDatabaseObject {
    
    private static final long serialVersionUID = -1982054958503652917L;
    
    @Id
    @SequenceGenerator(name = "portal.regulamin_id_seq", sequenceName = "portal.regulamin_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portal.regulamin_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "tresc")
    private String tresc;
    @Column(name = "data_dodania")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dataDodania = new Date();
    
    public Regulamin() {
    }
    
    @Override
    public Integer getId() {
        return id;
    }
    
    public Regulamin setId(Integer id) {
        this.id = id;
        return this;
    }
    
    public String getTresc() {
        return tresc;
    }
    
    public Regulamin setTresc(String tresc) {
        this.tresc = tresc;
        return this;
    }
    
    public Date getDataDodania() {
        return dataDodania;
    }
    
    public Regulamin setDataDodania(Date dataDodania) {
        this.dataDodania = dataDodania;
        return this;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.dataDodania);
        hash = 89 * hash + Objects.hashCode(this.tresc);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Regulamin)) {
            return false;
        }
        Regulamin other = (Regulamin) object;
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
        return "pl.softmedica.smportal.jpa.Regulamin[ id=" + id + " ]";
    }
    
    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("data_dodania", Utilities.dateToString(dataDodania))
                .put("tresc", this.tresc)
                .build();
    }
    
    @Override
    public Regulamin setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.dataDodania = jsone.getDate("data_dodania");
            this.tresc = jsone.getString("tresc");
        }
        return this;
    }
    
    public static final JSONArray POLA_WYMAGANE = new JSONArrayBuilder()
            .add("data_dodania")
            .add("tresc")
            .build();
    
    public static final HashMap<String, String> MAPA_POL = new LinkedHashMapBuilder<String, String>()
            .put("data_dodania", "Data dodania")
            .put("tresc", "Treść")
            .build();
    
}
