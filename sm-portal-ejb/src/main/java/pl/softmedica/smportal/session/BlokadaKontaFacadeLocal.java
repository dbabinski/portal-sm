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
import pl.softmedica.smportal.jpa.BlokadaKonta;
import pl.softmedica.smportal.jpa.Konta;

/**
 *
 * @author Lucek
 */
@Local
public interface BlokadaKontaFacadeLocal {

    void create(BlokadaKonta object);

    BlokadaKonta edit(BlokadaKonta object);

    void remove(BlokadaKonta object);

    BlokadaKonta find(Object id);

    List<BlokadaKonta> findAll();

    List<BlokadaKonta> findRange(int[] range);

    int count();

    BlokadaKonta createManaged(BlokadaKonta entity);

    BlokadaKonta find();

    void setPrincipal(Principal principal);
    
    void setClientIpAdress(String clientIpAdress);
    
    static BlokadaKontaFacadeLocal create(SecurityContext securityContext, String clientIpAdress) throws NamingException {
        return BlokadaKontaFacadeLocal.create(securityContext != null
                ? securityContext.getUserPrincipal() : (Principal) null, clientIpAdress);
    }

    static BlokadaKontaFacadeLocal create(Principal principal, String clientIpAdress) throws NamingException {
        InitialContext initialContext = new InitialContext();
        BlokadaKontaFacadeLocal bean = (BlokadaKontaFacadeLocal) initialContext
                .lookup("java:app/sm-portal-ejb/BlokadaKontaFacade");
        bean.setPrincipal(principal);
        bean.setClientIpAdress(clientIpAdress);
        return bean;
    }
}
