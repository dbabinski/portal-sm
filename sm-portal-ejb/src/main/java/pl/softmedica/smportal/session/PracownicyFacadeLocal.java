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
import pl.softmedica.smportal.jpa.Pracownicy;

/**
 *
 * @author Lucek
 */
@Local
public interface PracownicyFacadeLocal {

    void create(Pracownicy object);

    Pracownicy edit(Pracownicy object);

    void remove(Pracownicy object);

    Pracownicy find(Object id);

    List<Pracownicy> findAll();

    List<Pracownicy> findRange(int[] range);

    int count();

    Pracownicy createManaged(Pracownicy entity);

    void setPrincipal(Principal principal);    
    
    void setClientIpAdress(String clientIpAdress);
    
    static PracownicyFacadeLocal create(SecurityContext securityContext, String clientIpAdress) throws NamingException {
        return PracownicyFacadeLocal.create(securityContext != null
                ? securityContext.getUserPrincipal() : (Principal) null, clientIpAdress);
    }

    static PracownicyFacadeLocal create(Principal principal, String clientIpAdress) throws NamingException {
        InitialContext initialContext = new InitialContext();
        PracownicyFacadeLocal bean = (PracownicyFacadeLocal) initialContext
                .lookup("java:app/euslugi-zarzadzanie-ejb/PracownicyFacade");
        bean.setPrincipal(principal);
        bean.setClientIpAdress(clientIpAdress);
        return bean;
    }   
}
