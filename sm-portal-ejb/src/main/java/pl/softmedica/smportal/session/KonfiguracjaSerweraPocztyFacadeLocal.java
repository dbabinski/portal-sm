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
import pl.softmedica.smportal.jpa.KonfiguracjaSerweraPoczty;

/**
 *
 * @author Lucek
 */
@Local
public interface KonfiguracjaSerweraPocztyFacadeLocal {

    void create(KonfiguracjaSerweraPoczty object);

    KonfiguracjaSerweraPoczty edit(KonfiguracjaSerweraPoczty object, String password);

    void remove(KonfiguracjaSerweraPoczty object);

    KonfiguracjaSerweraPoczty find(Object id);

    List<KonfiguracjaSerweraPoczty> findAll();

    int count();

    KonfiguracjaSerweraPoczty createManaged(KonfiguracjaSerweraPoczty entity);

    KonfiguracjaSerweraPoczty find();
    
    boolean isEncoded(KonfiguracjaSerweraPoczty object);
    
    void setPrincipal(Principal principal);    
    
    void setClientIpAdress(String clientIpAdress);
    
    static KonfiguracjaSerweraPocztyFacadeLocal create(SecurityContext securityContext, String clientIpAdress) throws NamingException {
        return KonfiguracjaSerweraPocztyFacadeLocal.create(securityContext != null
                ? securityContext.getUserPrincipal() : (Principal) null, clientIpAdress);
    }

    static KonfiguracjaSerweraPocztyFacadeLocal create(Principal principal, String clientIpAdress) throws NamingException {
        InitialContext initialContext = new InitialContext();
        KonfiguracjaSerweraPocztyFacadeLocal bean = (KonfiguracjaSerweraPocztyFacadeLocal) initialContext
                .lookup("java:app/euslugi-zarzadzanie-ejb/KonfiguracjaSerweraPocztyFacade");
        bean.setPrincipal(principal);
        bean.setClientIpAdress(clientIpAdress);
        return bean;
    } 
}
