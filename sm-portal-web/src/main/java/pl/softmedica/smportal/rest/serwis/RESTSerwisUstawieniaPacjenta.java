/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.rest.serwis;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import org.json.simple.JSONObject;
import pl.softmedica.ea.common.utilities.JSONBuilder;
import pl.softmedica.smportal.jpa.UstawieniaPacjenta;
import pl.softmedica.smportal.rest.IpAdress;
import pl.softmedica.smportal.rest.Odpowiedz;
import pl.softmedica.smportal.session.UstawieniaPacjentaFacadeLocal;

/**
 *
 * @author Łukasz Brzeziński <lukasz.brzezinski@softmedica.pl>
 */
@Stateless
@Path("serwis/ustawienia-pacjenta")
public class RESTSerwisUstawieniaPacjenta {

    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    @Context
    SecurityContext securityContext;
    @Context
    HttpServletRequest httpRequest;
    @EJB
    private UstawieniaPacjentaFacadeLocal ustawieniaPacjentaFacade;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Odpowiedz getUstawieniaPacjenta() {
        Odpowiedz odpowiedz = new Odpowiedz();
        try {
            UstawieniaPacjenta object = ustawieniaPacjentaFacade.find();
            if (object == null) {
                object = new UstawieniaPacjenta();
            }
            odpowiedz.setDane(new JSONBuilder()
                    .put("ustawieniaPacjenta", object.getJSON())
                    .build());
        } catch (Exception ex) {
            odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
        }
        return odpowiedz;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Odpowiedz setUstawieniaPacjenta(
            @Context HttpServletRequest request,
            JSONObject json
    ) {
        Odpowiedz odpowiedz = new Odpowiedz();
        if (json != null) {
            UstawieniaPacjenta object = ustawieniaPacjentaFacade.find();
            if (object == null) {
                object = new UstawieniaPacjenta();
            }
            try {
                if (object.getId() != null) {
                    //UPDATE        
                    ustawieniaPacjentaFacade = UstawieniaPacjentaFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                    ustawieniaPacjentaFacade.edit(object.setJSON(json));
                    odpowiedz.setKomunikat("Ustawienia pacjenta zostały zaktualizowane");
                } else {
                    //INSERT
                    ustawieniaPacjentaFacade = UstawieniaPacjentaFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                    ustawieniaPacjentaFacade.create(object.setJSON(json));
                    odpowiedz.setKomunikat("Ustawienia pacjenta zostały zapisane");
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage());
                odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
            }
        }
        return odpowiedz;
    }
}
