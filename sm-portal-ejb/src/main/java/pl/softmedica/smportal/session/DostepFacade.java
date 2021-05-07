/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.softmedica.ea.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.ea.common.session.AbstractFacade;
import pl.softmedica.smportal.jpa.Dostep;

/**
 *
 * @author Lucek
 */
@Stateful(name = "DostepFacade")
public class DostepFacade extends AbstractFacade<Dostep> implements DostepFacadeLocal {

    @PersistenceContext(unitName = "eUslugi-Zarzadzanie-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public DostepFacade() {
        super(Dostep.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------   
    public Dostep find() {
        Dostep d = findAll().stream()
                .sorted(InterfaceDatabaseObject.COMPARATOR_BY_ID.reversed())
                .findFirst().orElse(null);
        
        return d;
    }
    
    public void create(Dostep object){
        getEntityManager();
        super.create(object);    
    }

    @Override
    public Dostep edit(Dostep object){
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(Dostep object){
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
