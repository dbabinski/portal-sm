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
@Table(schema = "serwis", name = "parametry_hasla")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametryHasla.findAll", query = "SELECT p FROM ParametryHasla p"),
    @NamedQuery(name = "ParametryHasla.findById", query = "SELECT p FROM ParametryHasla p WHERE p.id = :id")})
public class ParametryHasla implements Serializable, InterfaceJSON<ParametryHasla>, InterfaceDatabaseObject {

    public static final char[] ZNAKI_SPECJALNE_TABLICA = {
        '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.',
        '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`',
        '{', '|', '}'};

    public static final String ZNAKI_SPECJALNE = String.valueOf(ZNAKI_SPECJALNE_TABLICA);

    private static final long serialVersionUID = -1348897228322219134L;

    @Id
    @SequenceGenerator(name = "serwis.parametry_hasla_id_gen", sequenceName = "serwis.parametry_hasla_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serwis.parametry_hasla_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "minimalna_dlugosc")
    private int minimalnaDlugosc = 8;
    @Basic(optional = false)
    @NotNull
    @Column(name = "liczba_znakow_specjalnych")
    private int liczbaZnakowSpecjalnych = 1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "liczba_cyfr")
    private int liczbaCyfr = 1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "wielkosc_liter")
    private boolean wielkoscLiter = false;

    public ParametryHasla() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public ParametryHasla setId(Integer id) {
        this.id = id;
        return this;
    }

    public int getMinimalnaDlugosc() {
        return minimalnaDlugosc;
    }

    public ParametryHasla setMinimalnaDlugosc(int minimalnaDlugosc) {
        this.minimalnaDlugosc = minimalnaDlugosc;
        return this;
    }

    public int getLiczbaZnakowSpecjalnych() {
        return liczbaZnakowSpecjalnych;
    }

    public ParametryHasla setLiczbaZnakowSpecjalnych(int liczbaZnakowSpecjalnych) {
        this.liczbaZnakowSpecjalnych = liczbaZnakowSpecjalnych;
        return this;
    }

    public int getLiczbaCyfr() {
        return liczbaCyfr;
    }

    public ParametryHasla setLiczbaCyfr(int liczbaCyfr) {
        this.liczbaCyfr = liczbaCyfr;
        return this;
    }

    public boolean getWielkoscLiter() {
        return wielkoscLiter;
    }

    public ParametryHasla setWielkoscLiter(boolean wielkoscLiter) {
        this.wielkoscLiter = wielkoscLiter;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.minimalnaDlugosc);
        hash = 89 * hash + Objects.hashCode(this.liczbaZnakowSpecjalnych);
        hash = 89 * hash + Objects.hashCode(this.liczbaCyfr);
        hash = 89 * hash + Objects.hashCode(this.wielkoscLiter);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ParametryHasla)) {
            return false;
        }
        ParametryHasla other = (ParametryHasla) object;
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
        return "pl.softmedica.smportal.jpa.ParametryHasla[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("minimalnaDlugosc", this.minimalnaDlugosc)
                .put("liczbaZnakowSpecjalnych", this.liczbaZnakowSpecjalnych)
                .put("liczbaCyfr", this.liczbaCyfr)
                .put("wielkoscLiter", this.wielkoscLiter)
                .build();
    }

    @Override
    public ParametryHasla setJSON(JSONObject json) {
        if (json != null) {
            this.minimalnaDlugosc = Integer.valueOf(Utilities.stringToNull((String) json.get("minimalnaDlugosc")));
            this.liczbaZnakowSpecjalnych = Integer.valueOf(Utilities.stringToNull((String) json.get("liczbaZnakowSpecjalnych")));
            this.liczbaCyfr = Integer.valueOf(Utilities.stringToNull((String) json.get("liczbaCyfr")));
            this.wielkoscLiter = Boolean.valueOf(Utilities.stringToNull((String) json.get("wielkoscLiter")));
        }
        return this;
    }

    //--------------------------------------------------------------------------
    public static boolean isMaleIWielkieLitery(String text) {
        if (text != null) {
            boolean mala = false;
            boolean wielka = false;
            char[] chars = text.toCharArray();
            for (char c : chars) {
                if (!mala) {
                    mala = Character.isLowerCase((int) c);
                }
                if (!wielka) {
                    wielka = Character.isUpperCase((int) c);
                }
                if (mala && wielka) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int liczbaCyfr(String text) {
        int counter = 0;
        if (text != null) {
            char[] chars = text.toCharArray();
            for (char c : chars) {
                if (Character.isDigit(c)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int liczbaZnakowSpecjalnych(String text) {
        int counter = 0;
        if (text != null) {
            char[] chars = text.toCharArray();
            for (char c : chars) {
                for (char s : ZNAKI_SPECJALNE_TABLICA) {
                    if (c == s) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }
}
