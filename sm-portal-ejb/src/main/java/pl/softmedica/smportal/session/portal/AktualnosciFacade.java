/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session.portal;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.softmedica.ea.common.session.AbstractFacade;
import pl.softmedica.smportal.jpa.portal.Aktualnosci;

/**
 *
 * @author Krzysztof Depka Prądzyński <k.d.pradzynski@softmedica.pl>
 */
@Stateful(name = "AktualnosciFacade")
public class AktualnosciFacade extends AbstractFacade<Aktualnosci> implements AktualnosciFacadeLocal {

    @PersistenceContext(unitName = "sm-portal-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public AktualnosciFacade() {
        super(Aktualnosci.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------        
    @Override
    public List<Aktualnosci> findPublicated() {
        return em.createNativeQuery(
                "SELECT a.* FROM portal.aktualnosci a WHERE a.publikacja AND a.data_publikacji <= current_date ORDER BY a.data_publikacji DESC", Aktualnosci.class)
                .getResultList();
    }

    @Override
    public void create(Aktualnosci object){
        getEntityManager();
        super.create(object);    
    }

    @Override
    public Aktualnosci edit(Aktualnosci object){
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(Aktualnosci object){
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
