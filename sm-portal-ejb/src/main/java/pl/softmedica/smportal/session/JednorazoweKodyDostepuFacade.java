/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pl.softmedica.ea.common.session.AbstractFacade;
import pl.softmedica.ea.common.utilities.Utilities;
import pl.softmedica.smportal.jpa.JednorazoweKodyDostepu;

/**
 *
 * @author Łukasz Brzeziński <lukasz.brzezinski@softmedica.pl>
 */
@Stateful(name = "JednorazoweKodyDostepuFacade")
public class JednorazoweKodyDostepuFacade extends AbstractFacade<JednorazoweKodyDostepu> implements JednorazoweKodyDostepuFacadeLocal {

    @PersistenceContext(unitName = "eUslugi-Zarzadzanie-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public JednorazoweKodyDostepuFacade() {
        super(JednorazoweKodyDostepu.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    @Override
    public List<JednorazoweKodyDostepu> find(String filtr, Integer limit) {
        StringBuilder sql = new StringBuilder("SELECT * FROM kody.jednorazowe_kody_dostepu");
        if (Utilities.stringToNull(filtr) != null) {
            sql.append("\nWHERE")
                    .append("\nlower(kod)       LIKE '%' || lower(trim(both ' ' from regexp_replace(:filtr, '\\s{2,}', ' ', 'g'))) || '%'")                    
                    .append("\nOR lower(pesel)  LIKE '%' || lower(trim(both ' ' from regexp_replace(:filtr, '\\s{2,}', ' ', 'g'))) || '%'");
        }
        Query query = em.createNativeQuery(sql.toString(), JednorazoweKodyDostepu.class);
        if (filtr != null) {
            query.setParameter("filtr", filtr.trim());
        }
        return query.getResultList();
    }
    
    public void create(JednorazoweKodyDostepu object){
        getEntityManager();
        super.create(object);    
    }

    @Override
    public JednorazoweKodyDostepu edit(JednorazoweKodyDostepu object){
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(JednorazoweKodyDostepu object){
        getEntityManager();
        super.remove(object);
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
