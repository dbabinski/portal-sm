/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pl.softmedica.ea.common.session.AbstractFacade;
import pl.softmedica.ea.common.utilities.Utilities;
import pl.softmedica.smportal.jpa.Pacjenci;

/**
 *
 * @author Lucek
 */
@Stateful(name = "PacjenciFacade")
@Local(PacjenciFacadeLocal.class)
public class PacjenciFacade extends AbstractFacade<Pacjenci> implements PacjenciFacadeLocal {

    @PersistenceContext(unitName = "sm-portal-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public PacjenciFacade() {
        super(Pacjenci.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------       
    @Override
    public List<Pacjenci> find(String filtr, Integer limit) {
        StringBuilder sql = new StringBuilder("SELECT * FROM public.pacjenci");
        if (Utilities.stringToNull(filtr) != null) {
            sql.append("\nWHERE")
                    .append("\nlower(imie || ' ' || nazwisko)    LIKE '%' || lower(trim(both ' ' from regexp_replace(:filtr, '\\s{2,}', ' ', 'g'))) || '%'")
                    .append("\nOR lower(nazwisko || ' ' || imie) LIKE '%' || lower(trim(both ' ' from regexp_replace(:filtr, '\\s{2,}', ' ', 'g'))) || '%'")
                    .append("\nOR lower(pesel)                   LIKE '%' || lower(trim(both ' ' from regexp_replace(:filtr, '\\s{2,}', ' ', 'g'))) || '%'")
                    .append("\nOR lower(email)                   LIKE '%' || lower(trim(both ' ' from regexp_replace(:filtr, '\\s{2,}', ' ', 'g'))) || '%'");
        }
        Query query = em.createNativeQuery(sql.toString(), Pacjenci.class);
        if (filtr != null) {
            query.setParameter("filtr", filtr.trim());
        }
        return query.getResultList();
    }

    @Override
    public List<Pacjenci> findByKonto(Integer idKonta) {
        if (idKonta != null) {
            return em.createNativeQuery("SELECT * FROM public.pacjenci WHERE id_konta = :idKonta", Pacjenci.class)
                    .setParameter("idKonta", idKonta)
                    .getResultList();
        }
        return null;
    }

    public List<Pacjenci> findByPesel(String pesel) {
        return findByPesel(pesel, null);
    }

    @Override
    public List<Pacjenci> findByPesel(String pesel, Integer id) {
        if (pesel != null) {
            Query query = em.createNativeQuery(
                    "SELECT * FROM public.pacjenci WHERE pesel = :pesel"
                    + (id != null ? " AND id != :id" : ""), Pacjenci.class)
                    .setParameter("pesel", Utilities.nullToString(pesel));
            if (id != null) {
                query.setParameter("id", id);
            }
            return query.getResultList();
        }
        return new LinkedList<>();
    }

    @Override
    public Pacjenci findByEmail(String email) {
        if (email != null) {
            return (Pacjenci) em.createNativeQuery("SELECT * FROM public.pacjenci WHERE email = :email", Pacjenci.class)
                    .setParameter("email", email)
                    .getResultList().stream().findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public LinkedList<Pacjenci> getPacjenci(Integer idKonta) {
        LinkedList<Pacjenci> lista = new LinkedList<>();
        if (idKonta != null) {
            lista.addAll(
                    em.createNativeQuery("SELECT p.* FROM public.pacjenci p "
                            + "WHERE p.id_konta = :idKonta "
                            + "ORDER BY p.nazwisko, p.imie", Pacjenci.class)
                            .setParameter("idKonta", idKonta)
                            .getResultList());
        }
        return lista;
    }

    public void create(Pacjenci object) {
        getEntityManager();
        super.create(object);
    }

    @Override
    public Pacjenci edit(Pacjenci object) {
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(Pacjenci object) {
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
