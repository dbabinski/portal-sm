/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.utilities.JSONBuilder;


/**
 *
 * @author Lucek
 */
@Entity
@Table(schema = "logi", name = "operacje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Logi.findAll", query = "SELECT l FROM Logi l"),
    //@NamedQuery(name = "Logi.findByDate", query = "SELECT l FROM Logi l WHERE l.data BETWEEN ':data_od' AND ':data_do';"),
})
public class Logi implements Serializable, InterfaceJSON<Logi>,  InterfaceDatabaseObject {

    private static final long serialVersionUID = -4540098272282196698L;
    
    @Id
    @SequenceGenerator(name = "logi.operacje_id_gen", sequenceName = "logi.operacje_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logi.operacje_id_gen")
    @Basic(optional = false)
    @Column(name = "id", insertable = false, updatable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Basic(optional = false)
    @NotNull
    @Column(name = "czas")
    @Temporal(TemporalType.TIME)
    private Date czas;
    @Size(min = 1, max = 2147483647)
    @Column(name = "ip")
    private String ip;
    @Size(min = 1, max = 2147483647)
    @Column(name = "opis")
    private String opis;        
    @Column(name = "id_obiektu")
    private Integer idObiektu;
    @Size(min = 1, max = 2147483647)
    @Column(name = "tabela_obiektu")
    private String tabelaObiektu;
    @Column(name = "id_obiektu_nadrzednego")
    private Integer idObiektuNadrzednego;
    @Size(min = 1, max = 2147483647)
    @Column(name = "tabela_obiektu_nadrzednego")
    private String tabelaObiektuNadrzednego;
    @Size(min = 1, max = 2147483647)
    @Column(name = "pid")
    private String pid;
    @Size(min = 1, max = 2147483647)
    @Column(name = "typ_operacji")
    private String typOperacji;
    @Size(min = 1, max = 2147483647)
    @Column(name = "uuid_konta")
    private String uuidKonta;

    public Integer getId() {
        return id;
    }

    public Logi setId(Integer id) {
        this.id = id;
        return this;
    }

    public Date getData() {
        return data;
    }

    public Logi setData(Date data) {
        this.data = data;
        return this;
    }

    public Date getCzas() {
        return czas;
    }

    public Logi setCzas(Date czas) {
        this.czas = czas;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public Logi setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getOpis() {
        return opis;
    }

    public Logi setOpis(String opis) {
        this.opis = opis;
        return this;
    }

    public Integer getIdObiektu() {
        return idObiektu;
    }

    public Logi setIdObiektu(Integer idObiektu) {
        this.idObiektu = idObiektu;
        return this;
    }

    public String getTabelaObiektu() {
        return tabelaObiektu;
    }

    public Logi setTabelaObiektu(String tabelaObiektu) {
        this.tabelaObiektu = tabelaObiektu;
        return this;
    }

    public Integer getIdObiektuNadrzednego() {
        return idObiektuNadrzednego;
    }

    public Logi setIdObiektuNadrzednego(Integer idObiektuNadrzednego) {
        this.idObiektuNadrzednego = idObiektuNadrzednego;
        return this;
    }

    public String getTabelaObiektuNadrzednego() {
        return tabelaObiektuNadrzednego;
    }

    public Logi setTabelaObiektuNadrzednego(String tabelaObiektuNadrzednego) {
        this.tabelaObiektuNadrzednego = tabelaObiektuNadrzednego;
        return this;
    }

    public String getPid() {
        return pid;
    }

    public Logi setPid(String pid) {
        this.pid = pid;
        return this;
    }

    public String getTypOperacji() {
        return typOperacji;
    }

    public Logi setTypOperacji(String typOperacji) {
        this.typOperacji = typOperacji;
        return this;
    }

    public String getUuidKonta() {
        return uuidKonta;
    }

    public Logi setUuidKonta(String uuidKonta) {
        this.uuidKonta = uuidKonta;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (id != null ? id.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.data);
        hash = 89 * hash + Objects.hashCode(this.czas);
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.opis);
        hash = 89 * hash + Objects.hashCode(this.idObiektu);
        hash = 89 * hash + Objects.hashCode(this.tabelaObiektu);
        hash = 89 * hash + Objects.hashCode(this.idObiektuNadrzednego);
        hash = 89 * hash + Objects.hashCode(this.tabelaObiektuNadrzednego);
        hash = 89 * hash + Objects.hashCode(this.pid);
        hash = 89 * hash + Objects.hashCode(this.typOperacji);
        hash = 89 * hash + Objects.hashCode(this.uuidKonta);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Logi)) {
            return false;
        }
        Logi other = (Logi) object;
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
        return "pl.softmedica.smportal.zarzadzanie.jpa.Logi[ id=" + id + " ]";
    }    

    public static Comparator<Logi> COMPARATOR_BY_ID = (Logi o1, Logi o2) -> {
        if (o1 != null && o2 != null) {
            return o1.getId().compareTo(o2.getId());
        }
        return 0;
    };
    
    @Override
    public JSONObject getJSON() {
        return new JSONBuilder()
                .put("id", this.id)
                .put("data", this.data)
                .put("czas", this.czas)
                .put("ip", this.ip)
                .put("opis", this.opis)
                .put("idObiektu", this.idObiektu)
                .put("tabelaObiektu", this.tabelaObiektu)
                .put("idObiektuNadrzednego", this.idObiektuNadrzednego)
                .put("tabelaObiektuNadrzednego", this.tabelaObiektuNadrzednego)
                .put("pid", this.pid)
                .put("typOperacji", this.typOperacji)
                .put("uuidKonta", this.uuidKonta)
                .build();
    }
    
    @Override
    public Logi setJSON(JSONObject json) {
        return this;
    }    
}
