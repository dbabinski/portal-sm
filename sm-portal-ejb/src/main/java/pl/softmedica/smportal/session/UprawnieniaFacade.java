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
import pl.softmedica.smportal.jpa.Uprawnienia;

/**
 *
 * @author Lucek
 */
@Stateful(name = "UprawnieniaFacade")
public class UprawnieniaFacade extends AbstractFacade<Uprawnienia> implements UprawnieniaFacadeLocal {

    @PersistenceContext(unitName = "sm-portal-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public UprawnieniaFacade() {
        super(Uprawnienia.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    @Override
    public void create(Uprawnienia object){
        getEntityManager();
        super.create(object);    
    }

    @Override
    public Uprawnienia edit(Uprawnienia object){
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(Uprawnienia object){
        getEntityManager();
        super.remove(object);
    }  
    @Override
     public Uprawnienia findByIdGrupy(Integer idGrupy) {
        if (idGrupy == null) {
            return null;
        }
        return (Uprawnienia) em.createNativeQuery("SELECT * FROM uzytkownicy.uprawnienia WHERE id_grupy = :idGrupy", Uprawnienia.class)
                .setParameter("idGrupy", idGrupy).getResultList()
                .stream().findFirst().orElse(null);
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
