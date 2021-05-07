/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Local;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.SecurityContext;
import pl.softmedica.smportal.jpa.Pacjenci;

/**
 *
 * @author Lucek
 */
@Local
public interface PacjenciFacadeLocal{

    void create(Pacjenci object);

    Pacjenci edit(Pacjenci object);

    void remove(Pacjenci object);

    Pacjenci find(Object id);

    List<Pacjenci> findAll();

    List<Pacjenci> findRange(int[] range);

    int count();

    Pacjenci createManaged(Pacjenci entity);

    /**     
     *
     * @param pesel numer PESEL
     * @param id    jeśli id != null to pacjent o tym id nie jest wybierany
     * @return lista pacjentów o wskazanym numerze pesel
     */
    List<Pacjenci> findByPesel(String pesel, Integer id);

    LinkedList<Pacjenci> getPacjenci(Integer idKonta);

    List<Pacjenci> find(String filter, Integer limit);

    List<Pacjenci> findByPesel(String pesel);

    List<Pacjenci> findByKonto(Integer idKonta);
    
    Pacjenci findByEmail(String email);

    void setPrincipal(Principal principal);    
    
    void setClientIpAdress(String clientIpAdress);
    
    static PacjenciFacadeLocal create(SecurityContext securityContext, String clientIpAdress) throws NamingException {
        return PacjenciFacadeLocal.create(securityContext != null
                ? securityContext.getUserPrincipal() : (Principal) null, clientIpAdress);
    }

    static PacjenciFacadeLocal create(Principal principal, String clientIpAdress) throws NamingException {
        InitialContext initialContext = new InitialContext();
        PacjenciFacadeLocal bean = (PacjenciFacadeLocal) initialContext
                .lookup("java:app/euslugi-zarzadzanie-ejb/PacjenciFacade");
        bean.setPrincipal(principal);
        bean.setClientIpAdress(clientIpAdress);
        return bean;
    }
}
