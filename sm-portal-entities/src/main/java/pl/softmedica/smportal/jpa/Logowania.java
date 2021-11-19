/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.utilities.JSONBuilder;

/**
 *
 * @author Lucek
 */
@Entity
@Table(schema = "logi", name = "logowania")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Logowania.findAll", query = "SELECT l FROM Logowania l")
})
public class Logowania implements Serializable, InterfaceJSON<Logowania>,  InterfaceDatabaseObject {

    private static final long serialVersionUID = 2780187919418236657L;

    @Id
    @SequenceGenerator(name = "logi.logowania_id_gen", sequenceName = "logi.logowania_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logi.logowania_id_gen")
    @Basic(optional = false)
    @Column(name = "id", insertable = false, updatable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "data", columnDefinition = "('now'::text)::date", insertable = false)
    @Temporal(TemporalType.DATE)
    private Date data;
    @Basic(optional = false)
    @Column(name = "czas", columnDefinition = "('now'::text)::time without time zone", insertable = false)
    @Temporal(TemporalType.TIME)
    private Date czas;
    @Size(min = 1, max = 2147483647)
    @Column(name = "ip")
    private String ip;
    @Size(min = 1, max = 2147483647)
    @Column(name = "opis")
    private String opis;        
    @Size(min = 1, max = 2147483647)
    @Column(name = "uuid_konta")
    private String uuidKonta;

    public Integer getId() {
        return id;
    }

    public Logowania setId(Integer id) {
        this.id = id;
        return this;
    }

    public Date getData() {
        return data;
    }

    public Logowania setData(Date data) {
        this.data = data;
        return this;
    }

    public Date getCzas() {
        return czas;
    }

    public Logowania setCzas(Date czas) {
        this.czas = czas;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public Logowania setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getOpis() {
        return opis;
    }

    public Logowania setOpis(String opis) {
        this.opis = opis;
        return this;
    }

    public String getUuidKonta() {
        return uuidKonta;
    }

    public Logowania setUuidKonta(String uuidKonta) {
        this.uuidKonta = uuidKonta;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.data);
        hash = 89 * hash + Objects.hashCode(this.czas);
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.opis);
        hash = 89 * hash + Objects.hashCode(this.uuidKonta);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Logowania)) {
            return false;
        }
        Logowania other = (Logowania) object;
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
        return "pl.softmedica.smportal.zarzadzanie.jpa.Logowania[ id=" + id + " ]";
    }    

    public static Comparator<Logowania> COMPARATOR_BY_ID = (Logowania o1, Logowania o2) -> {
        if (o1 != null && o2 != null) {
            return o1.getId().compareTo(o2.getId());
        }
        return 0;
    };
    
    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("data", this.data)
                .put("czas", this.czas)
                .put("ip", this.ip)
                .put("opis", this.opis)
                .put("uuidKonta", this.uuidKonta)
                .build();
    }
    
    @Override
    public Logowania setJSON(JSONObject json) {
        return this;
    }    
}
