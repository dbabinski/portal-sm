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
 * @author Damian Babiński <damian.babinski@softmedica.pl>
 */
@Entity
@Table(schema = "serwis", name = "konfiguracja_serwera_sms")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KonfiguracjaSerweraSms.findAll", query = "SELECT k FROM KonfiguracjaSerweraSms k"),
    @NamedQuery(name = "KonfiguracjaSerweraSms.findById", query = "SELECT k FROM KonfiguracjaSerweraSms k WHERE k.id = :id")})
public class KonfiguracjaSerweraSms implements Serializable, InterfaceJSON<KonfiguracjaSerweraSms>, InterfaceDatabaseObject {

    private static final long serialVersionUID = 2986307226401247992L;
    public static final String TOKEN_KEY = "67117df1e2ca460c52084ca261aa85e8";
    public static final String EMPTY_PASSWORD = "***";

    @Id
    @SequenceGenerator(name = "serwis.konfiguracja_serwera_sms_id_gen", sequenceName = "serwis.konfiguracja_serwera_sms_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serwis.konfiguracja_serwera_sms_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "sms_api_login")
    private String smsApiLogin;
    @Size(max = 2147483647)
    @Column(name = "sms_api_password")
    private String smsApiPassword;
    @Size(max = 2147483647)
    @Column(name = "serwer_sms_login")
    private String serwerSmsLogin;
    @Size(max = 2147483647)
    @Column(name = "serwer_sms_password")
    private String serwerSmsPassword;

    public KonfiguracjaSerweraSms() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public KonfiguracjaSerweraSms setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getSmsApiLogin() {
        return smsApiLogin;
    }

    public KonfiguracjaSerweraSms setSmsApiLogin(String smsApiLogin) {
        this.smsApiLogin = smsApiLogin;
        return this;
    }

    public String getSmsApiPassword() {
        return smsApiPassword;
    }

    public KonfiguracjaSerweraSms setSmsApiPassword(String smsApiPassword) {
        this.smsApiPassword = smsApiPassword;
        return this;
    }

    public String getSerwerSmsLogin() {
        return serwerSmsLogin;
    }

    public KonfiguracjaSerweraSms setSerwerSmsLogin(String serwerSmsLogin) {
        this.serwerSmsLogin = serwerSmsLogin;
        return this;
    }

    public String getSerwerSmsPassword() {
        return serwerSmsPassword;
    }

    public KonfiguracjaSerweraSms setSerwerSmsPassword(String serwerSmsPassword) {
        this.serwerSmsPassword = serwerSmsPassword;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.serwerSmsLogin);
        hash = 89 * hash + Objects.hashCode(this.serwerSmsPassword);
        hash = 89 * hash + Objects.hashCode(this.smsApiLogin);
        hash = 89 * hash + Objects.hashCode(this.smsApiPassword);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KonfiguracjaSerweraSms)) {
            return false;
        }
        KonfiguracjaSerweraSms other = (KonfiguracjaSerweraSms) object;

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
        return "pl.softmedica.smportal.zarzadzanie.jpa.KonfiguracjaSerweraSms[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        if (isSmsApi()) {
            return new JSONBuilder()
                    .put("id", this.id)
                    .put("smsApiLogin", this.smsApiLogin)
                    .put("smsApiPassword", EMPTY_PASSWORD)
                    .put("serwerSmsLogin", this.serwerSmsLogin)
                    .put("serwerSmsPassword", this.serwerSmsPassword)
                    .build();
        } else if (isSerwerSms()) {
            return new JSONBuilder()
                    .put("id", this.id)
                    .put("smsApiLogin", this.smsApiLogin)
                    .put("smsApiPassword", this.smsApiPassword)
                    .put("serwerSmsLogin", this.serwerSmsLogin)
                    .put("serwerSmsPassword", EMPTY_PASSWORD)
                    .build();
        } else {
            return new JSONBuilder()
                    .put("id", this.id)
                    .put("smsApiLogin", this.smsApiLogin)
                    .put("smsApiPassword", this.smsApiPassword)
                    .put("serwerSmsLogin", this.serwerSmsLogin)
                    .put("serwerSmsPassword", this.serwerSmsPassword)
                    .build();
        }
    }

    @Override
    public KonfiguracjaSerweraSms setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.smsApiLogin = jsone.getString("smsApiLogin");
            this.smsApiPassword = jsone.getString("smsApiPassword");
            this.serwerSmsLogin = jsone.getString("serwerSmsLogin");
            this.serwerSmsPassword = jsone.getString("serwerSmsPassword");
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

    public boolean isSmsApi() {
        if (this.smsApiLogin == null) {
            return false;
        }
        return true;
    }

    public boolean isSerwerSms() {
        if (this.serwerSmsLogin == null) {
            return false;
        }
        return true;
    }

}
