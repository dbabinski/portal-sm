/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import java.util.List;
import javax.ejb.Local;
import pl.softmedica.smportal.jpa.Autentykacja;
import pl.softmedica.smportal.jpa.Konta;

/**
 *
 * @author Krzysztof Depka Prądzyński <k.d.pradzynski@softmedica.pl>
 */
@Local
@Deprecated
public interface AutentykacjaFacadeLocal {

    void create(Autentykacja object);

    Autentykacja edit(Autentykacja object);

    void remove(Autentykacja object);

    Autentykacja find(Object id);

    List<Autentykacja> findAll();

    List<Autentykacja> findRange(int[] range);

    int count();

    Autentykacja createManaged(Autentykacja entity);

    Autentykacja find(Konta idKonta);

    Autentykacja findByLogin(String login);

    Autentykacja findByEmail(String email);

    Autentykacja findByToken(String token);
}
