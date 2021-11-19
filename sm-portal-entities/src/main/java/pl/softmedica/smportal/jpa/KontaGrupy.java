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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.utilities.JSONBuilder;

 
/**
 *
 * @author Łukasz Brzeziński<lukasz.brzezinski@softmedica.pl>
 */
@Entity
@Table(schema = "uzytkownicy", name = "konta_grupy")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KontaGrupy.findAll", query = "SELECT k FROM KontaGrupy k"),
    @NamedQuery(name = "KontaGrupy.findById", query = "SELECT k FROM KontaGrupy k WHERE k.id = :id")})
public class KontaGrupy implements Serializable, InterfaceJSON<KontaGrupy>, InterfaceDatabaseObject {
 
    private static final long serialVersionUID = -4277675501348792004L;
 
    @Id
    @SequenceGenerator(name = "uzytkownicy.konta_grupy_id_gen", sequenceName = "uzytkownicy.konta_grupy_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uzytkownicy.konta_grupy_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @JoinColumn(name = "id_konta", referencedColumnName = "id")
    @ManyToOne
    private Konta konto;
    
    @JoinColumn(name = "id_grupy", referencedColumnName = "id")
    @ManyToOne
    private Grupy grupa;
 
    public KontaGrupy() {
    }
 
    @Override
    public Integer getId() {
        return id;
    }
 
    public KontaGrupy setId(Integer id) {
        this.id = id;
        return this;
    }
 
    public Konta getKonto() {
        return konto;
    }
 
    public KontaGrupy setKonto(Konta konto) {
        this.konto = konto;
        return this;
    }
 
    public Grupy getGrupa() {
        return grupa;
    }
 
    public KontaGrupy setGrupa(Grupy grupa) {
        this.grupa = grupa;
        return this;
    }
 
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.konto);
        hash = 89 * hash + Objects.hashCode(this.grupa);
        return hash;
    }
 
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KontaGrupy)) {
            return false;
        }
        KontaGrupy other = (KontaGrupy) object;
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
        return "pl.softmedica.euslugi.jpa.KontaGrupy[ id=" + id + " ]";
    }
 
    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("konto", this.konto)
                .put("grupa", this.grupa)
                .build();
    }
 
    @Override
    public KontaGrupy setJSON(JSONObject json) {
        return this;
    }
}