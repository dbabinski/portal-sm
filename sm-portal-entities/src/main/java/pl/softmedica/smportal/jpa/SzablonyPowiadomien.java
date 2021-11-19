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
 * @author Damian Babiński <damian.babinski@softmedica.pl>
 */
@Entity
@Table(schema = "powiadomienia", name = "szablony_powiadomien")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SzablonyPowiadomien.findAll", query = "SELECT s FROM SzablonyPowiadomien s"),
    @NamedQuery(name = "SzablonyPowiadomien.findById", query = "SELECT s FROM SzablonyPowiadomien s WHERE s.id = :id")})
public class SzablonyPowiadomien implements Serializable, InterfaceJSON<SzablonyPowiadomien>, InterfaceDatabaseObject {

    private static final long serialVersionUID = -5004985515039618387L;
    @Id
    @SequenceGenerator(name = "powiadomienia.szablony_powiadomien_id_gen", sequenceName = "powiadomienia.szablony_powiadomien_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "powiadomienia.szablony_powiadomien_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_sl_typy_powiadomien")
    private int idSlTypyPowiadomien;
    @Size(max = 2147483647)
    @Column(name = "szablony_powiadomien_email_naglowek")
    private String szablonyPowiadomienEmailNaglowek;
    @Size(max = 2147483647)
    @Column(name = "szablony_powiadomien_email_tresc")
    private String szablonyPowiadomienEmailTresc;
    @Size(max = 2147483647)
    @Column(name = "szablony_powiadomien_sms_tresc")
    private String szablonyPowiadomienSmsTresc;

    public SzablonyPowiadomien() {
    }


    @Override
    public Integer getId() {
        return id;
    }

    public SzablonyPowiadomien setId(Integer id) {
        this.id = id;
        return this;
    }

    public int getIdSlTypyPowiadomien() {
        return idSlTypyPowiadomien;
    }

    public SzablonyPowiadomien setIdSlTypyPowiadomien(int idSlTypyPowiadomien) {
        this.idSlTypyPowiadomien = idSlTypyPowiadomien;
        return this;
    }

    public String getSzablonyPowiadomienEmailNaglowek() {
        return szablonyPowiadomienEmailNaglowek;
    }

    public SzablonyPowiadomien setSzablonyPowiadomienEmailNaglowek(String szablonyPowiadomienEmailNaglowek) {
        this.szablonyPowiadomienEmailNaglowek = szablonyPowiadomienEmailNaglowek;
        return this;
    }

    public String getSzablonyPowiadomienEmailTresc() {
        return szablonyPowiadomienEmailTresc;
    }

    public SzablonyPowiadomien setSzablonyPowiadomienEmailTresc(String szablonyPowiadomienEmailTresc) {
        this.szablonyPowiadomienEmailTresc = szablonyPowiadomienEmailTresc;
        return this;
    }

    public String getSzablonyPowiadomienSmsTresc() {
        return szablonyPowiadomienSmsTresc;
    }

    public SzablonyPowiadomien setSzablonyPowiadomienSmsTresc(String szablonyPowiadomienSmsTresc) {
        this.szablonyPowiadomienSmsTresc = szablonyPowiadomienSmsTresc;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.idSlTypyPowiadomien);
        hash = 89 * hash + Objects.hashCode(this.szablonyPowiadomienEmailNaglowek);
        hash = 89 * hash + Objects.hashCode(this.szablonyPowiadomienEmailTresc);
        hash = 89 * hash + Objects.hashCode(this.szablonyPowiadomienSmsTresc);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SzablonyPowiadomien)) {
            return false;
        }
        SzablonyPowiadomien other = (SzablonyPowiadomien) object;
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
        return "pl.softmedica.smportal.jpa.SzablonyPowiadomien[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("idSlTypyPowiadomien", this.idSlTypyPowiadomien)
                .put("szablonyPowiadomienEmailNaglowek", this.szablonyPowiadomienEmailNaglowek)
                .put("szablonyPowiadomienEmailTresc", this.szablonyPowiadomienEmailTresc)
                .put("szablonyPowiadomienSmsTresc", this.szablonyPowiadomienSmsTresc)
                .build();
    }

    @Override
    public SzablonyPowiadomien setJSON(JSONObject jsono) {
        if(jsono != null){
            JSONObjectExt jsone = new JSONObjectExt();
            this.szablonyPowiadomienEmailNaglowek = jsone.getString("szablonyPowiadomienEmailNaglowek");
            this.szablonyPowiadomienEmailTresc = jsone.getString("szablonyPowiadomienEmailTresc");
            this.szablonyPowiadomienSmsTresc = jsone.getString("szablonyPowiadomienSmsTresc");
        }
        return this;
    }
    
}
