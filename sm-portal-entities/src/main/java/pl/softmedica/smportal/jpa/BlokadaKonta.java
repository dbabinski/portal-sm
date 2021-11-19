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
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.Utilities;

/**
 *
 * @author Lucek
 */
@Entity
@Table(schema = "serwis", name = "blokada_konta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BlokadaKonta.findAll", query = "SELECT b FROM BlokadaKonta b")
})
public class BlokadaKonta implements Serializable, InterfaceJSON<BlokadaKonta>, InterfaceDatabaseObject {

    private static final long serialVersionUID = 3351166163265768873L;

    @Id
    @SequenceGenerator(name = "serwis.blokada_konta_id_gen", sequenceName = "serwis.blokada_konta_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serwis.blokada_konta_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "liczba_bledow")
    private int liczbaBledow;
    @Basic(optional = false)
    @NotNull
    @Column(name = "czas_blokady")
    private int czasBlokady;

    public BlokadaKonta() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public BlokadaKonta setId(Integer id) {
        this.id = id;
        return this;
    }

    public int getLiczbaBledow() {
        return liczbaBledow;
    }

    public BlokadaKonta setLiczbaBledow(int liczbaBledow) {
        this.liczbaBledow = liczbaBledow;
        return this;
    }

    public int getCzasBlokady() {
        return czasBlokady;
    }

    public BlokadaKonta setCzasBlokady(int czasBlokady) {
        this.czasBlokady = czasBlokady;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.liczbaBledow);
        hash = 89 * hash + Objects.hashCode(this.czasBlokady);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BlokadaKonta)) {
            return false;
        }
        BlokadaKonta other = (BlokadaKonta) object;
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
        return "pl.softmedica.smportal.jpa.BlokadaKonta[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("liczbaBledow", this.liczbaBledow)
                .put("czasBlokady", this.czasBlokady)
                .build();
    }

    @Override
    public BlokadaKonta setJSON(JSONObject json) {
        if (json != null) {
            this.liczbaBledow = Integer.valueOf(Utilities.stringToNull((String) json.get("liczbaBledow")));
            this.czasBlokady = Integer.valueOf(Utilities.stringToNull((String) json.get("czasBlokady")));
        }
        return this;
    }

}
