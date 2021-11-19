/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
 * @author Lucek
 */
@Entity
@Table(schema = "uzytkownicy", name = "uprawnienia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Uprawnienia.findAll", query = "SELECT u FROM Uprawnienia u"),
    @NamedQuery(name = "Uprawnienia.findById", query = "SELECT u FROM Uprawnienia u WHERE u.id = :id")})
public class Uprawnienia implements Serializable, InterfaceJSON<Uprawnienia>, InterfaceDatabaseObject {

    private static final long serialVersionUID = -1730688243080925852L;

    @Id
    @SequenceGenerator(name = "uzytkownicy.uprawnienia_id_gen", sequenceName = "uzytkownicy.uprawnienia_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uzytkownicy.uprawnienia_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "blokowanie_konta")
    private int blokowanieKonta = 0;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "administracja")
    private int administracja = 8;
    
    @JoinColumn(name = "id_grupy", referencedColumnName = "id")
    @ManyToOne
    private Grupy idGrupy;
    
    public static final String BLOKOWANIE_KONTA = "blokowanieKonta";
    public static final String ADMINISTRACJA = "administracja";
    
    public Uprawnienia() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Uprawnienia setId(Integer id) {
        this.id = id;
        return this;
    }

    public int getBlokowanieKonta() {
        return blokowanieKonta;
    }

    public Uprawnienia setBlokowanieKonta(int blokowanieKonta) {
        this.blokowanieKonta = blokowanieKonta;
        return this;
    }
    
    public int getAdministracja() {
        return administracja;
    }

    public Uprawnienia setAdministracja(int administracja) {
        this.administracja = administracja;
        return this;
    }

    public Grupy getIdGrupy() {
        return idGrupy;
    }

    public Uprawnienia setIdGrupy(Grupy idGrupy) {
        this.idGrupy = idGrupy;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.idGrupy);
        hash = 89 * hash + Objects.hashCode(this.blokowanieKonta);
        hash = 89 * hash + Objects.hashCode(this.administracja);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Uprawnienia)) {
            return false;
        }
        Uprawnienia other = (Uprawnienia) object;
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
        return "pl.softmedica.smportal.jpa.Uprawnienia[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put(BLOKOWANIE_KONTA, this.blokowanieKonta)
                .put(ADMINISTRACJA, this.administracja)
                .build();
    }

    @Override
    public Uprawnienia setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.blokowanieKonta = setUprawnienie(jsone.getInteger(BLOKOWANIE_KONTA));
            this.administracja = setUprawnienie(jsone.getInteger(ADMINISTRACJA));
        }
        return this;
    }
    
    private int setUprawnienie(Integer uprawnienie) {
        if (uprawnienie != null && uprawnienie < 8) {
            return 0;
        }
        return uprawnienie;
    }

    public static final List<String> UPRAWNIENIA = Arrays.asList (
            BLOKOWANIE_KONTA,
            ADMINISTRACJA
    );
    
    public static final List<String> UPRAWNIENIA_KOLUMNY = Arrays.asList (
            "blokowanie_konta",
            "administracja"
    );
    
    
    public static final JSONArray POLA_WYMAGANE = new JSONArrayBuilder()
            .add("idGrupy")
            .add(BLOKOWANIE_KONTA)
            .add(ADMINISTRACJA)
            .build();

    public static final HashMap<String, String> MAPA_POL = new LinkedHashMapBuilder<String, String>()
            .put("idGrupy", "Nazwa grupy")
            .put("grupa", "Nazwa grupy")
            .put(BLOKOWANIE_KONTA, "Blokowanie konta")
            .put(ADMINISTRACJA, "Dostęp do panelu administracyjnego")
            .build();

    public static Comparator<Uprawnienia> COMPARATOR_BY_GRUPA = (Uprawnienia o1, Uprawnienia o2) -> {
        if (o1 != null && o2 != null) {
            if (o1.getIdGrupy() != null && o2.getIdGrupy() != null) {
                return Grupy.COMPARATOR_BY_OPIS.compare(o1.getIdGrupy(), o2.getIdGrupy());
            }
            if (o1 != null) {
                return -1;
            }
            if (o2 != null) {
                return 1;
            }
        }
        return 0;
    };
}
