/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.softmedica.smportal.common.interfaces.InterfaceDatabaseObject;
import pl.softmedica.smportal.common.session.AbstractFacade;
import pl.softmedica.smportal.jpa.Autentykacja;
import pl.softmedica.smportal.jpa.Konta;

/**
 *
 * @author Krzysztof Depka Prądzyński <k.d.pradzynski@softmedica.pl>
 */
@Stateless
@Deprecated
public class AutentykacjaFacade extends AbstractFacade<Autentykacja> implements AutentykacjaFacadeLocal {

    @PersistenceContext(unitName = "sm-portal-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public AutentykacjaFacade() {
        super(Autentykacja.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    @Override
    public Autentykacja find(Konta idKonta) {
        if (idKonta == null) {
            return null;
        }
        return em
                .createNamedQuery("Autentykacja.findByIdKonta", Autentykacja.class)
                .setParameter("idKonta", idKonta)
                .getResultList().stream()
                .sorted(InterfaceDatabaseObject.COMPARATOR_BY_ID.reversed())
                .findFirst().orElse(null);
    }

    @Override
    public Autentykacja findByLogin(String login) {
        if (login == null) {
            return null;
        }
        return em
                .createNamedQuery("Autentykacja.findByLogin", Autentykacja.class)
                .setParameter("login", login)
                .getResultList().stream()
                .sorted(InterfaceDatabaseObject.COMPARATOR_BY_ID.reversed())
                .findFirst().orElse(null);
    }

    @Override
    public Autentykacja findByEmail(String email) {
        if (email == null) {
            return null;
        }
        return em
                .createNamedQuery("Autentykacja.findByEmail", Autentykacja.class)
                .setParameter("email", email)
                .getResultList().stream()
                .sorted(InterfaceDatabaseObject.COMPARATOR_BY_ID.reversed())
                .findFirst().orElse(null);
    }

    @Override
    public Autentykacja findByToken(String token) {
        if (token == null) {
            return null;
        }
        return em.createNamedQuery("Autentykacja.findByToken", Autentykacja.class)
                .setParameter("token", token)
                .getResultList().stream()
                .sorted(InterfaceDatabaseObject.COMPARATOR_BY_ID.reversed())
                .findFirst().orElse(null);
    }

    //--------------------------------------------------------------------------
    // Metody prywatne
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // AbstractFacade
    //--------------------------------------------------------------------------
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
