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
import pl.softmedica.smportal.jpa.UstawieniaPacjenta;

/**
 *
 * @author Lucek
 */
@Stateful(name = "UstawieniaPacjentaFacade")
public class UstawieniaPacjentaFacade extends AbstractFacade<UstawieniaPacjenta> implements UstawieniaPacjentaFacadeLocal {

    @PersistenceContext(unitName = "eUslugi-Zarzadzanie-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public UstawieniaPacjentaFacade() {
        super(UstawieniaPacjenta.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------   
    @Override
    public UstawieniaPacjenta find() {
        return this.findAll().stream().findFirst().orElse(null);
    }

    @Override
    public void create(UstawieniaPacjenta object){
        getEntityManager();
        super.create(object);    
    }

    @Override
    public UstawieniaPacjenta edit(UstawieniaPacjenta object){
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(UstawieniaPacjenta object){
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
