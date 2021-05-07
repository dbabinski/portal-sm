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
import pl.softmedica.smportal.jpa.TypyPowiadomien;

/**
 *
 * @author Damian Babiński <damian.babinski@softmedica.pl>
 */
@Stateful(name = "TypyPowiadomienFacade")
public class TypyPowiadomienFacade extends AbstractFacade<TypyPowiadomien> implements TypyPowiadomienFacadeLocal{
    
    @PersistenceContext(unitName = "eUslugi-Zarzadzanie-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public TypyPowiadomienFacade() {
        super(TypyPowiadomien.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    @Override
    public boolean czyNazwaUnikalna(Integer id, String opis) {
        Query query = em.createNativeQuery("SELECT * FROM slowniki.typy_powiadomien "
                + "WHERE opis = :opis"
                + (id != null ? " AND id != :id" : ""))
                .setParameter("opis", opis);
        if (id != null) {
            query.setParameter("id", id);
        }
        return query.getResultList().isEmpty();
    }
    
    @Override
    public boolean czyOpisUnikalny(Integer id, String opis) {
        Query query = em.createNativeQuery("SELECT * FROM slowniki.typy_powiadomien "
                + "WHERE opis = :opis"
                + (id != null ? " AND id != :id" : ""))
                .setParameter("opis", opis);
        if (id != null) {
            query.setParameter("id", id);
        }
        return query.getResultList().isEmpty();
    }
    

    @Override
    public TypyPowiadomien findById(Integer id) {
        return (TypyPowiadomien) em.createNamedQuery("TypyPowiadomien.findById")
                .setParameter("id", id)
                .getSingleResult();
                
    }

    @Override
    public void remove(TypyPowiadomien entity) {
        getEntityManager();
        super.remove(entity); 
    }

    @Override
    public void create(TypyPowiadomien entity) {
        getEntityManager();
        super.create(entity); 
    }
    
    @Override
    public TypyPowiadomien edit(TypyPowiadomien object){
        getEntityManager();
        return super.edit(object);
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
