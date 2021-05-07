/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Lucek
 */
@Stateless
public class AktualnieZalogowaniUpdateFacade implements AktualnieZalogowaniFacadeLocal {

    @EJB
    private AktualnieZalogowaniPortalFacadeLocal aktualnieZalogowaniPortalFacade;
    
    @Override
    public void getAktualizuj(String aktualnieZalogowani) {
        aktualnieZalogowaniPortalFacade.aktualizuj(aktualnieZalogowani);
    }
}