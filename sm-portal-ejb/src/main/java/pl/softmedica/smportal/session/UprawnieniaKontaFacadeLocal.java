/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import java.security.Principal;
import java.util.List;
import javax.ejb.Local;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.SecurityContext;
import pl.softmedica.smportal.jpa.UprawnieniaKonta;

/**
 *
 * @author Łukasz Brzeziński<lukasz.brzezinski@softmedica.pl>
 */
@Local
public interface UprawnieniaKontaFacadeLocal {

    void create(UprawnieniaKonta object);

    UprawnieniaKonta edit(UprawnieniaKonta object);

    void remove(UprawnieniaKonta object);

    UprawnieniaKonta find(Object id);

    List<UprawnieniaKonta> findAll();

    List<UprawnieniaKonta> findRange(int[] range);

    UprawnieniaKonta findByIdKonta(Integer idKonta);

    int count();

    UprawnieniaKonta createManaged(UprawnieniaKonta entity);

    void setPrincipal(Principal principal);

    void setClientIpAdress(String clientIpAdress);

    static UprawnieniaKontaFacadeLocal create(SecurityContext securityContext, String clientIpAdress) throws NamingException {
        return UprawnieniaKontaFacadeLocal.create(securityContext != null
                ? securityContext.getUserPrincipal() : (Principal) null, clientIpAdress);
    }

    static UprawnieniaKontaFacadeLocal create(Principal principal, String clientIpAdress) throws NamingException {
        InitialContext initialContext = new InitialContext();
        UprawnieniaKontaFacadeLocal bean = (UprawnieniaKontaFacadeLocal) initialContext
                .lookup("java:app/euslugi-zarzadzanie-ejb/UprawnieniaKontaFacade");
        bean.setPrincipal(principal);
        bean.setClientIpAdress(clientIpAdress);
        return bean;
    }
}
