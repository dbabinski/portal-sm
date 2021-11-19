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
 * @author Krzysztof Depka Prądzyński <k.d.pradzynski@softmedica.pl>
 */
@Entity
@Table(schema = "portal", name = "aktualnosci")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aktualnosci.findAll", query = "SELECT a FROM Aktualnosci a ORDER BY a.dataPublikacji DESC"),
    @NamedQuery(name = "Aktualnosci.findById", query = "SELECT a FROM Aktualnosci a WHERE a.id = :id"),
    @NamedQuery(name = "Aktualnosci.findByDataPublikacji", query = "SELECT a FROM Aktualnosci a WHERE a.dataPublikacji = :dataPublikacji"),
    @NamedQuery(name = "Aktualnosci.findByAutor", query = "SELECT a FROM Aktualnosci a WHERE a.autor = :autor"),
    @NamedQuery(name = "Aktualnosci.findByPublikacja", query = "SELECT a FROM Aktualnosci a WHERE a.publikacja = :publikacja ORDER BY a.dataPublikacji DESC")})
public class Aktualnosci implements Serializable, InterfaceJSON<Aktualnosci>, InterfaceDatabaseObject {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "portal.aktualnosci_id_seq", sequenceName = "portal.aktualnosci_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portal.aktualnosci_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_publikacji")
    @Temporal(TemporalType.DATE)
    private Date dataPublikacji = new Date();
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "tresc")
    private String tresc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "tytul")
    private String tytul;
    @Column(name = "skrot")
    private String skrot;
    @Size(max = 2147483647)
    @Column(name = "autor")
    private String autor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "publikacja")
    private boolean publikacja = false;

    public Aktualnosci() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Aktualnosci setId(Integer id) {
        this.id = id;
        return this;
    }

    public Date getDataPublikacji() {
        return dataPublikacji;
    }

    public Aktualnosci setDataPublikacji(Date dataPublikacji) {
        this.dataPublikacji = dataPublikacji;
        return this;
    }

    public String getTresc() {
        return tresc;
    }

    public Aktualnosci setTresc(String tresc) {
        this.tresc = tresc;
        return this;
    }

    public String getTytul() {
        return tytul;
    }

    public Aktualnosci setTytul(String tytul) {
        this.tytul = tytul;
        return this;
    }

    public String getSkrot() {
        return skrot;
    }

    public Aktualnosci setSkrot(String skrot) {
        this.skrot = skrot;
        return this;
    }

    public String getAutor() {
        return autor;
    }

    public Aktualnosci setAutor(String autor) {
        this.autor = autor;
        return this;
    }

    public boolean isPublikacja() {
        return publikacja;
    }

    public Aktualnosci setPublikacja(boolean publikacja) {
        this.publikacja = publikacja;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.dataPublikacji);
        hash = 59 * hash + Objects.hashCode(this.tresc);
        hash = 59 * hash + Objects.hashCode(this.tytul);
        hash = 59 * hash + Objects.hashCode(this.skrot);
        hash = 59 * hash + Objects.hashCode(this.autor);
        hash = 59 * hash + Objects.hashCode(this.publikacja);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Aktualnosci)) {
            return false;
        }
        Aktualnosci other = (Aktualnosci) object;
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
        return "pl.softmedica.smportal.portal.jpa.Aktualnosci[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("dataPublikacji", Utilities.dateToString(dataPublikacji))
                .put("tresc", this.tresc)
                .put("tytul", this.tytul)
                .put("skrot", this.skrot)
                .put("autor", this.autor)
                .put("publikacja", this.publikacja)
                .build();
    }

    @Override
    public Aktualnosci setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.dataPublikacji = jsone.getDate("dataPublikacji");
            this.tresc = jsone.getString("tresc");
            this.tytul = jsone.getString("tytul");
            this.skrot = jsone.getString("skrot");
            this.autor = jsone.getString("autor");
            this.publikacja = jsone.getBooleanSimple("publikacja");
        }
        return this;
    }

    public static final JSONArray POLA_WYMAGANE = new JSONArrayBuilder()            
            .add("tytul")
            .add("tresc")
            .add("dataPublikacji")
            .add("publikacja")
            .build();

    public static final HashMap<String, String> MAPA_POL = new LinkedHashMapBuilder<String, String>()
            .put("dataPublikacji", "Data publikacji")
            .put("tresc", "Treść")
            .put("tytul", "Tytuł")
            .put("skrot", "Skrót wiadomości")
            .put("autor", "Autor")
            .put("publikacja", "Publikacja")
            .build();

}
