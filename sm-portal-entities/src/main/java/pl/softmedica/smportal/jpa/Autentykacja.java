/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.Utilities;

/**
 *
 * @author Krzysztof Depka Prądzyński <k.d.pradzynski@softmedica.pl>
 */
@Deprecated
@Entity
@Table(schema = "uzytkownicy", name = "autentykacja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Autentykacja.findAll", query = "SELECT a FROM Autentykacja a"),
    @NamedQuery(name = "Autentykacja.findById", query = "SELECT a FROM Autentykacja a WHERE a.id = :id"),
    @NamedQuery(name = "Autentykacja.findByToken", query = "SELECT a FROM Autentykacja a WHERE a.token = :token"),
    @NamedQuery(name = "Autentykacja.findByTokenWaznyDo", query = "SELECT a FROM Autentykacja a WHERE a.tokenWaznyDo = :tokenWaznyDo"),
    @NamedQuery(name = "Autentykacja.findByIp", query = "SELECT a FROM Autentykacja a WHERE a.ip = :ip"),
    @NamedQuery(name = "Autentykacja.findByLogin", query = "SELECT a FROM Autentykacja a WHERE a.idKonta IS NOT NULL AND a.idKonta.login = :login"),
    @NamedQuery(name = "Autentykacja.findByEmail", query = "SELECT a FROM Autentykacja a WHERE a.idKonta IS NOT NULL AND a.idKonta.email = :email"),
    @NamedQuery(name = "Autentykacja.findByIdKonta", query = "SELECT a FROM Autentykacja a WHERE a.idKonta = :idKonta")})
public class Autentykacja implements Serializable, InterfaceJSON<Autentykacja>, InterfaceDatabaseObject {

    private static final long serialVersionUID = -5812995493697251906L;
    @Id
    @SequenceGenerator(name = "uzytkownicy.autentykacja_id_seq", sequenceName = "uzytkownicy.autentykacja_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uzytkownicy.autentykacja_id_seq")
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "token")
    private String token;
    @Column(name = "token_wazny_do")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenWaznyDo;
    @Size(max = 2147483647)
    @Column(name = "ip")
    private String ip;
    @JoinColumn(name = "id_konta", referencedColumnName = "id")
    @ManyToOne
    private Konta idKonta;

    public Autentykacja() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Autentykacja setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Autentykacja setToken(String token) {
        this.token = token;
        return this;
    }

    public Date getTokenWaznyDo() {
        return tokenWaznyDo;
    }

    public Autentykacja setTokenWaznyDo(Date tokenWaznyDo) {
        this.tokenWaznyDo = tokenWaznyDo;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public Autentykacja setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Konta getIdKonta() {
        return idKonta;
    }

    public Autentykacja setIdKonta(Konta idKonta) {
        this.idKonta = idKonta;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.token);
        hash = 97 * hash + Objects.hashCode(this.tokenWaznyDo);
        hash = 97 * hash + Objects.hashCode(this.ip);
        hash = 97 * hash + Objects.hashCode(this.idKonta);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Konta)) {
            return false;
        }
        Autentykacja other = (Autentykacja) object;
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
        return "pl.softmedica.smportal.jpa.Autentykacja[ id=" + id + " ]";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("idKonta", this.idKonta != null ? this.idKonta.getId() : null)
                .put("token", token)
                .put("tokenWaznyDo", Utilities.dateToDateTimeString(tokenWaznyDo))
                .build();
    }

    @Override
    public Autentykacja setJSON(JSONObject json) {
        //NOP
        return this;
    }

    public String isValid(String token, Date znacznikCzasu) {
        if (token != null && token.equals(this.token)) {
            if (this.tokenWaznyDo == null) {
                return null;
            }
            if (znacznikCzasu != null) {
                if (this.tokenWaznyDo.compareTo(znacznikCzasu) >= 0) {
                    return null;
                } else {
                    return "Sesja wygasła, zaloguj się ponownie";
                }
            } else {
                return null;
            }
        }
        return "Zaloguj się";
    }
    
     /**
     * Ustawia ważność tokenu od momentu wywołania metody przez czas określony w
     * czasTrwaniaSesji. Czas wyrażony jest w minutach.
     *
     * @param czasTrwaniaSesji
     * @return
     */
    public Autentykacja setTokenWaznyDo(Integer czasTrwaniaSesji) {
        if (czasTrwaniaSesji != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, czasTrwaniaSesji);
            this.tokenWaznyDo = calendar.getTime();
        } else {
            this.tokenWaznyDo = null;
        }
        return this;
    }
}
