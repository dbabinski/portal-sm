/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.rest.uzytkownicy;

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
import pl.softmedica.ea.common.utilities.JSONBuilder;
import pl.softmedica.smportal.jpa.Grupy;
import pl.softmedica.smportal.jpa.Konta;
import pl.softmedica.smportal.jpa.Uprawnienia;
import pl.softmedica.smportal.jpa.UprawnieniaKonta;
import pl.softmedica.smportal.rest.IpAdress;
import pl.softmedica.smportal.rest.Odpowiedz;
import pl.softmedica.smportal.rest.Secured;
import pl.softmedica.smportal.session.GrupyFacadeLocal;
import pl.softmedica.smportal.session.KontaFacadeLocal;
import pl.softmedica.smportal.session.UprawnieniaFacadeLocal;
import pl.softmedica.smportal.session.UprawnieniaKontaFacadeLocal;

/**
 *
 * @author Łukasz Brzeziński <lukasz.brzezinski@softmedica.pl>
 */
@Stateless
@Path("uzytkownicy/uprawnienia")
public class RESTUzytkownicyUprawnienia {

    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    @Context
    SecurityContext securityContext;
    @Context
    HttpServletRequest httpRequest;
    @EJB
    private UprawnieniaFacadeLocal uprawnieniaFacade;
    @EJB
    private UprawnieniaKontaFacadeLocal uprawnieniaKontaFacade;
    @EJB
    private KontaFacadeLocal kontaFacade;
    @EJB
    private GrupyFacadeLocal grupyFacade;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Odpowiedz getUprawnienia() {
        Odpowiedz odpowiedz = new Odpowiedz();
        try {
            List<Uprawnienia> list = uprawnieniaFacade.findAll()
                    .stream()
                    .sorted(Uprawnienia.COMPARATOR_BY_GRUPA)
                    .collect(Collectors.toCollection(LinkedList::new));
            if (list != null) {
                JSONArray jsonArray = new JSONArray();
                list.stream().forEach((item) -> {
                    jsonArray.add(item.getJSON());
                });
                odpowiedz.setDane(new JSONBuilder()
                        .put("uprawnienia", jsonArray)
                        .build());
            }
        } catch (Exception ex) {
            odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
        }
        return odpowiedz;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @Secured
    public Odpowiedz setUprawnienia(
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
                Grupy uzytkownicyGrupa = null;
                try {
                    if (json.get("idGrupy") != null) {
                        Integer idGrupy = Integer.parseInt(json.get("idGrupy").toString());
                        uzytkownicyGrupa = grupyFacade.find(idGrupy);
                    }
                } catch (NumberFormatException ex) {
                }
                if (id != null) {
                    //UPDATE
                    Uprawnienia uprawnienia = uprawnieniaFacade.find(id);
                    if (uprawnienia != null) {
                        uprawnienia.setJSON(json)
                                .setIdGrupy(uzytkownicyGrupa);
                        uprawnieniaFacade = UprawnieniaFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                        uprawnieniaFacade.edit(uprawnienia);
                        odpowiedz.setKomunikat("Uprawnienia zostały zaktualizowane");
                    } else {
                        odpowiedz.setBlad(true).setKomunikat("Nie odnaleziono uprawnień o ID: " + id);
                    }
                } else {
                    //INSERT                  
                    Uprawnienia uprawnienia = new Uprawnienia();
                    uprawnieniaFacade = UprawnieniaFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                    uprawnieniaFacade.create(uprawnienia.setJSON(json)
                            .setIdGrupy(uzytkownicyGrupa));
                    odpowiedz.setKomunikat("Uprawnienia zostały zapisane");
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage());
                odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
            }
        }
        return odpowiedz;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Odpowiedz getGrupa(
            @PathParam("id") String paramId) {
        Odpowiedz odpowiedz = new Odpowiedz();
        int id = 0;
        try {
            id = Integer.parseInt(paramId);
        } catch (NumberFormatException ex) {
        }
        if (id > 0) {
            try {
                Uprawnienia uprawnienia = uprawnieniaFacade.find(id);
                if (uprawnienia != null) {
                    odpowiedz.setDane(uprawnienia.getJSON());
                } else {
                    odpowiedz.setBlad(true).setKomunikat("Nie odnaleziono uprawnienia o ID: " + id);
                }
            } catch (Exception ex) {
                odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
            }
        }
        return odpowiedz;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/konto")
    @Secured
    public Odpowiedz setUprawnieniaKonta(
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
                Konta kontoUzytkownika = null;
                try {
                    if (json.get("idKonta") != null) {
                        Integer idKonta = Integer.parseInt(json.get("idKonta").toString());
                        kontoUzytkownika = kontaFacade.find(idKonta);
                    }
                } catch (NumberFormatException ex) {
                }
                if (id != null) {
                    //UPDATE
                    UprawnieniaKonta uprawnieniaKonta = uprawnieniaKontaFacade.find(id);
                    if (uprawnieniaKonta != null) {
                        uprawnieniaKonta.setJSON(json)
                                .setIdKonta(kontoUzytkownika);
                        uprawnieniaKontaFacade = UprawnieniaKontaFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                        uprawnieniaKontaFacade.edit(uprawnieniaKonta);
                        odpowiedz.setKomunikat("Uprawnienia konta zostały zaktualizowane");
                    } else {
                        odpowiedz.setBlad(true).setKomunikat("Nie odnaleziono uprawnień o ID: " + id);
                    }
                } else {
                    //INSERT                  
                    UprawnieniaKonta uprawnienieKonta = new UprawnieniaKonta();
                    uprawnieniaKontaFacade = UprawnieniaKontaFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                    uprawnieniaKontaFacade.create(uprawnienieKonta.setJSON(json)
                            .setIdKonta(kontoUzytkownika));
                    odpowiedz.setKomunikat("Uprawnienia konta zostały zapisane");
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage());
                odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
            }
        }
        return odpowiedz;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/konto/{id}")
    public Odpowiedz getUprawnieniaKonta(
            @PathParam("id") String paramId) {
        Odpowiedz odpowiedz = new Odpowiedz();
        int id = 0;
        try {
            id = Integer.parseInt(paramId);
        } catch (NumberFormatException ex) {
        }
        if (id > 0) {
            try {
                UprawnieniaKonta uprawnieniaKonta = uprawnieniaKontaFacade.findByIdKonta(id);
                if (uprawnieniaKonta != null) {
                    odpowiedz.setDane(uprawnieniaKonta.getJSON());
                } else {
                    odpowiedz.setBlad(true).setKomunikat("Nie odnaleziono uprawnienia o ID: " + id);
                }
            } catch (Exception ex) {
                odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
            }
        }
        return odpowiedz;
    }
}
