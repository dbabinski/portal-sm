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
import pl.softmedica.smportal.jpa.TypyGrup;

/**
 *
 * @author Lucek
 */
@Local
public interface TypyGrupFacadeLocal {

    void create(TypyGrup object);

    TypyGrup edit(TypyGrup object);

    void remove(TypyGrup object);

    TypyGrup find(Object id);

    List<TypyGrup> findAll();

    List<TypyGrup> findRange(int[] range);

    int count();

    TypyGrup createManaged(TypyGrup entity);

    boolean czyNazwaUnikalna(Integer id, String opis);

    public boolean czyMogeUsunacTypGrupy(int id);
    
    void setPrincipal(Principal principal); 
    
    void setClientIpAdress(String clientIpAdress);
    
    static TypyGrupFacadeLocal create(SecurityContext securityContext, String clientIpAdress) throws NamingException {
        return TypyGrupFacadeLocal.create(securityContext != null
                ? securityContext.getUserPrincipal() : (Principal) null, clientIpAdress);
    }

    static TypyGrupFacadeLocal create(Principal principal, String clientIpAdress) throws NamingException {
        InitialContext initialContext = new InitialContext();
        TypyGrupFacadeLocal bean = (TypyGrupFacadeLocal) initialContext
                .lookup("java:app/sm-portal-ejb/TypyGrupFacade");
        bean.setPrincipal(principal);
        bean.setClientIpAdress(clientIpAdress);
        return bean;
    }
}
