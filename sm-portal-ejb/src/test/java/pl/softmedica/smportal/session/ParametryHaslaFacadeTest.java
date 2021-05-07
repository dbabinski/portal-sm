/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import pl.softmedica.smportal.session.ParametryHaslaFacade;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.softmedica.smportal.jpa.ParametryHasla;

/**
 *
 * @author Krzysztof Depka Prądzyński <k.d.pradzynski@softmedica.pl>
 */
public class ParametryHaslaFacadeTest {

    public ParametryHaslaFacadeTest() {
    }

    @Test
    public void testSprawdz() {
        System.out.println("ParametryHaslaFacade.sprawdz");

        ParametryHasla parametry = new ParametryHasla()
                .setLiczbaCyfr(2)
                .setLiczbaZnakowSpecjalnych(2)
                .setWielkoscLiter(true)
                .setMinimalnaDlugosc(8);
        String haslo;
        System.out.println("hasło: " + (haslo = null) + " -> " + printList(ParametryHaslaFacade.sprawdz(parametry, haslo)));
        System.out.println("hasło: " + (haslo = "abcd") + " -> " + printList(ParametryHaslaFacade.sprawdz(parametry, haslo)));
        System.out.println("hasło: " + (haslo = "abcdefgh") + " -> " + printList(ParametryHaslaFacade.sprawdz(parametry, haslo)));
        System.out.println("hasło: " + (haslo = "abcdefgh1") + " -> " + printList(ParametryHaslaFacade.sprawdz(parametry, haslo)));
        System.out.println("hasło: " + (haslo = "abcdefgh12") + " -> " + printList(ParametryHaslaFacade.sprawdz(parametry, haslo)));
        System.out.println("hasło: " + (haslo = "abcdefgh12!") + " -> " + printList(ParametryHaslaFacade.sprawdz(parametry, haslo)));
        System.out.println("hasło: " + (haslo = "abcdefgh12!@") + " -> " + printList(ParametryHaslaFacade.sprawdz(parametry, haslo)));
        System.out.println("hasło: " + (haslo = "ABCDEFGH12!@") + " -> " + printList(ParametryHaslaFacade.sprawdz(parametry, haslo)));
        System.out.println("hasło: " + (haslo = "Abcdefgh12!@") + " -> " + printList(ParametryHaslaFacade.sprawdz(parametry, haslo)));
        System.out.println("hasło: " + (haslo = "Ąbcdefgh12!@") + " -> " + printList(ParametryHaslaFacade.sprawdz(parametry, haslo)));
        System.out.println("hasło: " + (haslo = "012!@#$%^&") + " -> " + printList(ParametryHaslaFacade.sprawdz(parametry, haslo)));
        System.out.println("hasło: " + (haslo = "Ąą012!@#$%^&") + " -> " + printList(ParametryHaslaFacade.sprawdz(parametry, haslo)));
        assertTrue(true);
    }

    private String printList(List list) {
        if (list != null) {
            if (list.isEmpty()) {
                return "OK";
            }
            return list.stream().map(o -> "\n  - " + o.toString()).collect(Collectors.joining("")).toString();
        }
        return "";
    }

}
