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
 * @author Lucek
 */
@Entity
@Table(schema = "serwis", name = "konfiguracja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Konfiguracja.findAll", query = "SELECT k FROM Konfiguracja k"),
    @NamedQuery(name = "Konfiguracja.findById", query = "SELECT k FROM Konfiguracja k WHERE k.id = :id")})
public class Konfiguracja implements Serializable, InterfaceJSON<Konfiguracja>, InterfaceDatabaseObject {

    private static final long serialVersionUID = 3507823420795972484L;

    @Id
    @SequenceGenerator(name = "serwis.konfiguracja_id_gen", sequenceName = "serwis.konfiguracja_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serwis.konfiguracja_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "logowanie")
    private boolean logowanie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aktualnosci_na_stronie_glownej")
    private boolean aktualnosciNaStronieGlownej;
    @Size(max = 2147483647)
    @Column(name = "recaptcha_site_key")
    private String recaptchaSiteKey;
    @Size(max = 2147483647)
    @Column(name = "recaptcha_secret_key")
    private String recaptchaSecretKey;
    @Size(max = 2147483647)
    @Column(name = "szablon_email_potwierdzenie_zalozenia_konta")
    private String szablonEmailPotwierdzenieZalozeniaKonta;
    @Size(max = 2147483647)
    @Column(name = "szablon_email_zmiana_hasla")
    private String szablonEmailZmianaHasla;
    @Size(max = 2147483647)
    @Column(name = "szablon_email_powiadomienie_o_wykorzystaniu_danych_pesel")
    private String szablonEmailPowiadomienieOWykorzystaniuDanychPESEL;
    @Size(max = 2147483647)
    @Column(name = "szablon_email_powiadomienie_o_wykorzystaniu_danych_email")
    private String szablonEmailPowiadomienieOWykorzystaniuDanychEmail;
    @Size(max = 2147483647)
    @Column(name = "szablon_email_powiadomienie_o_zmianie_hasla")
    private String szablonEmailPowiadomienieOZmianieHasla;
    @Size(max = 2147483647)
    @Column(name = "url_serwera")
    private String urlSerwera;
    @Size(max = 2147483647)
    @Column(name = "nazwa_serwisu")
    private String nazwaSerwisu;
    @Column(name = "email_administratora")
    private String emailAdministratora;
    @Column(name = "logo")
    private String logo;
    @Size(max = 2147483647)
    @Column(name = "szablon_email_powiadomienie_o_zablokowaniu_konta")
    private String szablonEmailPowiadomienieOZablokowaniuKonta;
    @Column(name = "domena")
    private String domena = null;

    public Konfiguracja() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Konfiguracja setId(Integer id) {
        this.id = id;
        return this;
    }

    public boolean getLogowanie() {
        return logowanie;
    }

    public Konfiguracja setLogowanie(boolean logowanie) {
        this.logowanie = logowanie;
        return this;
    }

    public boolean getAktualnosciNaStronieGlownej() {
        return aktualnosciNaStronieGlownej;
    }

    public Konfiguracja setAktualnosciNaStronieGlownej(boolean aktualnosciNaStronieGlownej) {
        this.aktualnosciNaStronieGlownej = aktualnosciNaStronieGlownej;
        return this;
    }

    public String getRecaptchaSiteKey() {
        return recaptchaSiteKey;
    }

    public Konfiguracja setRecaptchaSiteKey(String recaptchaSiteKey) {
        this.recaptchaSiteKey = recaptchaSiteKey;
        return this;
    }

    public String getRecaptchaSecretKey() {
        return recaptchaSecretKey;
    }

    public Konfiguracja setRecaptchaSecretKey(String recaptchaSecretKey) {
        this.recaptchaSecretKey = recaptchaSecretKey;
        return this;
    }

    public String getSzablonEmailPotwierdzenieZalozeniaKonta() {
        return szablonEmailPotwierdzenieZalozeniaKonta;
    }

    public Konfiguracja setSzablonEmailPotwierdzenieZalozeniaKonta(String szablonEmailPotwierdzenieZalozeniaKonta) {
        this.szablonEmailPotwierdzenieZalozeniaKonta = szablonEmailPotwierdzenieZalozeniaKonta;
        return this;
    }

    public String getSzablonEmailZmianaHasla() {
        return szablonEmailZmianaHasla;
    }

    public Konfiguracja setSzablonEmailZmianaHasla(String szablonEmailZmianaHasla) {
        this.szablonEmailZmianaHasla = szablonEmailZmianaHasla;
        return this;
    }

    public String getSzablonEmailPowiadomienieOWykorzystaniuDanychPESEL() {
        return szablonEmailPowiadomienieOWykorzystaniuDanychPESEL;
    }

    public Konfiguracja setSzablonEmailPowiadomienieOWykorzystaniuDanychPESEL(String szablonEmailPowiadomienieOWykorzystaniuDanychPESEL) {
        this.szablonEmailPowiadomienieOWykorzystaniuDanychPESEL = szablonEmailPowiadomienieOWykorzystaniuDanychPESEL;
        return this;
    }

    public String getSzablonEmailPowiadomienieOWykorzystaniuDanychEmail() {
        return szablonEmailPowiadomienieOWykorzystaniuDanychEmail;
    }

    public Konfiguracja setSzablonEmailPowiadomienieOWykorzystaniuDanychEmail(String szablonEmailPowiadomienieOWykorzystaniuDanychEmail) {
        this.szablonEmailPowiadomienieOWykorzystaniuDanychEmail = szablonEmailPowiadomienieOWykorzystaniuDanychEmail;
        return this;
    }

    public String getSzablonEmailPowiadomienieOZmianieHasla() {
        return szablonEmailPowiadomienieOZmianieHasla;
    }

    public Konfiguracja setSzablonEmailPowiadomienieOZmianieHasla(String szablonEmailPowiadomienieOZmianieHasla) {
        this.szablonEmailPowiadomienieOZmianieHasla = szablonEmailPowiadomienieOZmianieHasla;
        return this;
    }

    public String getUrlSerwera() {
        return urlSerwera;
    }

    public Konfiguracja setUrlSerwera(String urlSerwera) {
        this.urlSerwera = urlSerwera;
        return this;
    }

    public String getNazwaSerwisu() {
        return nazwaSerwisu;
    }

    public Konfiguracja setNazwaSerwisu(String nazwaSerwisu) {
        this.nazwaSerwisu = nazwaSerwisu;
        return this;
    }

    public String getEmailAdministratora() {
        return emailAdministratora;
    }

    public Konfiguracja setEmailAdministratora(String emailAdministratora) {
        this.emailAdministratora = emailAdministratora;
        return this;
    }

    public String getLogo() {
        return logo;
    }

    public Konfiguracja setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public String getSzablonEmailPowiadomienieOZablokowaniuKonta() {
        return szablonEmailPowiadomienieOZablokowaniuKonta;
    }

    public Konfiguracja setSzablonEmailPowiadomienieOZablokowaniuKonta(String szablonEmailPowiadomienieOZablokowaniuKonta) {
        this.szablonEmailPowiadomienieOZablokowaniuKonta = szablonEmailPowiadomienieOZablokowaniuKonta;
        return this;
    }

    public String getDomena() {
        return domena;
    }

    public Konfiguracja setDomena(String domena) {
        this.domena = domena;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.logowanie);
        hash = 89 * hash + Objects.hashCode(this.aktualnosciNaStronieGlownej);
        hash = 89 * hash + Objects.hashCode(this.szablonEmailPotwierdzenieZalozeniaKonta);
        hash = 89 * hash + Objects.hashCode(this.szablonEmailZmianaHasla);
        hash = 89 * hash + Objects.hashCode(this.szablonEmailPowiadomienieOWykorzystaniuDanychPESEL);
        hash = 89 * hash + Objects.hashCode(this.szablonEmailPowiadomienieOWykorzystaniuDanychEmail);
        hash = 89 * hash + Objects.hashCode(this.szablonEmailPowiadomienieOZmianieHasla);
        hash = 89 * hash + Objects.hashCode(this.szablonEmailPowiadomienieOZablokowaniuKonta);
        hash = 89 * hash + Objects.hashCode(this.urlSerwera);
        hash = 89 * hash + Objects.hashCode(this.nazwaSerwisu);
        hash = 89 * hash + Objects.hashCode(this.emailAdministratora);
        hash = 89 * hash + Objects.hashCode(this.domena);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Konfiguracja)) {
            return false;
        }
        Konfiguracja other = (Konfiguracja) object;
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
        return "pl.softmedica.smportal.jpa.Konfiguracja[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("logowanie", this.logowanie)
                .put("aktualnosciNaStronieGlownej", this.aktualnosciNaStronieGlownej)
                .put("recaptchaSiteKey", this.recaptchaSiteKey)
                .put("recaptchaSecretKey", this.recaptchaSecretKey)
                .put("szablonEmailPotwierdzenieZalozeniaKonta", this.szablonEmailPotwierdzenieZalozeniaKonta)
                .put("szablonEmailZmianaHasla", this.szablonEmailZmianaHasla)
                .put("szablonEmailPowiadomienieOWykorzystaniuDanychPESEL", this.szablonEmailPowiadomienieOWykorzystaniuDanychPESEL)
                .put("szablonEmailPowiadomienieOWykorzystaniuDanychEmail", this.szablonEmailPowiadomienieOWykorzystaniuDanychEmail)
                .put("szablonEmailPowiadomienieOZmianieHasla", this.szablonEmailPowiadomienieOZmianieHasla)
                .put("szablonEmailPowiadomienieOZablokowaniuKonta", this.szablonEmailPowiadomienieOZablokowaniuKonta)
                .put("urlSerwera", this.urlSerwera)
                .put("nazwaSerwisu", this.nazwaSerwisu)
                .put("emailAdministratora", this.emailAdministratora)
                .put("domena", this.domena)
                .build();
    }

    @Override
    public Konfiguracja setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.logowanie = jsone.getBooleanSimple("logowanie");
            this.aktualnosciNaStronieGlownej = jsone.getBooleanSimple("aktualnosciNaStronieGlownej");
            this.recaptchaSiteKey = jsone.getString("recaptchaSiteKey");
            this.recaptchaSecretKey = jsone.getString("recaptchaSecretKey");
            this.szablonEmailPotwierdzenieZalozeniaKonta = jsone.getString("szablonEmailPotwierdzenieZalozeniaKonta");
            this.szablonEmailZmianaHasla = jsone.getString("szablonEmailZmianaHasla");
            this.szablonEmailPowiadomienieOWykorzystaniuDanychPESEL = jsone.getString("szablonEmailPowiadomienieOWykorzystaniuDanychPESEL");
            this.szablonEmailPowiadomienieOWykorzystaniuDanychEmail = jsone.getString("szablonEmailPowiadomienieOWykorzystaniuDanychEmail");
            this.szablonEmailPowiadomienieOZmianieHasla = jsone.getString("szablonEmailPowiadomienieOZmianieHasla");
            this.szablonEmailPowiadomienieOZablokowaniuKonta = jsone.getString("szablonEmailPowiadomienieOZablokowaniuKonta");
            this.urlSerwera = jsone.getString("urlSerwera");
            this.nazwaSerwisu = jsone.getString("nazwaSerwisu");
            this.emailAdministratora = jsone.getString("emailAdministratora");
        }
        return this;
    }
}
