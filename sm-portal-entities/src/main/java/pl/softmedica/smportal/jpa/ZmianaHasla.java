/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.JSONObjectExt;
import pl.softmedica.smportal.common.utilities.Utilities;

/**
 *
 * @author Brzeziak
 */
@Entity
@Table(schema = "uzytkownicy", name = "zmiana_hasla")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZmianaHasla.findAll", query = "SELECT z FROM ZmianaHasla z"),
    @NamedQuery(name = "ZmianaHasla.findById", query = "SELECT z FROM ZmianaHasla z WHERE z.id = :id"),
    @NamedQuery(name = "ZmianaHasla.findByUUID", query = "SELECT z FROM ZmianaHasla z WHERE z.uuid = :uuid")})
public class ZmianaHasla implements Serializable, InterfaceJSON<ZmianaHasla>, InterfaceDatabaseObject {

    private static final long serialVersionUID = 782448056342856153L;
    @Id
    @SequenceGenerator(name = "uzytkownicy.zmiana_hasla_id_seq", sequenceName = "uzytkownicy.zmiana_hasla_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uzytkownicy.zmiana_hasla_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "uuid")
    private String uuid;
    @Basic(optional = false)
    @Column(name = "token")
    private String token;
    @Column(name = "znacznik_czasu_utworzenia", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date znacznikCzasuUtworzenia;

    public ZmianaHasla() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public ZmianaHasla setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public ZmianaHasla setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getToken() {
        return token;
    }

    public ZmianaHasla setToken(String token) {
        this.token = token;
        return this;
    }

    public Date getZnacznikCzasuUtworzenia() {
        return znacznikCzasuUtworzenia;
    }

    public ZmianaHasla setZnacznikCzasuUtworzenia(Date znacznikCzasuUtworzenia) {
        this.znacznikCzasuUtworzenia = znacznikCzasuUtworzenia;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.uuid);
        hash = 17 * hash + Objects.hashCode(this.token);
        hash = 17 * hash + Objects.hashCode(this.znacznikCzasuUtworzenia);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ZmianaHasla)) {
            return false;
        }
        ZmianaHasla other = (ZmianaHasla) object;
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
        return "pl.softmedica.smportal.jpa.ZmianaHasla[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("uuid", this.uuid)
                .put("token", this.token)
                .put("znacznikCzasuUtworzenia", Utilities.dateToString(this.znacznikCzasuUtworzenia, "yyyy-MM-dd kk:mm"))
                .build();
    }

    @Override
    public ZmianaHasla setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.uuid = jsone.getString("uuid");
            this.token = jsone.getString("token");
            this.znacznikCzasuUtworzenia = jsone.getDate("znacznikCzasuUtworzenia");
        }
        return this;
    }

}
