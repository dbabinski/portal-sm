/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import java.io.Serializable;
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
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.JSONObjectExt;

/**
 *
 * @author Lucek
 */
@Entity
@Table(schema = "serwis", name = "dostep")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dostep.findAll", query = "SELECT d FROM Dostep d")
})
public class Dostep implements Serializable, InterfaceJSON<Dostep>, InterfaceDatabaseObject {

    private static final long serialVersionUID = -1136239108909804398L;
    public static final int DLUGOSC_SESJI_HTTP = 5;

    @Id
    @SequenceGenerator(name = "serwis.dostep_id_gen", sequenceName = "serwis.dostep_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serwis.dostep_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dlugosc_sesji_http")
    private int dlugoscSesjiHttp = DLUGOSC_SESJI_HTTP;
    @Size(max = 2147483647)
    @Column(name = "ip_rejestracja")
    private String ipRejestracja;
    @Size(max = 2147483647)
    @Column(name = "ip_logowanie")
    private String ipLogowanie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "czas_waznosci_kodu_jednorazowego")
    private int czasWaznosciKoduJednorazowego;

    public Dostep() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Dostep setId(Integer id) {
        this.id = id;
        return this;
    }

    public int getDlugoscSesjiHttp() {
        return dlugoscSesjiHttp;
    }

    public Dostep setDlugoscSesjiHttp(int dlugoscSesjiHttp) {
        this.dlugoscSesjiHttp = dlugoscSesjiHttp;
        return this;
    }

    public String getIpRejestracja() {
        return ipRejestracja;
    }

    public Dostep setIpRejestracja(String ipRejestracja) {
        this.ipRejestracja = ipRejestracja;
        return this;
    }

    public String getIpLogowanie() {
        return ipLogowanie;
    }

    public Dostep setIpLogowanie(String ipLogowanie) {
        this.ipLogowanie = ipLogowanie;
        return this;
    }

    public int getCzasWaznosciKoduJednorazowego() {
        return czasWaznosciKoduJednorazowego;
    }

    public Dostep setCzasWaznosciKoduJednorazowego(int czasWaznosciKoduJednorazowego) {
        this.czasWaznosciKoduJednorazowego = czasWaznosciKoduJednorazowego;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.dlugoscSesjiHttp);
        hash = 89 * hash + Objects.hashCode(this.ipRejestracja);
        hash = 89 * hash + Objects.hashCode(this.ipLogowanie);
        hash = 89 * hash + Objects.hashCode(this.czasWaznosciKoduJednorazowego);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Dostep)) {
            return false;
        }
        Dostep other = (Dostep) object;
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
        return "pl.softmedica.smportal.jpa.Dostep[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("dlugoscSesjiHttp", this.dlugoscSesjiHttp)
                .put("ipRejestracja", this.ipRejestracja)
                .put("ipLogowanie", this.ipLogowanie)
                .put("czasWaznosciKoduJednorazowego", this.czasWaznosciKoduJednorazowego)
                .build();
    }

    @Override
    public Dostep setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.dlugoscSesjiHttp = jsone.getInteger("dlugoscSesjiHttp");
            this.ipRejestracja = jsone.getString("ipRejestracja");
            this.ipLogowanie = jsone.getString("ipLogowanie");
            this.czasWaznosciKoduJednorazowego = jsone.getInteger("czasWaznosciKoduJednorazowego");
        }
        return this;
    }
}
