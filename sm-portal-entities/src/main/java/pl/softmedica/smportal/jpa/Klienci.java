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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.interfaces.InterfaceUUID;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.JSONObjectExt;
import pl.softmedica.smportal.common.utilities.LinkedHashMapBuilder;
import pl.softmedica.smportal.common.utilities.ListBuilder;
import pl.softmedica.smportal.common.utilities.Utilities;

/**
 *
 * @author Lucek
 */
@Entity
@Table(schema = "public", name = "klienci")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Klienci.findAll", query = "SELECT k FROM Klienci k"),
    @NamedQuery(name = "Klienci.findById", query = "SELECT k FROM Klienci k WHERE k.id = :id")})
public class Klienci implements Serializable, InterfaceJSON<Klienci>,InterfaceUUID<Klienci>, InterfaceDatabaseObject{

    private static final long serialVersionUID = 2933959408002029850L;

    @Id
    @SequenceGenerator(name = "public.klienci_id_gen", sequenceName = "public.klienci_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public.klienci_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(mappedBy = "klient")
    private List<KlienciPowiazania> klienciPowiazania = new ArrayList<>();
    @Size(min = 1, max = 2147483647)
    @Column(name = "imie")
    private String imie;
    @Size(min = 1, max = 2147483647)
    @Column(name = "nazwisko")
    private String nazwisko;
    @Size(min = 1, max = 2147483647)
    @Column(name = "nazwa_klienta")
    private String nazwa_klienta;
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nip")
    private String nip;
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nr_licencji")
    private String nr_licencji;
    @Size(max = 2147483647)
    @Column(name = "telefon_kontaktowy")
    private String telefonKontaktowy;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 2147483647)
    @Column(name = "email")
    private String email;
    @Size(max = 2147483647)
    @Column(name = "miejscowosc")
    private String miejscowosc;
    @Size(max = 2147483647)
    @Column(name = "kod_pocztowy")
    private String kodPocztowy;
    @Size(max = 2147483647)
    @Column(name = "ulica")
    private String ulica;
    @Size(max = 2147483647)
    @Column(name = "nr_domu")
    private String nrDomu;
    @Size(max = 2147483647)
    @Column(name = "nr_lokalu")
    private String nrLokalu;
    @Column(name = "uuid")
    private String uuid = UUID.randomUUID().toString().replace("-", "");
    @Basic(optional = false)
    @NotNull
    @Column(name = "potwierdzenie_danych")
    private boolean potwierdzenieDanych = false;
    
    
    public Klienci() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Klienci setId(Integer id) {
        this.id = id;
        return this;
    }

    public List<KlienciPowiazania> getKlienciPowiazania() {
        return klienciPowiazania;
    }

    public Klienci setKlienciPowiazania(List<KlienciPowiazania> klienciPowiazania) {
        this.klienciPowiazania = klienciPowiazania;
        return this;
    }

    public String getImie() {
        return imie;
    }

    public Klienci setImie(String imie) {
        this.imie = imie;
        return this;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public Klienci setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
        return this;
    }

    public String getNazwaKlienta() {
        return nazwa_klienta;
    }

    public Klienci setNazwaKlienta(String nazwa_klienta) {
        this.nazwa_klienta = nazwa_klienta;
        return this;
    }

    public String getNip() {
        return nip;
    }

    public Klienci setNip(String nip) {
        this.nip = nip;
        return this;
    }

    public String getNrLicencji() {
        return nr_licencji;
    }

    public Klienci setNrLicencji(String nr_licencji) {
        this.nr_licencji = nr_licencji;
        return this;
    }

    public String getTelefonKontaktowy() {
        return telefonKontaktowy;
    }

