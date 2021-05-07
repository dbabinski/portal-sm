/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.rest.portal;

import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import pl.softmedica.ea.common.utilities.JSONBuilder;
import pl.softmedica.smportal.jpa.portal.Regulamin;
import pl.softmedica.smportal.jpa.portal.Tresci;
import pl.softmedica.smportal.rest.Odpowiedz;
import pl.softmedica.smportal.session.portal.AktualnosciFacadeLocal;
import pl.softmedica.smportal.session.portal.RegulaminFacadeLocal;
import pl.softmedica.smportal.session.portal.TresciFacadeLocal;

/**
 *
 * @author Krzysztof Depka Prądzyński <k.d.pradzynski@softmedica.pl>
 */
@Stateless
@Path("portal")
public class RESTPortal {

    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    @Context
    SecurityContext securityContext;
    @EJB
    private RegulaminFacadeLocal regulaminFacade;
    @EJB
    private TresciFacadeLocal tresciFacade;
    @EJB
    private AktualnosciFacadeLocal aktualnosciFacade;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/regulamin")
    public Odpowiedz getRegulamin() {
        Odpowiedz odpowiedz = new Odpowiedz();
        try {
            Regulamin object = regulaminFacade.findLast();
            odpowiedz.setDane(new JSONBuilder()
                    .put("regulamin", object != null ? object.getJSON() : null)
                    .build());
        } catch (Exception ex) {
            odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
        }
        return odpowiedz;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stopka")
    public Odpowiedz getStopka() {
        Odpowiedz odpowiedz = new Odpowiedz();
        try {
            Tresci object = tresciFacade.find();
            odpowiedz.setDane(new JSONBuilder()
                    .put("stopka", object != null ? object.getStopka() : null)
                    .build());
        } catch (Exception ex) {
            odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
        }
        return odpowiedz;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/naglowek")
    public Odpowiedz getNaglowek() {
        Odpowiedz odpowiedz = new Odpowiedz();
        try {
            Tresci object = tresciFacade.find();
            odpowiedz.setDane(new JSONBuilder()
                    .put("naglowek", object != null ? object.getNaglowek() : null)
                    .build());
        } catch (Exception ex) {
            odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
        }
        return odpowiedz;
    }         
}
