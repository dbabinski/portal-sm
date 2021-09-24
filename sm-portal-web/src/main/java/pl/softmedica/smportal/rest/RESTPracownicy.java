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
import pl.softmedica.smportal.common.utilities.JSONArrayBuilder;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.JSONObjectExt;
import pl.softmedica.smportal.common.utilities.Utilities;
import pl.softmedica.smportal.common.utilities.Validator;
import pl.softmedica.smportal.jpa.Pracownicy;
import pl.softmedica.smportal.session.PracownicyFacadeLocal;

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
@Path("pracownicy")
public class RESTPracownicy {
    
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    @Context
    SecurityContext securityContext;
    @Context
    HttpServletRequest httpRequest;
    @EJB
    private PracownicyFacadeLocal pracownicyFacade;
    
    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Odpowiedz getPracownicy() {
        Odpowiedz odpowiedz = new Odpowiedz();
        try {
            List<Pracownicy> list = pracownicyFacade.findAll()
                    .stream()
                    .sorted(Pracownicy.COMPARATOR_BY_NAZWISKO_IMIE)
                    .collect(Collectors.toCollection(LinkedList::new));
            if (list != null) {
                JSONArray jsonArray = new JSONArray();
                list.stream().forEach((item) -> {
                    jsonArray.add(item.getJSON());
                });
                odpowiedz.setDane(new JSONBuilder()
                        .put("pracownicy", jsonArray)
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
    public Odpowiedz getPracownik(
            @PathParam("id") String paramId) {
        Odpowiedz odpowiedz = new Odpowiedz();
        int id = 0;
        try {
            id = Integer.parseInt(paramId);
        } catch (NumberFormatException ex) {
        }
        if (id > 0) {
            try {
                Pracownicy object = pracownicyFacade.find(id);
                if (object != null) {
                    odpowiedz.setDane(object.getJSON());
                } else {
                    odpowiedz.setBlad(true).setKomunikat("Nie odnaleziono pracownika o ID: " + id);
                }
            } catch (Exception ex) {
                odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
            }
        }
        return odpowiedz;
    }
    
    public static Odpowiedz parse(
            JSONObjectExt json,
            PracownicyFacadeLocal pracownicyFacade) {
        Odpowiedz odpowiedz = new Odpowiedz();
        JSONBuilder daneBuilder = new JSONBuilder();
        if (json != null) {

            HashMap<String, JSONArrayBuilder> mapaUwag = new HashMap<>();

            //płeć zgodna z pesel
            if (json.getString("plec") != null && mapaUwag.get("pesel") == null) {
                String plec = Utilities.getSexFromPesel(json.getString("pesel")).toLowerCase();
                if (!json.getString("plec").equalsIgnoreCase(plec)) {
                    RESTApplication.addToMap(mapaUwag, "plec", "płeć niezgodna z numerem PESEL");
                }
            }
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
    public Odpowiedz setPracownik(
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
                    Pracownicy object = pracownicyFacade.find(id);
                    if (object != null) {
                        pracownicyFacade = PracownicyFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                        pracownicyFacade.edit(object.setJSON(json));
                        odpowiedz.setKomunikat("Dane pracownika zostały zaktualizowane");
                    } else {
                        odpowiedz.setBlad(true).setKomunikat("Nie odnaleziono pracownika o ID: " + id);
                    }
                } else {
                    //INSERT    
                    pracownicyFacade = PracownicyFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                    pracownicyFacade.create(new Pracownicy().setJSON(json));
                    odpowiedz.setKomunikat("Dane pracownika zostały zapisane");
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage());
                odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
            }
        }
        return odpowiedz;
    }
}
