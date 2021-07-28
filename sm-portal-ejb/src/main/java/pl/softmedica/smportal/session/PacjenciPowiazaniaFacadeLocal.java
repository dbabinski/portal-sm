/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.SecurityContext;
import pl.softmedica.smportal.jpa.PacjenciPowiazania;

/**
 *
 * @author Lucek
 */
@Local
public interface PacjenciPowiazaniaFacadeLocal {

    void create(PacjenciPowiazania object);

    PacjenciPowiazania edit(PacjenciPowiazania object);

    void remove(PacjenciPowiazania object);

    PacjenciPowiazania find(Object id);

    List<PacjenciPowiazania> findAll();

    List<PacjenciPowiazania> findRange(int[] range);

    int count();

    ArrayList<PacjenciPowiazania> getPowiazaniaKonta(Integer idKonta);
    
    PacjenciPowiazania findBy(Integer idKonta, Integer idPacjenta);
    
    void setPrincipal(Principal principal);    
    
    void setClientIpAdress(String clientIpAdress);
    
    static PacjenciPowiazaniaFacadeLocal create(SecurityContext securityContext, String clientIpAdress) throws NamingException {
        return PacjenciPowiazaniaFacadeLocal.create(securityContext != null
                ? securityContext.getUserPrincipal() : (Principal) null, clientIpAdress);
    }

    static PacjenciPowiazaniaFacadeLocal create(Principal principal, String clientIpAdress) throws NamingException {
        InitialContext initialContext = new InitialContext();
        PacjenciPowiazaniaFacadeLocal bean = (PacjenciPowiazaniaFacadeLocal) initialContext
                .lookup("java:app/sm-portal-ejb/PacjenciPowiazaniaFacade");
        bean.setPrincipal(principal);
        bean.setClientIpAdress(clientIpAdress);
        return bean;
    }
}
