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
@Table(schema = "slowniki", name = "typy_dokumentow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TypyDokumentow.findAll", query = "SELECT t FROM TypyDokumentow t"),
    @NamedQuery(name = "TypyDokumentow.findById", query = "SELECT t FROM TypyDokumentow t WHERE t.id = :id")})
public class TypyDokumentow implements Serializable, InterfaceJSON<TypyDokumentow>, InterfaceDatabaseObject {

    private static final long serialVersionUID = 7646187343914338855L;

    @Id
    @SequenceGenerator(name = "slowniki.typy_dokumentow_id_gen", sequenceName = "slowniki.typy_dokumentow_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slowniki.typy_dokumentow_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nazwa_dokumentu_tozsamosci")
    private String nazwaDokumentuTozsamosci;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "format_numeracji_regex")
    private String formatNumeracjiRegex;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "format_numeracji_opis")
    private String formatNumeracjiOpis;

    public TypyDokumentow() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public TypyDokumentow setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNazwaDokumentuTozsamosci() {
        return nazwaDokumentuTozsamosci;
    }

    public TypyDokumentow setNazwaDokumentuTozsamosci(String nazwaDokumentuTozsamosci) {
        this.nazwaDokumentuTozsamosci = nazwaDokumentuTozsamosci;
        return this;
    }

    public String getFormatNumeracjiRegex() {
        return formatNumeracjiRegex;
    }

    public TypyDokumentow setFormatNumeracjiRegex(String formatNumeracjiRegex) {
        this.formatNumeracjiRegex = formatNumeracjiRegex;
        return this;
    }

    public String getFormatNumeracjiOpis() {
        return formatNumeracjiOpis;
    }

    public TypyDokumentow setFormatNumeracjiOpis(String formatNumeracjiOpis) {
        this.formatNumeracjiOpis = formatNumeracjiOpis;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.nazwaDokumentuTozsamosci);
        hash = 89 * hash + Objects.hashCode(this.formatNumeracjiRegex);
        hash = 89 * hash + Objects.hashCode(this.formatNumeracjiOpis);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TypyDokumentow)) {
            return false;
        }
        TypyDokumentow other = (TypyDokumentow) object;
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
        return "pl.softmedica.smportal.jpa.TypyDokumentow[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("nazwaDokumentuTozsamosci", this.nazwaDokumentuTozsamosci)
                .put("formatNumeracjiRegex", this.formatNumeracjiRegex)
                .put("formatNumeracjiOpis", this.formatNumeracjiOpis)
                .build();
    }

    @Override
    public TypyDokumentow setJSON(JSONObject json) {
        if (json != null) {
            this.nazwaDokumentuTozsamosci = Utilities.stringToNull((String) json.get("nazwaDokumentuTozsamosci"));
            this.formatNumeracjiRegex = Utilities.stringToNull((String) json.get("formatNumeracjiRegex"));
            this.formatNumeracjiOpis = Utilities.stringToNull((String) json.get("formatNumeracjiOpis"));
        }
        return this;
    }

    public static final JSONArray POLA_WYMAGANE = new JSONArrayBuilder()
            .add("nazwaDokumentuTozsamosci")
            .add("formatNumeracjiRegex")
            .add("formatNumeracjiOpis")
            .build();

    public static final HashMap<String, String> MAPA_POL = new LinkedHashMapBuilder<String, String>()
            .put("nazwaDokumentuTozsamosci", "Nazwa dokumentu tożsamości")
            .put("formatNumeracjiRegex", "Format numeracji (regex)")
            .put("formatNumeracjiOpis", "Format numeracji (opis)")
            .build();
}
