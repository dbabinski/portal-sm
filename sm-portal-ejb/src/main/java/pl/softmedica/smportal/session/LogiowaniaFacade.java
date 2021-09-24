/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pl.softmedica.smportal.common.session.AbstractFacade;
import pl.softmedica.smportal.common.utilities.Utilities;
import pl.softmedica.smportal.jpa.Logowania;

/**
 *
 * @author Lucek
 */
@Stateful(name = "LogowaniaFacade")
public class LogiowaniaFacade extends AbstractFacade<Logowania> implements LogowaniaFacadeLocal {

    @PersistenceContext(unitName = "sm-portal-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public LogiowaniaFacade() {
        super(Logowania.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    public void create(Logowania object){
        getEntityManager();
        super.create(object);    
    }
    
    @Override
    public List<Logowania> find(String filtr, Date dataOd, Date dataDo, Integer limit) {
        StringBuilder sql = new StringBuilder("SELECT * FROM logi.logowania");
        boolean where = false;
        boolean paramFiltr = false;
        boolean paramDaty = false;
        if (Utilities.stringToNull(filtr) != null) {
            where = true;
            paramFiltr = true;
            sql.append("\nWHERE")
                    .append("(")
                    .append("\ncast(id AS text)      = lower(trim(both ' ' from regexp_replace(:filtr, '\\s{2,}', ' ', 'g')))")
                    .append("\nOR")
                    .append("\nlower(ip)                LIKE '%' || lower(trim(both ' ' from regexp_replace(:filtr, '\\s{2,}', ' ', 'g'))) || '%'")
                    .append("\nOR")
                    .append("\nlower(uuid_konta)     LIKE '%' || lower(trim(both ' ' from regexp_replace(:filtr, '\\s{2,}', ' ', 'g'))) || '%'")
                    .append("\nOR")
                    .append("\nlower(opis)     LIKE '%' || lower(trim(both ' ' from regexp_replace(:filtr, '\\s{2,}', ' ', 'g'))) || '%'")
                    .append(")");
        }
        if(dataOd != null && dataDo != null){
            paramDaty = true;
            if(!where){
                sql.append("\nWHERE ");
            } else {
                sql.append("\nAND ");
            }
            sql.append("\n(data BETWEEN :data_od AND :data_do)");
        }
        Query query = em.createNativeQuery(sql.toString(), Logowania.class);
        if(paramFiltr){
           query 
                .setParameter("filtr", filtr.trim());
        }
        if(paramDaty){
            query
                .setParameter("data_od", dataOd)
                .setParameter("data_do", dataDo);
        }
        return query.getResultList();
    }
    
    //--------------------------------------------------------------------------
    // Metody prywatne
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // AbstractFacade
    //--------------------------------------------------------------------------

    @Override
    protected EntityManager getEntityManager() {
        em.createNativeQuery("SET application_name = '" + (principal != null ? principal.getName() : "") + (clientIpAdress != null ? "#" + clientIpAdress : "") + "@e_uslugi'").executeUpdate();
        return em;
    }
}
