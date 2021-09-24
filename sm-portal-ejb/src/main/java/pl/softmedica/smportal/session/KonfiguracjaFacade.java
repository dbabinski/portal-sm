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
import pl.softmedica.smportal.common.session.AbstractFacade;
import pl.softmedica.smportal.jpa.Konfiguracja;

/**
 *
 * @author Lucek
 */
@Stateful(name = "KonfiguracjaFacade")
public class KonfiguracjaFacade extends AbstractFacade<Konfiguracja> implements KonfiguracjaFacadeLocal {

    @PersistenceContext(unitName = "sm-portal-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public KonfiguracjaFacade() {
        super(Konfiguracja.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------   
    public void create(Konfiguracja object){
        getEntityManager();
        super.create(object);    
    }

    @Override
    public Konfiguracja edit(Konfiguracja object){
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(Konfiguracja object){
        getEntityManager();
        super.remove(object);
    }     
    
    @Override
    public Konfiguracja find() {
        return this.findAll().stream().findFirst().orElse(null);
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
