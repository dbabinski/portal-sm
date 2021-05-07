/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.rest;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import org.json.simple.JSONArray;
import pl.softmedica.ea.common.utilities.JSONBuilder;
import pl.softmedica.ea.common.utilities.JSONObjectExt;
import pl.softmedica.ea.common.utilities.Utilities;
import pl.softmedica.smportal.jpa.AktualnieZalogowani;
import pl.softmedica.smportal.session.AktualnieZalogowaniPortalFacadeLocal;

/**
 *
 * @author Lucek
 */
@Stateless
@Path("zalogowani")
public class RESTZalogowani {

    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");

    @Context
    SecurityContext securityContext;
    @Context
    HttpServletRequest httpRequest;
    @EJB
    private AktualnieZalogowaniPortalFacadeLocal aktualnieZalogowaniPortalFacade;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @Secured
    public Odpowiedz getZalogowani(
            @Context HttpServletRequest request,
            JSONObjectExt json
    ) {
        Odpowiedz odpowiedz = new Odpowiedz();
        try {
            boolean refresh = false;
            Integer limit = null;
            Integer offset = null;
            String filterText = null;
            Date filterDataOd = null;
            Date filterDataDo = null;
            String sort = null;

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
                try {
                    filterText = json.getString("filterText");
                } catch (Exception ex) {
                }
                try {
                    sort = json.getString("sort");
                } catch (Exception ex) {
                }
            }
            if (offset == null) {
                offset = 0;
            }
            sort = Utilities.nullToString(sort).toString();
            List<AktualnieZalogowani> list = aktualnieZalogowaniPortalFacade.find(filterText);
            int liczbaDostepnychRekordow = list.size();
            limit = limit != null ? limit : 50;
            
            list = list.stream()
                    .sorted(sort.equalsIgnoreCase("DESC") ? AktualnieZalogowani.COMPARATOR_BY_UUID.reversed() : AktualnieZalogowani.COMPARATOR_BY_UUID)
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
                        .put("aktualnieZalogowani", jsonArray)
                        .put("liczbaDostepnychRekordow", liczbaDostepnychRekordow)
                        .build());
            }
        } catch (Exception ex) {
            odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
        }
        return odpowiedz;
    }
}
