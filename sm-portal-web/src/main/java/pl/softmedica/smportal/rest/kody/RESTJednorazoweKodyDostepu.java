/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.rest.kody;

import java.util.Calendar;
import java.util.Date;
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
import javax.ws.rs.DELETE;
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
import pl.softmedica.smportal.common.utilities.GeneratorKodow;
import pl.softmedica.smportal.common.utilities.JSONArrayBuilder;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.JSONObjectExt;
import pl.softmedica.smportal.common.utilities.Utilities;
import pl.softmedica.smportal.common.utilities.Validator;
import pl.softmedica.smportal.jpa.Dostep;
import pl.softmedica.smportal.jpa.JednorazoweKodyDostepu;
import pl.softmedica.smportal.rest.IpAdress;
import pl.softmedica.smportal.rest.Odpowiedz;
import pl.softmedica.smportal.rest.RESTApplication;
import pl.softmedica.smportal.rest.Secured;
import pl.softmedica.smportal.session.DostepFacadeLocal;
import pl.softmedica.smportal.session.JednorazoweKodyDostepuFacadeLocal;

/**
 *
 * @author Łukasz Brzeziński <lukasz.brzezinski@softmedica.pl>
 */
@Stateless
@Path("jednorazowe-kody-dostepu")
public class RESTJednorazoweKodyDostepu {

    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    @Context
    SecurityContext securityContext;
    @Context
    HttpServletRequest httpRequest;
    @EJB
    private JednorazoweKodyDostepuFacadeLocal jednorazoweKodyDostepuFacade;
    @EJB
    private DostepFacadeLocal dostepFacade;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @Secured
    public Odpowiedz getKody() {
        return getKody(null, null);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lista")
    @Secured
    public Odpowiedz getKody(
            @Context HttpServletRequest request,
            JSONObjectExt json) {
        Odpowiedz odpowiedz = new Odpowiedz();
        try {
            boolean refresh = false;
            Integer limit = null;
            Integer offset = null;
            String filter = null;
            if (json != null) {
                try {
                    refresh = json.getBooleanSimple("refresh");
                } catch (NumberFormatException ex) {
                }
                try {
                    limit = json.getInteger("limit");
                } catch (NumberFormatException ex) {
                }
                try {
                    offset = json.getInteger("offset");
                } catch (NumberFormatException ex) {
                }
                filter = json.getString("filter");
            }
            if (offset == null) {
                offset = 0;
            }
            List<JednorazoweKodyDostepu> list = jednorazoweKodyDostepuFacade.find(filter, null);
            int liczbaDostepnychRekordow = list.size();
            limit = limit != null ? limit : 50;
            list = list.stream()
                    .sorted(JednorazoweKodyDostepu.COMPARATOR_BY_ID)
                    .skip(refresh ? 0 : offset)
                    .collect(Collectors.toCollection(LinkedList::new));
            if (list.size() > limit) {
                list = list.stream()
                        .limit(refresh ? offset : limit)
                        .collect(Collectors.toCollection(LinkedList::new));
            }

            if (list != null) {
                JSONArray jsonArray = new JSONArray();
                list.stream().forEach((item) -> {
                    jsonArray.add(item.getJSON());
                });
                odpowiedz.setDane(new JSONBuilder()
                        .put("kody", jsonArray)
                        .put("liczbaDostepnychRekordow", liczbaDostepnychRekordow)
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
    public Odpowiedz getKod(
            @PathParam("id") String paramId) {
        Odpowiedz odpowiedz = new Odpowiedz();
        int id = 0;
        try {
            id = Integer.parseInt(paramId);
        } catch (NumberFormatException ex) {
        }
        if (id > 0) {
            try {
                JednorazoweKodyDostepu object = jednorazoweKodyDostepuFacade.find(id);
                if (object != null) {
                    odpowiedz.setDane(object.getJSON());
                } else {
                    odpowiedz.setBlad(true).setKomunikat("Nie odnaleziono kodu o ID: " + id);
                }
            } catch (Exception ex) {
                odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
            }
        }
        return odpowiedz;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/generuj/{pesel}")
    @Secured
    public Odpowiedz generujKod(
            @PathParam("pesel") String pesel) {
        Odpowiedz odpowiedz = new Odpowiedz();
        try {
            String jednorazowyKodDostepu = generujJednorazowyKodDostepu();
            Date dataWaznosciKodu = Utilities.calculateDate(new Date(),
                    Calendar.DATE, getCzasWaznosciKoduJednorazowego());

            JednorazoweKodyDostepu nowyKod = new JednorazoweKodyDostepu()
                    .setKod(jednorazowyKodDostepu)
                    .setPesel(pesel)
                    .setZnacznikCzasuUtworzenia(new Date())
                    .setWaznyDo(dataWaznosciKodu);
            jednorazoweKodyDostepuFacade = JednorazoweKodyDostepuFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
            jednorazoweKodyDostepuFacade.create(nowyKod);

            odpowiedz.setDane(nowyKod.getJSON());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
        }

        return odpowiedz;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/generuj")
    @Secured
    public Odpowiedz generujKod() {
        Odpowiedz odpowiedz = new Odpowiedz();
        try {
            String jednorazowyKodDostepu = generujJednorazowyKodDostepu();
            Date dataWaznosciKodu = Utilities.calculateDate(new Date(),
                    Calendar.DATE, getCzasWaznosciKoduJednorazowego());

            JednorazoweKodyDostepu nowyKod = new JednorazoweKodyDostepu()
                    .setKod(jednorazowyKodDostepu)                    
                    .setZnacznikCzasuUtworzenia(new Date())
                    .setWaznyDo(dataWaznosciKodu);            

            odpowiedz.setDane(nowyKod.getJSON());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
        }

        return odpowiedz;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/parse")
    public Odpowiedz parse(
            @Context HttpServletRequest request,
            JSONObjectExt json
    ) {
        Odpowiedz odpowiedz = new Odpowiedz();
        JSONBuilder daneBuilder = new JSONBuilder();
        if (json != null) {
            HashMap<String, JSONArrayBuilder> mapaUwag = new HashMap<>();

            //sprawdzenie wymaganych pól                           
            JednorazoweKodyDostepu.POLA_WYMAGANE.stream().forEach(poleWymagane -> {
                if (json.isNull(poleWymagane)) {
                    RESTApplication.addToMap(
                            mapaUwag,
                            (String) poleWymagane,
                            "nie wypełniono pola " + Utilities.capitalizeFirstLetter(JednorazoweKodyDostepu.MAPA_POL.get(poleWymagane)));
                }
            });

            if (mapaUwag.get("pesel") == null
                    && !Validator.isValidPesel(json.getString("pesel"))) {
                RESTApplication.addToMap(mapaUwag, "pesel", "nieprawidłowy format PESEL");
            }

            if (mapaUwag.get("waznyDo") == null && !json.isNull("waznyDo")) {
                Date waznyDo = json.getDateTime("waznyDo");
                if (waznyDo == null) {
                    RESTApplication.addToMap(mapaUwag, "waznyDo", "nieprawidłowy format daty ważności kodu");
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
    @Secured
    public Odpowiedz setKod(
            @Context HttpServletRequest request,
            JSONObjectExt json
    ) {
        Odpowiedz odpowiedz = new Odpowiedz();
        if (json != null) {
            Odpowiedz wynikParsowania = parse(request, json);
            if (wynikParsowania.isBlad() || wynikParsowania.isUwaga()) {
                return odpowiedz.setBlad(true).setKomunikat("Wykryto błąd w danych");
            }

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
                    JednorazoweKodyDostepu object = jednorazoweKodyDostepuFacade.find(id);
                    if (object != null) {
                        object.setJSON(json);
                        jednorazoweKodyDostepuFacade.edit(object);
                        odpowiedz.setKomunikat("Kod został zaktualizowany");
                    } else {
                        odpowiedz.setBlad(true).setKomunikat("Nie odnaleziono kodu o ID: " + id);
                    }
                } else {
                    //INSERT                  
                    JednorazoweKodyDostepu object = new JednorazoweKodyDostepu();
                    jednorazoweKodyDostepuFacade.create(object.setJSON(json));
                    odpowiedz.setKomunikat("Kod został zapisany");
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage());
                odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
            }
        }
        return odpowiedz;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Secured
    public Odpowiedz deleteKod(
            @PathParam("id") String paramId) {
        Odpowiedz odpowiedz = new Odpowiedz();
        int id = 0;
        try {
            id = Integer.parseInt(paramId);
        } catch (NumberFormatException ex) {
        }
        try {
            JednorazoweKodyDostepu object = jednorazoweKodyDostepuFacade.find(id);
            if (object != null) {
                jednorazoweKodyDostepuFacade.remove(object);
                odpowiedz.setKomunikat("Kod został usunięty!");
            } else {
                odpowiedz.setBlad(true).setKomunikat("Nie znaleziono kodu o podanym ID!");
            }
        } catch (Exception ex) {
            odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
        }
        return odpowiedz;
    }

    private String generujJednorazowyKodDostepu() {
        String jednorazowyKodDostepu = GeneratorKodow.generuj(10);
        if (!czyKodUnikalny(jednorazowyKodDostepu)) {
            jednorazowyKodDostepu = generujJednorazowyKodDostepu();
        }
        return jednorazowyKodDostepu;
    }

    private boolean czyKodUnikalny(String jednorazowyKodDostepu) {
        List<JednorazoweKodyDostepu> kodyWBazie = jednorazoweKodyDostepuFacade.findAll();
        boolean kodUnikalny = kodyWBazie
                .stream()
                .filter(k -> k.getKod().equals(jednorazowyKodDostepu))
                .collect(Collectors.toList())
                .isEmpty();
        return kodUnikalny;
    }

    private int getCzasWaznosciKoduJednorazowego() {
        int czasWaznosciKodu = 0;
        Dostep serwisDostep = dostepFacade.findAll().isEmpty()
                ? null : dostepFacade.findAll().get(0);
        if (serwisDostep != null) {
            czasWaznosciKodu = serwisDostep.getCzasWaznosciKoduJednorazowego();
        }
        return czasWaznosciKodu;
    }
}
