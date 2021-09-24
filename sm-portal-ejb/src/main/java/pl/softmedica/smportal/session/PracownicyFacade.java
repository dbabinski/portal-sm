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
import pl.softmedica.smportal.common.session.AbstractFacade;
import pl.softmedica.smportal.jpa.Pracownicy;

/**
 *
 * @author Lucek
 */
@Stateful(name = "PracownicyFacade")
public class PracownicyFacade extends AbstractFacade<Pracownicy> implements PracownicyFacadeLocal {

    @PersistenceContext(unitName = "sm-portal-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public PracownicyFacade() {
        super(Pracownicy.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    public void create(Pracownicy object){
        getEntityManager();
        super.create(object);    
    }

    @Override
    public Pracownicy edit(Pracownicy object){
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(Pracownicy object){
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
