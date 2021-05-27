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
import pl.softmedica.smportal.jpa.TypyGrup;

/**
 *
 * @author Lucek
 */
@Stateful(name = "TypyGrupFacade")
public class TypyGrupFacade extends AbstractFacade<TypyGrup> implements TypyGrupFacadeLocal {

    @PersistenceContext(unitName = "sm-portal-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public TypyGrupFacade() {
        super(TypyGrup.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    @Override
    public boolean czyMogeUsunacTypGrupy(int id) {
        return em.createNativeQuery("SELECT * FROM uzytkownicy.grupy "
                + "WHERE id_typ_grupy = :id")
                .setParameter("id", id)
                .getResultList().isEmpty();
    }

    @Override
    public boolean czyNazwaUnikalna(Integer id, String nazwa) {
        Query query = em.createNativeQuery("SELECT * FROM slowniki.typy_grup "
                + "WHERE nazwa = :nazwa"
                + (id != null ? " AND id != :id" : ""))
                .setParameter("nazwa", nazwa);
        if (id != null) {
            query.setParameter("id", id);
        }
        return query.getResultList().isEmpty();
    }

    @Override
    public void create(TypyGrup object){
        getEntityManager();
        super.create(object);    
    }

    @Override
    public TypyGrup edit(TypyGrup object){
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(TypyGrup object){
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
