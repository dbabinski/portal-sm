/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
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
@Table(schema = "slowniki", name = "komorki_organizacyjne")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KomorkiOrganizacyjne.findAll", query = "SELECT k FROM KomorkiOrganizacyjne k"),
    @NamedQuery(name = "KomorkiOrganizacyjne.findById", query = "SELECT k FROM KomorkiOrganizacyjne k WHERE k.id = :id"),
    @NamedQuery(name = "KomorkiOrganizacyjne.findByIdKomorkiOrganizacyjnej", query = "SELECT k FROM KomorkiOrganizacyjne k WHERE k.idKomorkiOrganizacyjnej = :idKomorkiOrganizacyjnej")})
public class KomorkiOrganizacyjne implements Serializable, InterfaceJSON<KomorkiOrganizacyjne>, InterfaceDatabaseObject {

    private static final long serialVersionUID = 2980809468083329329L;

    @Id
    @SequenceGenerator(name = "uzytkownicy.grupy_id_gen", sequenceName = "uzytkownicy.grupy_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uzytkownicy.grupy_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_komorki_organizacyjnej", referencedColumnName = "id")
    @ManyToOne
    private KomorkiOrganizacyjne idKomorkiOrganizacyjnej;
    @OneToMany(mappedBy = "idKomorkiOrganizacyjnej", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<KomorkiOrganizacyjne> komorkiOrganizacyjne = new ArrayList<>(0);
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nazwa")
    private String nazwa;
    @Size(max = 2147483647)
    @Column(name = "opis")
    private String opis;

    public KomorkiOrganizacyjne() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public KomorkiOrganizacyjne setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNazwa() {
        return nazwa;
    }

    public KomorkiOrganizacyjne setNazwa(String nazwa) {
        this.nazwa = nazwa;
        return this;
    }

    public String getOpis() {
        return opis;
    }

    public KomorkiOrganizacyjne setOpis(String opis) {
        this.opis = opis;
        return this;
    }

    public KomorkiOrganizacyjne getIdKomorkiOrganizacyjnej() {
        return idKomorkiOrganizacyjnej;
    }

    public KomorkiOrganizacyjne setIdKomorkiOrganizacyjnej(KomorkiOrganizacyjne idKomorkiOrganizacyjnej) {
        //zabezpieczenie przed związaniem z samą sobą
        if (idKomorkiOrganizacyjnej != null && idKomorkiOrganizacyjnej.equals(this)) {
            return this;
        }
        this.idKomorkiOrganizacyjnej = idKomorkiOrganizacyjnej;
        return this;
    }

    @XmlTransient
    public List<KomorkiOrganizacyjne> getKomorkiOrganizacyjneList() {
        return komorkiOrganizacyjne;
    }

    public KomorkiOrganizacyjne setKomorkiOrganizacyjneList(List<KomorkiOrganizacyjne> komorkiOrganizacyjne) {
        this.komorkiOrganizacyjne = komorkiOrganizacyjne;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.idKomorkiOrganizacyjnej);
        hash = 89 * hash + Objects.hashCode(this.nazwa);
        hash = 89 * hash + Objects.hashCode(this.opis);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KomorkiOrganizacyjne)) {
            return false;
        }
        KomorkiOrganizacyjne other = (KomorkiOrganizacyjne) object;
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
        return "pl.softmedica.smportal.jpa.KomorkiOrganizacyjne[ id=" + id + " ]";
    }

    public JSONObject getJSON(boolean zNadrzednaKomorkaOrganizacyjna) {
        JSONBuilder builder = new JSONBuilder()
                .put("id", this.id)
                .put("idKomorkiOrganizacyjnej", this.idKomorkiOrganizacyjnej != null ? this.idKomorkiOrganizacyjnej.getId() : null)
                .put("nazwa", this.nazwa)
                .put("opis", this.opis);
        if (zNadrzednaKomorkaOrganizacyjna) {
            builder.put("nadrzednaKomorkaOrganizacyjna", this.idKomorkiOrganizacyjnej != null ? this.idKomorkiOrganizacyjnej.getJSON(false) : null);
        }
        return builder.build();
    }

    @Override
    public JSONObject getJSON() {
        return getJSON(true);
    }

    @Override
    public KomorkiOrganizacyjne setJSON(JSONObject json) {
        if (json != null) {
            this.nazwa = Utilities.stringToNull((String) json.get("nazwa"));
            this.opis = Utilities.stringToNull((String) json.get("opis"));
        }
        return this;
    }

    public static final JSONArray POLA_WYMAGANE = new JSONArrayBuilder()
            .add("nazwa")
            .add("opis")
            .build();

    public static final HashMap<String, String> MAPA_POL = new LinkedHashMapBuilder<String, String>()
            .put("nazwa", "Nazwa")
            .put("opis", "Opis")
            .put("idKomorkiOrganizacyjnej", "Komórka nadrzędna")
            .put("nadrzednaKomorkaOrganizacyjna", "Komórka nadrzędna")
            .build();
}
