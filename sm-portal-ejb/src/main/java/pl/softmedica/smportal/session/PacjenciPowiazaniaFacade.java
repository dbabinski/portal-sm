/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import java.util.ArrayList;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pl.softmedica.ea.common.session.AbstractFacade;
import pl.softmedica.smportal.jpa.PacjenciPowiazania;

/**
 *
 * @author Lucek
 */
@Stateful(name = "PacjenciPowiazaniaFacade")
public class PacjenciPowiazaniaFacade extends AbstractFacade<PacjenciPowiazania> implements PacjenciPowiazaniaFacadeLocal {

    @PersistenceContext(unitName = "sm-portal-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public PacjenciPowiazaniaFacade() {
        super(PacjenciPowiazania.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    @Override
    public ArrayList<PacjenciPowiazania> getPowiazaniaKonta(Integer idKonta) {
        String sql = "SELECT * FROM uzytkownicy.pacjenci_powiazania WHERE id_konta = :id_konta";
        Query query = em.createNativeQuery(sql, PacjenciPowiazania.class);
        query.setParameter("id_konta", idKonta);
        return (ArrayList<PacjenciPowiazania>) query.getResultList();
    }

    @Override
    public PacjenciPowiazania findBy(Integer idKonta, Integer idPacjenta) {
        String sql = "SELECT p.* FROM uzytkownicy.pacjenci_powiazania p WHERE id_konta = :id_konta AND id_pacjenta = :id_pacjenta";
        Query query = em.createNativeQuery(sql, PacjenciPowiazania.class)
                .setParameter("id_konta", idKonta)
                .setParameter("id_pacjenta", idPacjenta);
        return (PacjenciPowiazania) query
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public void create(PacjenciPowiazania object) {
        getEntityManager();
        super.create(object);
    }

    @Override
    public PacjenciPowiazania edit(PacjenciPowiazania object) {
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(PacjenciPowiazania object) {
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
