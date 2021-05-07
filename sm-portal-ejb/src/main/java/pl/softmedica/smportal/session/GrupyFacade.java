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
import javax.persistence.Query;
import pl.softmedica.ea.common.session.AbstractFacade;
import pl.softmedica.smportal.jpa.Grupy;

/**
 *
 * @author Lucek
 */
@Stateful(name = "GrupyFacade")
public class GrupyFacade extends AbstractFacade<Grupy> implements GrupyFacadeLocal {

    @PersistenceContext(unitName = "eUslugi-Zarzadzanie-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public GrupyFacade() {
        super(Grupy.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    @Override
    public boolean czyOpisUnikalny(Integer id, String opis) {
        Query query = em.createNativeQuery("SELECT * FROM uzytkownicy.grupy "
                + "WHERE opis = :opis"
                + (id != null ? " AND id != :id" : ""))
                .setParameter("opis", opis);
        if (id != null) {
            query.setParameter("id", id);
        }
        return query.getResultList().isEmpty();
    }

    public void create(Grupy object){
        getEntityManager();
        super.create(object);    
    }

    @Override
    public Grupy edit(Grupy object){
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(Grupy object){
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
