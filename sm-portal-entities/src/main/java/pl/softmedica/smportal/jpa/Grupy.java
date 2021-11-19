/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import pl.softmedica.smportal.common.utilities.Utilities;

/**
 *
 * @author Lucek
 */
@Entity
@Table(schema = "uzytkownicy", name = "grupy")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupy.findAll", query = "SELECT g FROM Grupy g"),
    @NamedQuery(name = "Grupy.findById", query = "SELECT g FROM Grupy g WHERE g.id = :id"),
    @NamedQuery(name = "Grupy.findByIdTypGrupy", query = "SELECT g FROM Grupy g WHERE g.idTypGrupy = :idTypGrupy"),
    @NamedQuery(name = "Grupy.findByAktywna", query = "SELECT g FROM Grupy g WHERE g.aktywna = :aktywna")})
public class Grupy implements Serializable, InterfaceJSON<Grupy>, InterfaceDatabaseObject {

    private static final long serialVersionUID = 3508053034015876442L;
    
    public static final String GRUPA_ADMINISTRATORZY_OPIS = "Administratorzy";
    public static final String GRUPA_UZYTKOWNICY_OPIS = "Użytkownicy";
    public static final String GRUPA_KONTRAHENCI_OPIS = "Kontrahenci";
    
    @Id
    @SequenceGenerator(name = "uzytkownicy.grupy_id_gen", sequenceName = "uzytkownicy.grupy_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uzytkownicy.grupy_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_typ_grupy", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private TypyGrup idTypGrupy;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "opis")
    private String opis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aktywna")
    private boolean aktywna;

    public Grupy() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Grupy setId(Integer id) {
        this.id = id;
        return this;
    }

    public TypyGrup getTypGrupy() {
        return idTypGrupy;
    }

    public Grupy setTypGrupy(TypyGrup typGrupy) {
        this.idTypGrupy = typGrupy;
        return this;
    }

    public String getOpis() {
        return opis;
    }

    public Grupy setOpis(String opis) {
        this.opis = opis;
        return this;
    }

    public boolean isAktywna() {
        return aktywna;
    }

    public Grupy setAktywna(boolean aktywna) {
        this.aktywna = aktywna;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.idTypGrupy);
        hash = 89 * hash + Objects.hashCode(this.opis);
        hash = 89 * hash + Objects.hashCode(this.aktywna);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Grupy)) {
            return false;
        }
        Grupy other = (Grupy) object;
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
        return "pl.softmedica.smportal.jpa.Grupy[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("idTypGrupy", this.idTypGrupy != null ? this.idTypGrupy.getId() : null)
                .put("slownikTypyGrup", this.idTypGrupy != null ? this.idTypGrupy.getJSON() : null)
                .put("opis", this.opis)
                .put("aktywna", this.aktywna)
                .build();
    }

    @Override
    public Grupy setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.opis = jsone.getString("opis");
            this.aktywna = jsone.getBooleanSimple("aktywna");
        }
        return this;
    }

    public static final JSONArray POLA_WYMAGANE = new JSONArrayBuilder()
            .add("idTypGrupy")
            .add("opis")
            .build();

    public static final HashMap<String, String> MAPA_POL = new LinkedHashMapBuilder<String, String>()
            .put("idTypGrupy", "Typ grupy")
            .put("slownikTypyGrup", "Typ grupy")
            .put("opis", "Opis")
            .put("aktywna", "Aktywna")
            .build();

    public static Comparator<Grupy> COMPARATOR_BY_OPIS = (Grupy o1, Grupy o2) -> {
        if (o1 != null && o2 != null) {
            return Utilities.nullToString(o1.getOpis()).toString()
                    .compareTo(Utilities.nullToString(o2.getOpis()).toString());
        }
        return 0;
    };
}
