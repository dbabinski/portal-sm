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
import pl.softmedica.smportal.jpa.UprawnieniaKonta;

/**
 *
 * @author Łukasz Brzeziński<lukasz.brzezinski@softmedica.pl>
 */
@Stateful(name = "UprawnieniaKontaFacade")
public class UprawnieniaKontaFacade extends AbstractFacade<UprawnieniaKonta> implements UprawnieniaKontaFacadeLocal {

    @PersistenceContext(unitName = "sm-portal-EJB-PU")
    private EntityManager em;
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public UprawnieniaKontaFacade() {
        super(UprawnieniaKonta.class);
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    @Override
    public UprawnieniaKonta findByIdKonta(Integer idKonta) {
        if (idKonta == null) {
            return null;
        }
        return (UprawnieniaKonta) em.createNativeQuery("SELECT * FROM uzytkownicy.uprawnienia_konta WHERE id_konta = :idKonta", UprawnieniaKonta.class)
                .setParameter("idKonta", idKonta).getResultList()
                .stream().findFirst().orElse(null);
    }

    @Override
    public void create(UprawnieniaKonta object) {
        getEntityManager();
        super.create(object);
    }

    @Override
    public UprawnieniaKonta edit(UprawnieniaKonta object) {
        getEntityManager();
        return super.edit(object);
    }

    @Override
    public void remove(UprawnieniaKonta object) {
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
