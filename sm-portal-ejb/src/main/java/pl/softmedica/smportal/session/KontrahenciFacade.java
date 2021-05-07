/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.softmedica.ea.common.session.AbstractFacade;
import pl.softmedica.smportal.jpa.Kontrahenci;

/**
 *
 * @author Lucek
 */
@Stateful(name = "KontrahenciFacade")
public class KontrahenciFacade extends AbstractFacade<Kontrahenci> implements KontrahenciFacadeLocal {

    @PersistenceContext(unitName = "eUslugi-Zarzadzanie-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public KontrahenciFacade() {
        super(Kontrahenci.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    public void create(Kontrahenci object){
        getEntityManager();
        super.create(object);    
    }

    @Override
    public Kontrahenci edit(Kontrahenci object){
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(Kontrahenci object){
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
