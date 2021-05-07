/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import java.util.List;
import pl.softmedica.smportal.jpa.AktualnieZalogowani;

/**
 *
 * @author Lucek
 */
public interface AktualnieZalogowaniPortalFacadeLocal {
    
    void aktualizuj(AktualnieZalogowani zalogowany);
    
    void aktualizuj(String zalogowany);
    
    List<AktualnieZalogowani> find(String filtr);
}
