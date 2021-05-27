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
import pl.softmedica.smportal.jpa.UstawieniaPacjenta;

/**
 *
 * @author Lucek
 */
@Local
public interface UstawieniaPacjentaFacadeLocal {

    void create(UstawieniaPacjenta object);

    UstawieniaPacjenta edit(UstawieniaPacjenta object);

    void remove(UstawieniaPacjenta object);

    UstawieniaPacjenta find(Object id);

    UstawieniaPacjenta find();

    List<UstawieniaPacjenta> findAll();

    List<UstawieniaPacjenta> findRange(int[] range);

    int count();

    UstawieniaPacjenta createManaged(UstawieniaPacjenta entity);
    
    void setPrincipal(Principal principal); 
    
    void setClientIpAdress(String clientIpAdress);
    
    static UstawieniaPacjentaFacadeLocal create(SecurityContext securityContext, String clientIpAdress) throws NamingException {
        return UstawieniaPacjentaFacadeLocal.create(securityContext != null
                ? securityContext.getUserPrincipal() : (Principal) null, clientIpAdress);
    }

    static UstawieniaPacjentaFacadeLocal create(Principal principal, String clientIpAdress) throws NamingException {
        InitialContext initialContext = new InitialContext();
        UstawieniaPacjentaFacadeLocal bean = (UstawieniaPacjentaFacadeLocal) initialContext
                .lookup("java:app/smportal-ejb/UstawieniaPacjentaFacade");
        bean.setPrincipal(principal);
        bean.setClientIpAdress(clientIpAdress);
        return bean;
    }
}
