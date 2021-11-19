/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.interfaces.InterfaceUUID;
import pl.softmedica.smportal.common.utilities.BCrypt;
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
@Table(schema = "uzytkownicy", name = "konta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Konta.findAll", query = "SELECT k FROM Konta k"),
    @NamedQuery(name = "Konta.findById", query = "SELECT k FROM Konta k WHERE k.id = :id"),
    @NamedQuery(name = "Konta.findByLogin", query = "SELECT k FROM Konta k WHERE k.login = :login"),
    @NamedQuery(name = "Konta.findByEmail", query = "SELECT k FROM Konta k WHERE k.email = :email")})
public class Konta implements Serializable, InterfaceJSON<Konta>, InterfaceUUID<Konta>, InterfaceDatabaseObject {

    private static final long serialVersionUID = 5617677834356507782L;

    private static final String SALT = "pl.softmedica.smportal.zarzadzanie.konta.";

    @Id
    @SequenceGenerator(name = "uzytkownicy.konta_id_gen", sequenceName = "uzytkownicy.konta_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uzytkownicy.konta_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(mappedBy = "konto")
    private List<KlienciPowiazania> klienciPowiazania = new ArrayList<>();
    @Size(max = 2147483647)
    @Column(name = "login")
    private String login;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 2147483647)
    @Column(name = "email")
    private String email = null;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "haslo")
    private String haslo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "konto_aktywne")
    private boolean kontoAktywne = false;
    @Column(name = "akceptacja_regulaminu")
    @Temporal(TemporalType.TIMESTAMP)
    private Date akceptacjaRegulaminu = null;
    @Column(name = "blokada_konta_do")
    @Temporal(TemporalType.TIMESTAMP)
    private Date blokadaKontaDo = null;
    @Basic(optional = false)
    @NotNull
    @Column(name = "blokada_konta")
    private boolean blokadaKonta = false;
    /*
    @Basic(optional = false)
    @NotNull
    @Column(name = "potwierdzenie")
    private boolean potwierdzenie = false;
     */
    @JoinColumn(name = "id_grupy", referencedColumnName = "id")
    @ManyToOne
    private Grupy grupa = null;
    @Column(name = "uuid")
    private String uuid = UUID.randomUUID().toString().replace("-", "");
    @Transient
    private Klienci pacjentTransient = null;
    @Basic(optional = false)
    @NotNull
    @Column(name = "liczba_prob_logowania")
    private int liczbaProbLogowania = 0;
    
    @OneToMany (mappedBy = "konto")
    private List<KontaGrupy> kontaGrupy = new ArrayList<>();
    
   
    public Konta() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Konta setId(Integer id) {
        this.id = id;
        return this;
    }

    public List<KlienciPowiazania> getKlienciPowiazania() {
        return klienciPowiazania;
    }

    public Konta setKlienciPowiazania(List<KlienciPowiazania> klienciPowiazania) {
        this.klienciPowiazania = klienciPowiazania;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Konta setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Konta setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getHaslo() {
        return haslo;
    }

    public Konta setHaslo(String haslo) {
        this.haslo = haslo;
        return this;
    }

    public boolean isKontoAktywne() {
        return kontoAktywne;
    }

    public Konta setKontoAktywne(boolean kontoAktywne) {
        this.kontoAktywne = kontoAktywne;
        return this;
    }

    public Date getAkceptacjaRegulaminu() {
        return akceptacjaRegulaminu;
    }

    public Konta setAkceptacjaRegulaminu(Date akceptacjaRegulaminu) {
        this.akceptacjaRegulaminu = akceptacjaRegulaminu;
        return this;
    }

    public Date getBlokadaKontaDo() {
        return blokadaKontaDo;
    }

    public Konta setBlokadaKontaDo(Date blokadaKontaDo) {
        this.blokadaKontaDo = blokadaKontaDo;
        return this;
    }

    public boolean isBlokadaKonta() {
        return blokadaKonta;
    }

    public Konta setBlokadaKonta(boolean blokadaKonta) {
        this.blokadaKonta = blokadaKonta;
        return this;
    }

    /*
    public boolean getPotwierdzenie() {
        return potwierdzenie;
    }

    public Konta setPotwierdzenie(boolean potwierdzenie) {
        this.potwierdzenie = potwierdzenie;
        return this;
    }
     */
    public Grupy getIdGrupy() {
        return grupa;
    }

    public Konta setIdGrupy(Grupy grupa) {
        this.grupa = grupa;
        return this;
    }

    public Konta setHaslo() {
        if (this.id == null && this.haslo == null) {
            this.haslo = BCrypt.hashpw("test", BCrypt.gensalt(12));
        }
        return this;
    }

    public int getLiczbaProbLogowania() {
        return liczbaProbLogowania;
    }

    public Konta setLiczbaProbLogowania(int liczbaProbLogowania) {
        this.liczbaProbLogowania = liczbaProbLogowania;
        return this;
    }

    public Konta dodajLiczbaProbLogowania() {
        this.liczbaProbLogowania++;
        return this;
    }

    public Konta resetLiczbaProbLogowania() {
        this.liczbaProbLogowania = 0;
        return this;
    }

    public Klienci getPacjentTransient() {
        return pacjentTransient;
    }

    public Konta setPacjentTransient(Klienci pacjentTransient) {
        this.pacjentTransient = pacjentTransient;
        return this;
    }
    
    public List<KontaGrupy> getKontaGrupy() {
        return kontaGrupy;
    }
    
    public Konta setKontaGrupy(List<KontaGrupy> kontaGrupy) {
        this.kontaGrupy = kontaGrupy;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.klienciPowiazania);
        hash = 89 * hash + Objects.hashCode(this.grupa);
        hash = 89 * hash + Objects.hashCode(this.login);
        hash = 89 * hash + Objects.hashCode(this.email);
        hash = 89 * hash + Objects.hashCode(this.haslo);
        hash = 89 * hash + Objects.hashCode(this.kontoAktywne);
        hash = 89 * hash + Objects.hashCode(this.akceptacjaRegulaminu);
        hash = 89 * hash + Objects.hashCode(this.blokadaKontaDo);
        hash = 89 * hash + Objects.hashCode(this.blokadaKonta);
        //hash = 89 * hash + Objects.hashCode(this.potwierdzenie);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Konta)) {
            return false;
        }
        Konta other = (Konta) object;
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
        return "pl.softmedica.smportal.jpa.Konta[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("uuid", uuid)
                .put("idGrupa", this.grupa != null ? this.grupa.getId() : null)
                .put("grupa", this.grupa != null ? this.grupa.getJSON() : null)
                .put("grupaOpis", this.grupa != null ? this.grupa.getOpis() : null)
                .put("login", this.login)
                .put("email", this.email)
                //                .put("haslo", this.haslo)
                .put("kontoAktywne", this.kontoAktywne)
                .put("akceptacjaRegulaminu", this.akceptacjaRegulaminu != null
                        ? Utilities.dateToString(this.akceptacjaRegulaminu, Utilities.DATE_TIME_SEC_FORMAT) : null)
                .put("blokadaKontaDo", this.blokadaKontaDo != null
                        ? Utilities.dateToString(this.blokadaKontaDo, Utilities.DATE_TIME_SEC_FORMAT) : null)
                .put("blokadaKonta", this.blokadaKonta)
                .put("isKontrahent", (this.grupa != null && this.grupa.getOpis().equals(Grupy.GRUPA_KONTRAHENCI_OPIS)))
                //.put("potwierdzenie", this.potwierdzenie)
                .build();
    }

    @Override
    public Konta setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.login = jsone.getString("login");
            this.email = jsone.getString("email");
            /*
            String haslo = jsone.getString("haslo");
            if (this.haslo == null && haslo == null) {
                try {
                    this.haslo = MD5Password.getEncodedPassword("test", SALT + this.login);
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
                }
            }
             */
            this.kontoAktywne = jsone.getBooleanSimple("kontoAktywne");
            this.akceptacjaRegulaminu = jsone.getDateTimeSec("akceptacjaRegulaminu");
            this.blokadaKonta = jsone.getBooleanSimple("blokadaKonta");
            this.blokadaKontaDo = blokadaKonta ? jsone.getDateTimeSec("blokadaKontaDo") : null;
            //this.potwierdzenie = jsone.getBooleanSimple("potwierdzenie");
        }
        return this;
    }

    public static final JSONArray POLA_WYMAGANE = new JSONArrayBuilder()
            //.add("login")
            .add("email")
            .build();

    public static final HashMap<String, String> MAPA_POL = new LinkedHashMapBuilder<String, String>()
            .put("grupa", "Grupa")
            .put("login", "Login")
            .put("email", "E-mail")
            .put("haslo", "Hasło")
            .put("kontoAktywne", "Konto aktywne")
            .put("akceptacjaRegulaminu", "Akceptacja regulaminu")
            .put("blokadaKonta", "Blokada konta")
            .put("blokadaKontaDo", "Blokada konta do")
            .put("potwierdzenie", "Konto potwierdzone")
            .build();

    public static Comparator<Konta> COMPARATOR_BY_LOGIN = (Konta o1, Konta o2) -> {
        if (o1 != null && o2 != null) {
            return Utilities.nullToString(o1.getLogin()).toString()
                    .compareTo(Utilities.nullToString(o2.getLogin()).toString());
        }
        return 0;
    };

    public static Comparator<Konta> COMPARATOR_BY_ID = (Konta o1, Konta o2) -> {
        if (o1 != null && o2 != null) {
            return Utilities.nullToString(o1.getId()).toString()
                    .compareTo(Utilities.nullToString(o2.getId()).toString());
        }
        return 0;
    };

    //--------------------------------------------------------------------------
    // InterfaceUUID
    //--------------------------------------------------------------------------
    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public Konta setUUID(String sid) {
        this.uuid = sid;
        return this;
    }

    @Override
    public Konta setUUID() {
        if (uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
        return this;
    }
}
