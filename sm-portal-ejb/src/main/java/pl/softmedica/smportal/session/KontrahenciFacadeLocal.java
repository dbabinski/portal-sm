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
import pl.softmedica.smportal.jpa.Kontrahenci;

/**
 *
 * @author Lucek
 */
@Local
public interface KontrahenciFacadeLocal {

    void create(Kontrahenci object);

    Kontrahenci edit(Kontrahenci object);

    void remove(Kontrahenci object);

    Kontrahenci find(Object id);

    List<Kontrahenci> findAll();

    List<Kontrahenci> findRange(int[] range);

    int count();

    Kontrahenci createManaged(Kontrahenci entity);

    void setPrincipal(Principal principal);    
    
    void setClientIpAdress(String clientIpAdress);
    
    static KontrahenciFacadeLocal create(SecurityContext securityContext, String clientIpAdress) throws NamingException {
        return KontrahenciFacadeLocal.create(securityContext != null
                ? securityContext.getUserPrincipal() : (Principal) null, clientIpAdress);
    }

    static KontrahenciFacadeLocal create(Principal principal, String clientIpAdress) throws NamingException {
        InitialContext initialContext = new InitialContext();
        KontrahenciFacadeLocal bean = (KontrahenciFacadeLocal) initialContext
                .lookup("java:app/smportal-ejb/KontrahenciFacade");
        bean.setPrincipal(principal);
        bean.setClientIpAdress(clientIpAdress);
        return bean;
    }
}
