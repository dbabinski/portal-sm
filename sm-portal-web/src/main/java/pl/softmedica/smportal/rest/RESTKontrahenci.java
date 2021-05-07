package pl.softmedica.smportal.rest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.softmedica.ea.common.utilities.JSONArrayBuilder;
import pl.softmedica.ea.common.utilities.JSONBuilder;
import pl.softmedica.ea.common.utilities.JSONObjectExt;
import pl.softmedica.ea.common.utilities.Validator;
import pl.softmedica.smportal.jpa.Kontrahenci;
import pl.softmedica.smportal.session.KontrahenciFacadeLocal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucek
 */
@Stateless
@Path("kontrahenci")
public class RESTKontrahenci {
    
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");
    
    @Context
    SecurityContext securityContext;
    @Context
    HttpServletRequest httpRequest;
    @EJB
    private KontrahenciFacadeLocal kontrahenciFacade;
    
    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Odpowiedz getKontrahenci() {
        Odpowiedz odpowiedz = new Odpowiedz();
        try {
            List<Kontrahenci> list = kontrahenciFacade.findAll()
                    .stream()
                    .sorted((Kontrahenci t1, Kontrahenci t2)
                            -> t1.getNazwa().compareToIgnoreCase(t2.getNazwa()))
                    .collect(Collectors.toCollection(LinkedList::new));
            if (list != null) {
                JSONArray jsonArray = new JSONArray();
                list.stream().forEach((item) -> {
                    jsonArray.add(item.getJSON());
                });
                odpowiedz.setDane(new JSONBuilder()
                        .put("kontrahenci", jsonArray)
                        .build());
            }
        } catch (Exception ex) {
            odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
        }
        return odpowiedz;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Odpowiedz getKontrahent(
            @PathParam("id") String paramId) {
        Odpowiedz odpowiedz = new Odpowiedz();
        int id = 0;
        try {
            id = Integer.parseInt(paramId);
        } catch (NumberFormatException ex) {
        }
        if (id > 0) {
            try {
                Kontrahenci object = kontrahenciFacade.find(id);
                if (object != null) {
                    odpowiedz.setDane(object.getJSON());
                } else {
                    odpowiedz.setBlad(true).setKomunikat("Nie odnaleziono kontrahenta o ID: " + id);
                }
            } catch (Exception ex) {
                odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
            }
        }
        return odpowiedz;
    }
    
    public static Odpowiedz parse(
            JSONObjectExt json,
            KontrahenciFacadeLocal kontrahenciFacade) {
        Odpowiedz odpowiedz = new Odpowiedz();
        JSONBuilder daneBuilder = new JSONBuilder();
        if (json != null) {
            HashMap<String, JSONArrayBuilder> mapaUwag = new HashMap<>();

            //email
            if (json.getString("email") != null) {
                if (!Validator.isValidEmail(json.getString("email"))) {
                    RESTApplication.addToMap(mapaUwag, "email", "nieprawidłowy format adresu e-mail");
                }
            }
            //numer telefonu         
            if (json.getString("telefonKontaktowy") != null) {
                if (!Validator.isValidPhoneNumber(json.getString("telefonKontaktowy"))) {
                    RESTApplication.addToMap(mapaUwag, "telefonKontaktowy", "nieprawidłowy format numeru telefonu");
                }
            }

            mapaUwag.entrySet().stream().forEach(entry -> {
                if (!entry.getValue().isEmpty()) {
                    daneBuilder.put(entry.getKey(), entry.getValue().build());
                }
            });
            JSONObject dane = daneBuilder.build();
            odpowiedz.setUwaga(!dane.isEmpty()).setDane(dane);
        }
        return odpowiedz;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Odpowiedz setKontrahent(
            @Context HttpServletRequest request,
            JSONObject json
    ) {
        Odpowiedz odpowiedz = new Odpowiedz();
        if (json != null) {
            try {
                Integer id = null;
                try {
                    if (json.get("id") != null) {
                        id = Integer.parseInt(json.get("id").toString());
                    }
                } catch (NumberFormatException ex) {
                }
                if (id != null) {
                    //UPDATE
                    Kontrahenci object = kontrahenciFacade.find(id);
                    if (object != null) {
                        kontrahenciFacade = KontrahenciFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                        kontrahenciFacade.edit(object.setJSON(json));
                        odpowiedz.setKomunikat("Dane kontrahenta zostały zaktualizowane");
                    } else {
                        odpowiedz.setBlad(true).setKomunikat("Nie odnaleziono kontrahenta o ID: " + id);
                    }
                } else {
                    //INSERT
                    kontrahenciFacade = KontrahenciFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                    kontrahenciFacade.create(new Kontrahenci().setJSON(json));
                    odpowiedz.setKomunikat("Dane kontrahenta zostały zapisane");
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage());
                odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
            }
        }
        return odpowiedz;
    }
}