    public Klienci setTelefonKontaktowy(String telefonKontaktowy) {
        this.telefonKontaktowy = telefonKontaktowy;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Klienci setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getMiejscowosc() {
        return miejscowosc;
    }

    public Klienci setMiejscowosc(String miejscowosc) {
        this.miejscowosc = miejscowosc;
        return this;
    }

    public String getKodPocztowy() {
        return kodPocztowy;
    }

    public Klienci setKodPocztowy(String kodPocztowy) {
        this.kodPocztowy = kodPocztowy;
        return this;
    }

    public String getUlica() {
        return ulica;
    }

    public Klienci setUlica(String ulica) {
        this.ulica = ulica;
        return this;
    }

    public String getNrDomu() {
        return nrDomu;
    }

    public Klienci setNrDomu(String nrDomu) {
        this.nrDomu = nrDomu;
        return this;
    }

    public String getNrLokalu() {
        return nrLokalu;
    }

    public Klienci setNrLokalu(String nrLokalu) {
        this.nrLokalu = nrLokalu;
        return this;
    }

    public String getImieNazwisko() {
        return imie + " " + nazwisko;
    }

    public String getNazwiskoImie() {
        return nazwisko + " " + imie;
    }

    public String[] getAddressInParts() {
        String[] address = new String[2];
        address[0] = "";
        if (ulica != null) {
            address[0] += ulica;
        }
        if (nrDomu != null) {
            if (address[0].length() > 0) {
                address[0] += " ";
            }
            address[0] += nrDomu;
        }
        if (nrLokalu != null) {
            address[0] += "/";
            address[0] += nrLokalu;
        }
        address[1] = "";
        if (kodPocztowy != null) {
            address[1] += kodPocztowy;
        }
        address[1] += " ";
        if (miejscowosc != null) {
            address[1] += miejscowosc;
        }
        return address;
    }

    public String getAddress() {
        String address = "";
        if (ulica != null) {
            address += ulica;
        }
        if (nrDomu != null) {
            if (address.length() > 0) {
                address += " ";
            }
            address += nrDomu;
        }
        if (nrLokalu != null) {
            address += "/";
            address += nrLokalu;
        }
        if (kodPocztowy != null) {
            if (address.length() > 0) {
                address += ", ";
            }
            address += kodPocztowy;
        }

        if (miejscowosc != null) {
            if (address.length() > 0) {
                address += " ";
            }
            address += miejscowosc;
        }
        return address;
    }

    public boolean isPotwierdzenieDanych() {
        return potwierdzenieDanych;
    }

    public Klienci setPotwierdzenieDanych(boolean potwierdzenieDanych) {
        this.potwierdzenieDanych = potwierdzenieDanych;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.imie);
        hash = 89 * hash + Objects.hashCode(this.nazwisko);
        hash = 89 * hash + Objects.hashCode(this.telefonKontaktowy);
        hash = 89 * hash + Objects.hashCode(this.nazwa_klienta);
        hash = 89 * hash + Objects.hashCode(this.nip);
        hash = 89 * hash + Objects.hashCode(this.nr_licencji);
        hash = 89 * hash + Objects.hashCode(this.email);
        hash = 89 * hash + Objects.hashCode(this.miejscowosc);
        hash = 89 * hash + Objects.hashCode(this.kodPocztowy);
        hash = 89 * hash + Objects.hashCode(this.ulica);
        hash = 89 * hash + Objects.hashCode(this.nrDomu);
        hash = 89 * hash + Objects.hashCode(this.nrLokalu);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Klienci)) {
            return false;
        }
        Klienci other = (Klienci) object;
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
        return "pl.softmedica.smportal.jpa.klienci[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("imie", this.imie)
                .put("nazwisko", this.nazwisko)
                .put("nazwaKklienta", this.nazwa_klienta)
                .put("nip", this.nip)
                .put("nrLicencji", this.nr_licencji)
                .put("telefonKontaktowy", this.telefonKontaktowy)
                .put("email", this.email)
                .put("miejscowosc", this.miejscowosc)
                .put("kodPocztowy", this.kodPocztowy)
                .put("ulica", this.ulica)
                .put("nrDomu", this.nrDomu)
                .put("nrLokalu", this.nrLokalu)
                .put("adres", this.getAddress())
                .build();
    }

    @Override
    public Klienci setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.imie = jsone.getString("imie");
            this.nazwisko = jsone.getString("nazwisko");
            this.nazwa_klienta = jsone.getString("nazwaKlienta");
            this.nip = jsone.getString("nip");
            this.nr_licencji = jsone.getString("nrLicencji");
            this.telefonKontaktowy = jsone.getString("telefonKontaktowy");
            this.email = jsone.getString("email");
            this.miejscowosc = jsone.getString("miejscowosc");
            this.kodPocztowy = jsone.getString("kodPocztowy");
            this.ulica = jsone.getString("ulica");
            this.nrDomu = jsone.getString("nrDomu");
            this.nrLokalu = jsone.getString("nrLokalu");
        }
        return this;
    }

    public static Comparator<Klienci> COMPARATOR_BY_NAZWISKO_IMIE = (Klienci o1, Klienci o2) -> {
        if (o1 != null && o2 != null) {
            int result = Utilities.nullToString(o1.getNazwisko()).toString()
                    .compareTo(Utilities.nullToString(o2.getNazwisko()).toString());
            return ((result == 0)
                    ? Utilities.nullToString(o1.getImie()).toString().compareTo(Utilities.nullToString(o2.getImie()).toString())
                    : result);
        }
        return 0;
    };

    public static final HashMap<String, String> MAPA_POL = new LinkedHashMapBuilder<String, String>()
            .put("imie", "imię")
            .put("nazwisko", "nazwisko")
            .put("nazwa_klienta", "nazwa klienta")
            .put("nip", "nip")
            .put("nr_licencji", "nr licencji")
            .put("telefon_kontaktowy", "telefon")
            .put("email", "e-mail")
            .put("miejscowosc", "miejscowość")
            .put("kod_pocztowy", "kod pocztowy")
            .put("ulica", "ulica")
            .put("nr_domu", "nr domu")
            .put("nr_lokalu", "nr lokalu")
            .build();

    //--------------------------------------------------------------------------
    // InterfaceUUID
    //--------------------------------------------------------------------------
    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public Klienci setUUID(String sid) {
        this.uuid = sid;
        return this;
    }

    @Override
    public Klienci setUUID() {
        if (uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
        return this;
    }
    
    //--------------------------------------------------------------------------
    // Import
    //--------------------------------------------------------------------------
    
    public static enum kolumnyDoImportu{
        IMIE,
        NAZWISKO,
        PESEL,
        PLEC,
        DATA_URODZENIA,
        MIEJSCE_URODZENIA,
        ULICA,
        NR_DOMU,
        NR_LOKALU,
        MIEJSCOWOSC,
        KOD_POCZTOWY,
        TELEFON_KONTAKTOWY,
        EMAIL,
        TYP_DOKUMENTU_TOZSAMOSCI,
        NUMER_DOKUMENTU_TOZSAMOSCI
    }

    public static final List<kolumnyDoImportu> COLUMN_LIST = new ListBuilder<kolumnyDoImportu>()
            .append(kolumnyDoImportu.IMIE)
            .append(kolumnyDoImportu.NAZWISKO)
            .append(kolumnyDoImportu.PESEL)
            .append(kolumnyDoImportu.PLEC)
            .append(kolumnyDoImportu.DATA_URODZENIA)
            .append(kolumnyDoImportu.MIEJSCE_URODZENIA)
            .append(kolumnyDoImportu.ULICA)
            .append(kolumnyDoImportu.NR_DOMU)
            .append(kolumnyDoImportu.NR_LOKALU)
            .append(kolumnyDoImportu.MIEJSCOWOSC)
            .append(kolumnyDoImportu.KOD_POCZTOWY)
            .append(kolumnyDoImportu.TELEFON_KONTAKTOWY)
            .append(kolumnyDoImportu.EMAIL)
            .append(kolumnyDoImportu.TYP_DOKUMENTU_TOZSAMOSCI)
            .append(kolumnyDoImportu.NUMER_DOKUMENTU_TOZSAMOSCI)
            .build();

    public static final HashMap<kolumnyDoImportu, Boolean> OBLIGATORY_COLUMNS_MAP = new LinkedHashMapBuilder<kolumnyDoImportu, Boolean>()
            .put(kolumnyDoImportu.IMIE, true)
            .put(kolumnyDoImportu.NAZWISKO, true)
            .put(kolumnyDoImportu.PESEL, true)
            .put(kolumnyDoImportu.PLEC, false)
            .put(kolumnyDoImportu.DATA_URODZENIA, false)
            .put(kolumnyDoImportu.MIEJSCE_URODZENIA, false)
            .put(kolumnyDoImportu.ULICA, false)
            .put(kolumnyDoImportu.NR_DOMU, false)
            .put(kolumnyDoImportu.NR_LOKALU, false)
            .put(kolumnyDoImportu.MIEJSCOWOSC, false)
            .put(kolumnyDoImportu.KOD_POCZTOWY, false)
            .put(kolumnyDoImportu.TELEFON_KONTAKTOWY, false)
            .put(kolumnyDoImportu.EMAIL, false)
            .put(kolumnyDoImportu.TYP_DOKUMENTU_TOZSAMOSCI, false)
            .put(kolumnyDoImportu.NUMER_DOKUMENTU_TOZSAMOSCI, false)
            .build();

    public static final HashMap<kolumnyDoImportu, String> EXAMPLE_COLUMN_VALUES = new LinkedHashMapBuilder<kolumnyDoImportu, String>()
            .put(kolumnyDoImportu.IMIE, "Joanna")
            .put(kolumnyDoImportu.NAZWISKO, "Testowa")
            .put(kolumnyDoImportu.PESEL, "84070500002")
            .put(kolumnyDoImportu.PLEC, "k")
            .put(kolumnyDoImportu.DATA_URODZENIA, "1984-07-05")
            .put(kolumnyDoImportu.MIEJSCE_URODZENIA, "Testowo")
            .put(kolumnyDoImportu.ULICA, "Testowa")
            .put(kolumnyDoImportu.NR_DOMU, "1A")
            .put(kolumnyDoImportu.NR_LOKALU, "3")
            .put(kolumnyDoImportu.MIEJSCOWOSC, "Testowo")
            .put(kolumnyDoImportu.KOD_POCZTOWY, "00-001")
            .put(kolumnyDoImportu.TELEFON_KONTAKTOWY, "100200300")
            .put(kolumnyDoImportu.EMAIL, "test@test.pl")
            .put(kolumnyDoImportu.TYP_DOKUMENTU_TOZSAMOSCI, "paszport")
            .put(kolumnyDoImportu.NUMER_DOKUMENTU_TOZSAMOSCI, "00-000-00")
            .build();
}
