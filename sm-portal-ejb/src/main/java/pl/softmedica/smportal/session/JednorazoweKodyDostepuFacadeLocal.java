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
import pl.softmedica.smportal.jpa.JednorazoweKodyDostepu;

/**
 *
 * @author Łukasz Brzeziński <lukasz.brzezinski@softmedica.pl>
 */
@Local
public interface JednorazoweKodyDostepuFacadeLocal {

    void create(JednorazoweKodyDostepu object);

    JednorazoweKodyDostepu edit(JednorazoweKodyDostepu object);

    void remove(JednorazoweKodyDostepu object);

    JednorazoweKodyDostepu find(Object id);

    List<JednorazoweKodyDostepu> findAll();

    List<JednorazoweKodyDostepu> findRange(int[] range);

    int count();

    JednorazoweKodyDostepu createManaged(JednorazoweKodyDostepu entity);

    List<JednorazoweKodyDostepu> find(String filtr, Integer limit);
    
    void setPrincipal(Principal principal);
    
    void setClientIpAdress(String clientIpAdress);
    
    static JednorazoweKodyDostepuFacadeLocal create(SecurityContext securityContext, String clientIpAdress) throws NamingException {
        return JednorazoweKodyDostepuFacadeLocal.create(securityContext != null
                ? securityContext.getUserPrincipal() : (Principal) null, clientIpAdress);
    }

    static JednorazoweKodyDostepuFacadeLocal create(Principal principal, String clientIpAdress) throws NamingException {
        InitialContext initialContext = new InitialContext();
        JednorazoweKodyDostepuFacadeLocal bean = (JednorazoweKodyDostepuFacadeLocal) initialContext
                .lookup("java:app/euslugi-zarzadzanie-ejb/JednorazoweKodyDostepuFacade");
        bean.setPrincipal(principal);
        bean.setClientIpAdress(clientIpAdress);
        return bean;
    }
}
