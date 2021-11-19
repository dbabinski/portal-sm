/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import com.google.common.io.BaseEncoding;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.JSONObjectExt;
import pl.softmedica.smportal.criptography.SimpleAES;

/**
 *
 * @author Lucek
 */
@Entity
@Table(schema = "serwis", name = "konfiguracja_serwera_poczty")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KonfiguracjaSerweraPoczty.findAll", query = "SELECT k FROM KonfiguracjaSerweraPoczty k"),
    @NamedQuery(name = "KonfiguracjaSerweraPoczty.findById", query = "SELECT k FROM KonfiguracjaSerweraPoczty k WHERE k.id = :id")})
public class KonfiguracjaSerweraPoczty implements Serializable, InterfaceJSON<KonfiguracjaSerweraPoczty>, InterfaceDatabaseObject {

    private static final long serialVersionUID = -4924274549216650108L;
    public static final String TOKEN_KEY = "894e76b96f92516e6f932ebd5f434af8";
    public static final String EMPTY_PASSWORD = "***";

    @Id
    @SequenceGenerator(name = "serwis.konfiguracja_serwera_poczty_id_gen", sequenceName = "serwis.konfiguracja_serwera_poczty_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serwis.konfiguracja_serwera_poczty_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Size(max = 2147483647)
    @Column(name = "uzytkownik")
    private String uzytkownik;
    @Size(max = 2147483647)
    @Column(name = "haslo_serwera_poczty")
    private String hasloSerweraPoczty;
    @Size(max = 2147483647)
    @Column(name = "adres_serwera_poczty")
    private String adresSerweraPoczty;
    @Size(max = 2147483647)
    @Column(name = "port_serwera_poczty")
    private String portSerweraPoczty;
    @Column(name = "ssl")
    private boolean ssl = true;
    @Column(name = "nadawca")
    private String nadawca;
    @Column(name = "email")
    private String email;

    public KonfiguracjaSerweraPoczty() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public KonfiguracjaSerweraPoczty setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getAdresSerweraPoczty() {
        return adresSerweraPoczty;
    }

    public String getUzytkownik() {
        return uzytkownik;
    }

    public KonfiguracjaSerweraPoczty setUzytkownik(String uzytkownik) {
        this.uzytkownik = uzytkownik;
        return this;
    }

    public String getHasloSerweraPoczty() {
        return hasloSerweraPoczty;
    }

    public KonfiguracjaSerweraPoczty setHasloSerweraPoczty(String hasloSerweraPoczty) {
        this.hasloSerweraPoczty = hasloSerweraPoczty;
        return this;
    }

    public KonfiguracjaSerweraPoczty setAdresSerweraPoczty(String adresSerweraPoczty) {
        this.adresSerweraPoczty = adresSerweraPoczty;
        return this;
    }

    public String getPortSerweraPoczty() {
        return portSerweraPoczty;
    }

    public KonfiguracjaSerweraPoczty setPortSerweraPoczty(String portSerweraPoczty) {
        this.portSerweraPoczty = portSerweraPoczty;
        return this;
    }

    public boolean isSSL() {
        return ssl;
    }

    public KonfiguracjaSerweraPoczty setSSL(boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    public String getNadawca() {
        return nadawca;
    }

    public KonfiguracjaSerweraPoczty setNadawca(String nadawca) {
        this.nadawca = nadawca;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public KonfiguracjaSerweraPoczty setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.uzytkownik);
        hash = 89 * hash + Objects.hashCode(this.hasloSerweraPoczty);
        hash = 89 * hash + Objects.hashCode(this.adresSerweraPoczty);
        hash = 89 * hash + Objects.hashCode(this.portSerweraPoczty);
        hash = 89 * hash + Objects.hashCode(this.ssl);
        hash = 89 * hash + Objects.hashCode(this.nadawca);
        hash = 89 * hash + Objects.hashCode(this.email);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KonfiguracjaSerweraPoczty)) {
            return false;
        }
        KonfiguracjaSerweraPoczty other = (KonfiguracjaSerweraPoczty) object;
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
        return "pl.softmedica.smportal.jpa.KonfiguracjaSerweraPoczty[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("uzytkownik", this.uzytkownik)
                .put("hasloSerweraPoczty", EMPTY_PASSWORD) //ze względów bezpieczeństwa
                .put("adresSerweraPoczty", this.adresSerweraPoczty)
                .put("portSerweraPoczty", this.portSerweraPoczty)
                .put("ssl", this.ssl)
                .put("nadawca", this.nadawca)
                .put("email", this.email)
                .build();
    }

    @Override
    public KonfiguracjaSerweraPoczty setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.uzytkownik = jsone.getString("uzytkownik");
            this.hasloSerweraPoczty = jsone.getString("hasloSerweraPoczty");
            this.adresSerweraPoczty = jsone.getString("adresSerweraPoczty");
            this.portSerweraPoczty = jsone.getString("portSerweraPoczty");
            this.ssl = jsone.getBooleanSimple("ssl");
            this.nadawca = jsone.getString("nadawca");
            this.email = jsone.getString("email");
        }
        return this;
    }

    public static String encodePassword(String cleanPassword) throws Exception {
        String encodePassword = "";
        if (cleanPassword != null && !cleanPassword.isEmpty()) {
            byte[] encryptionKey = TOKEN_KEY.getBytes(StandardCharsets.UTF_8);
            StringBuilder toEncode = new StringBuilder();
            for (int i = 0; i < cleanPassword.length(); i++) {
                toEncode
                        .append(cleanPassword.substring(i, i + 1))
                        .append(new Random().nextInt(10));
            }
            byte[] plainText = toEncode.toString().getBytes(StandardCharsets.UTF_8);
            SimpleAES aes = new SimpleAES(encryptionKey);
            encodePassword = BaseEncoding.base32Hex().encode(aes.encrypt(plainText));
        }
        return encodePassword;
    }

    public static String decodePassword(String password) throws Exception {
        String cleanPassword = "";
        if (password != null && !password.isEmpty()) {
            byte[] encryptionKey = TOKEN_KEY.getBytes(StandardCharsets.UTF_8);
            SimpleAES aes = new SimpleAES(encryptionKey);
            byte[] decryptedCipherText = aes.decrypt(BaseEncoding.base32Hex().decode(password));
            String toClean = new String(decryptedCipherText, "UTF-8");
            StringBuilder clean = new StringBuilder();
            for (int i = 0; i < toClean.length(); i += 2) {
                if (i < toClean.length()) {
                    clean.append(toClean.substring(i, i + 1));
                }
            }
            cleanPassword = clean.toString();
        }
        return cleanPassword;
    }
}
