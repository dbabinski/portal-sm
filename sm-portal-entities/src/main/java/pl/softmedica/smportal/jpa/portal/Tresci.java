/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa.portal;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.JSONObjectExt;

/**
 *
 * @author chiefu
 */
@Entity
@Table(schema = "portal", name = "tresci")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tresci.findAll", query = "SELECT t FROM Tresci t"),
    @NamedQuery(name = "Tresci.findById", query = "SELECT t FROM Tresci t WHERE t.id = :id")
})
public class Tresci implements Serializable, InterfaceJSON<Tresci>, InterfaceDatabaseObject {

    private static final long serialVersionUID = -810140415984446819L;    
    @Id
    @SequenceGenerator(name = "portal.tresci_id_seq", sequenceName = "portal.tresci_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portal.tresci_id_seq")
    @Basic(optional = false)
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "dane_kontaktowe")
    private String daneKontaktowe;
    @Size(max = 2147483647)
    @Column(name = "dane_adresowe")
    private String daneAdresowe;
    @Size(max = 2147483647)
    @Column(name = "naglowek")
    private String naglowek;
    @Size(max = 2147483647)
    @Column(name = "stopka")
    private String stopka;

    public Tresci() {
    }

    public Tresci(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Tresci setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getDaneKontaktowe() {
        return daneKontaktowe;
    }

    public Tresci setDaneKontaktowe(String daneKontaktowe) {
        this.daneKontaktowe = daneKontaktowe;
        return this;
    }

    public String getDaneAdresowe() {
        return daneAdresowe;
    }

    public Tresci setDaneAdresowe(String daneAdresowe) {
        this.daneAdresowe = daneAdresowe;
        return this;
    }

    public String getNaglowek() {
        return naglowek;
    }

    public Tresci setNaglowek(String naglowek) {
        this.naglowek = naglowek;
        return this;
    }

    public String getStopka() {
        return stopka;
    }

    public Tresci setStopka(String stopka) {
        this.stopka = stopka;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.daneKontaktowe);
        hash = 89 * hash + Objects.hashCode(this.daneAdresowe);
        hash = 89 * hash + Objects.hashCode(this.naglowek);
        hash = 89 * hash + Objects.hashCode(this.stopka);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Regulamin)) {
            return false;
        }
        Tresci other = (Tresci) object;
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
        return "pl.softmedica.smportal.jpa.Tresci[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("daneAdresowe", this.daneAdresowe)
                .put("daneKontaktowe", this.daneKontaktowe)
                .put("naglowek", this.naglowek)
                .put("stopka", this.stopka)
                .build();
    }

    @Override
    public Tresci setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.daneAdresowe = jsone.getString("daneAdresowe");
            this.daneKontaktowe = jsone.getString("daneKontaktowe");
            this.naglowek = jsone.getString("naglowek");
            this.stopka = jsone.getString("stopka");            
        }
        return this;
    }

}
