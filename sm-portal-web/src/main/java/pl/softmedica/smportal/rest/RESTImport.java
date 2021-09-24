/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.JSONObjectExt;
import pl.softmedica.smportal.jpa.Kontrahenci;
import pl.softmedica.smportal.jpa.Pacjenci;
import pl.softmedica.smportal.jpa.Pracownicy;
import pl.softmedica.smportal.session.KonfiguracjaFacadeLocal;
import pl.softmedica.smportal.session.KontrahenciFacadeLocal;
import pl.softmedica.smportal.session.PacjenciFacadeLocal;
import pl.softmedica.smportal.session.PracownicyFacadeLocal;
import pl.softmedica.smportal.session.TypyDokumentowFacadeLocal;

/**
 *
 * @author Lucek
 */
@Stateless
@Path("import")
public class RESTImport {
    
    public static final Logger LOGGER = Logger.getLogger("pl.softmedica.ea");
    
    @Context
    SecurityContext securityContext;
    @Context
    HttpServletRequest httpRequest;
    @EJB
    private PacjenciFacadeLocal pacjenciFacade;
    @EJB
    private TypyDokumentowFacadeLocal typyDokumentowFacade;
    @EJB
    private PracownicyFacadeLocal pracownicyFacade;
    @EJB
    private KontrahenciFacadeLocal kontrahenciFacade;
        
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/przyklad")
    public Odpowiedz getPrzyklad(
            @Context HttpServletRequest request,
            JSONObject json
    ) {
        Odpowiedz odpowiedz = new Odpowiedz();
        try {
            if(json != null){
                if(json.containsKey("tabela")){
                    String tabela = (String) json.get("tabela");
                    switch(tabela){
                        case "pacjenci":
                            {
                                String kolumny = "";
                                String dane = "";
                                String obowiazkowe = "";
                                for (Pacjenci.kolumnyDoImportu kolumna : Pacjenci.COLUMN_LIST) {
                                    kolumny = kolumny + kolumna.name() + ";";
                                    dane = dane + Pacjenci.EXAMPLE_COLUMN_VALUES.get(kolumna) + ";";
                                    obowiazkowe = obowiazkowe + (Pacjenci.OBLIGATORY_COLUMNS_MAP.get(kolumna) ? "(kolumna obowiązkowa)" : "") + ";";
                                }

                                //usuwanie zbędnego znaku ";" na końcu wiersza
                                kolumny = kolumny.length() > 0 ? kolumny.substring(0, kolumny.length()-1) : kolumny;
                                dane = dane.length() > 0 ? dane.substring(0, dane.length()-1) : dane;
                                obowiazkowe = obowiazkowe.length() > 0 ? obowiazkowe.substring(0, obowiazkowe.length()-1) : obowiazkowe;
                                
                                odpowiedz.setDane(
                                    new JSONBuilder()
                                            .put("kolumny", kolumny)
                                            .put("dane", dane)
                                            .put("obowiazkowe", obowiazkowe)
                                            .build()
                                );

                            }
                            break;
                        case "pracownicy":
                            {
                                String kolumny = "";
                                String dane = "";
                                String obowiazkowe = "";
                                for (Pracownicy.kolumnyDoImportu kolumna : Pracownicy.COLUMN_LIST) {
                                    kolumny = kolumny + kolumna.name() + ";";
                                    dane = dane + Pracownicy.EXAMPLE_COLUMN_VALUES.get(kolumna) + ";";
                                    obowiazkowe = obowiazkowe + (Pracownicy.OBLIGATORY_COLUMNS_MAP.get(kolumna) ? "(kolumna obowiązkowa)" : "") + ";";
                                }
                                
                                //usuwanie zbędnego znaku ";" na końcu wiersza
                                kolumny = kolumny.length() > 0 ? kolumny.substring(0, kolumny.length()-1) : kolumny;
                                dane = dane.length() > 0 ? dane.substring(0, dane.length()-1) : dane;
                                obowiazkowe = obowiazkowe.length() > 0 ? obowiazkowe.substring(0, obowiazkowe.length()-1) : obowiazkowe;
                                
                                odpowiedz.setDane(
                                    new JSONBuilder()
                                            .put("kolumny", kolumny)
                                            .put("dane", dane)
                                            .put("obowiazkowe", obowiazkowe)
                                            .build()
                                );
                            }
                            break;
                        case "kontrahenci":
                            {
                                String kolumny = "";
                                String dane = "";
                                String obowiazkowe = "";
                                for (Kontrahenci.kolumnyDoImportu kolumna : Kontrahenci.COLUMN_LIST) {
                                    kolumny = kolumny + kolumna.name() + ";";
                                    dane = dane + Kontrahenci.EXAMPLE_COLUMN_VALUES.get(kolumna) + ";";
                                    obowiazkowe = obowiazkowe + (Kontrahenci.OBLIGATORY_COLUMNS_MAP.get(kolumna) ? "(kolumna obowiązkowa)" : "") + ";";
                                }
                                
                                //usuwanie zbędnego znaku ";" na końcu wiersza
                                kolumny = kolumny.length() > 0 ? kolumny.substring(0, kolumny.length()-1) : kolumny;
                                dane = dane.length() > 0 ? dane.substring(0, dane.length()-1) : dane;
                                obowiazkowe = obowiazkowe.length() > 0 ? obowiazkowe.substring(0, obowiazkowe.length()-1) : obowiazkowe;
                                
                                odpowiedz.setDane(
                                    new JSONBuilder()
                                            .put("kolumny", kolumny)
                                            .put("dane", dane)
                                            .put("obowiazkowe", obowiazkowe)
                                            .build()
                                );
                            }
                            break;
                        default:
                            odpowiedz.setBlad(true).setKomunikat("Wybrana tabela nie znajduje się w zakresie danych mozliwym do importu!");
                    }
                } else {
                    odpowiedz.setBlad(true).setKomunikat("Nie podano tabeli do importu!");
                }
            } else {
                odpowiedz.setBlad(true).setKomunikat("Przesyłanie danych nie powiodło się!");
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
    public Odpowiedz setImport(
            @Context HttpServletRequest request,
            JSONObject json
    ) {
        Odpowiedz odpowiedz = new Odpowiedz();
        
        if(json != null){
            if(json.containsKey("tabela")){
                String tabela = (String) json.get("tabela");
                
                List<String> tabele = new ArrayList();
                tabele.add("pacjenci");
                tabele.add("pracownicy");
                tabele.add("kontrahenci");
                //sprawdzanie czy wybrana tabela znajduje się w zakresie przygotowanych tabel
                if(tabele.contains(tabela)){
                    
                    //sprawdzanie czy przesłany komunikat zaiwera linie
                    if(json.containsKey("linie")){
                        
                        ArrayList linie = (ArrayList) json.get("linie");
                        
                        HashMap<Integer, String> naglowki = new HashMap();
                        String[] k = ((String)linie.get(0)).split(";");
                        Integer liczbaKolumn = k.length;
                        for (int i = 0; i < liczbaKolumn; i++) {
                            naglowki.put(i, k[i]);
                        }
                        //sprawdzanie czy wiersz 0 zawiera informacje o naglowkach kolumn i czy są kolumny wymagane
                        String naglowkiOdpowiedz = sprawdzNaglowki(naglowki.values(), tabela);

                        if(naglowkiOdpowiedz.isEmpty()){
                            
                            //sprawdzanie czy wiersze z danymi zawierają odpowiednią ilośc kolumn
                            int blad = -1;
                            for (int i = 1; i < linie.size() - 1; i++) {
                                int t = (int) ((String)linie.get(i)).chars().filter(ch -> ch == ';').count();
                                if(t != liczbaKolumn -1){
                                    blad = i;
                                    break;
                                }
                            }
                            if(blad == -1){
                                try {
                                    //przygotowanie kolumn w formacie dla JSON
                                    String[] kolumny = new String[liczbaKolumn-1];
                                    for (int i = 0; i < liczbaKolumn-1; i++) {
                                        String[] n = naglowki.get(i).split("_");
                                        if(n.length == 1){
                                            kolumny[i] = naglowki.get(i).toLowerCase();
                                        } else {
                                            kolumny[i] = n[0].toLowerCase();
                                            for (int l = 1; l < n.length; l++) {
                                                kolumny[i] = kolumny[i] + n[l].substring(0, 1) + n[l].substring(1).toLowerCase();
                                            }
                                        }
                                    }
                                    ArrayList<JSONObject> importList = new ArrayList<>();
                                    //przygotowanie wartości z wiersza
                                    for (int i = 1; i < linie.size(); i++) {
                                        String[] wiersz = ((String)linie.get(i)).split(Pattern.quote(";"));
                                        if(wiersz.length < liczbaKolumn){
                                            wiersz = ((String)linie.get(i)).replace(";;", "; ;").split(Pattern.quote(";"));
                                            for (int j = 0; j < liczbaKolumn-1; j++){
                                                if(wiersz[j].equals(" ")){
                                                    wiersz[j] = "";
                                                }
                                            }
                                        }
                                        JSONBuilder builder = new JSONBuilder();
                                        for (int j = 0; j < liczbaKolumn-1; j++) {
                                            String wartos = wiersz[j];
                                            if(naglowki.get(j) != null && naglowki.get(j).equals(Pacjenci.kolumnyDoImportu.PLEC.name())){
                                                if (wiersz[j].equals("k")){
                                                    wartos = "Kobieta";
                                                } else if(wiersz[j].equals("m")){
                                                    wartos = "Mężczyzna";
                                                }
                                            }
                                            builder.put(kolumny[j], wartos);
                                        }
                                        JSONObject obj = builder.build();

                                        Odpowiedz parseOdpowiedz = null;

                                        switch(tabela){
                                            case "pacjenci":
                                                parseOdpowiedz = RESTPacjenci.parse(new JSONObjectExt(obj), null, pacjenciFacade, typyDokumentowFacade, null, null, null, null, null);
                                                break;
                                            case "pracownicy":
                                                parseOdpowiedz = RESTPracownicy.parse(new JSONObjectExt(obj), pracownicyFacade);
                                                break;
                                            case "kontrahenci":
                                                parseOdpowiedz = RESTKontrahenci.parse(new JSONObjectExt(obj), kontrahenciFacade);
                                                break;
                                        }
                                        importList.add(obj);

                                        if(parseOdpowiedz.isUwaga()){

                                            String s = odpowiedz.getKomunikat();
                                            if(s == null){
                                                s = "</br>" + "W linii " + (i + 1) + " napotkano błąd:</br>";
                                                for(Object key : parseOdpowiedz.getDane().keySet()){
                                                    s = s + ((JSONArray)parseOdpowiedz.getDane().get((String)key)).get(0) + "</br>";
                                                }
                                                odpowiedz.setBlad(true).setKomunikat(s);
                                                break;
                                            } else {
                                                s = s + "</br>" + "W linii " + (i + 1) + " napotkano błąd</br>";
                                                for(Object key : parseOdpowiedz.getDane().keySet()){
                                                    s = s + ((JSONArray)parseOdpowiedz.getDane().get((String)key)).get(0) + "</br>";
                                                }
                                                odpowiedz.setBlad(true).setKomunikat(s);
                                                break;
                                            }
                                        } 
                                    }
                                    if(!odpowiedz.isBlad()){
                                        switch(tabela){
                                            case "pacjenci":
                                                for (JSONObject o : importList) {
                                                    Pacjenci p = new Pacjenci().setJSON(o);
                                                    pacjenciFacade = PacjenciFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                                                    pacjenciFacade.create(p);
                                                }
                                                break;
                                            case "pracownicy":
                                                for (JSONObject o : importList) {
                                                    pracownicyFacade = PracownicyFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                                                    pracownicyFacade.create(new Pracownicy().setJSON(o));
                                                }
                                                break;
                                            case "kontrahenci":
                                                for (JSONObject o : importList) {
                                                    kontrahenciFacade = KontrahenciFacadeLocal.create(securityContext, IpAdress.getClientIpAddr(httpRequest));
                                                    kontrahenciFacade.create(new Kontrahenci().setJSON(o));
                                                }
                                                break;
                                        }
                                        odpowiedz.setBlad(true).setKomunikat("Import zakończony.\nPrzetworzono " + importList.size() + " rekordów.");
                                    }
                                } catch (Exception ex) {
                                    odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
                                }
                            } else {
                                odpowiedz.setBlad(true).setKomunikat("Błąd danych w pliku - nieprawidłowa liczba kolumn w linii " + (blad + 1) + "!");
                            }
                            
                        } else {
                            odpowiedz.setBlad(true).setKomunikat(naglowkiOdpowiedz);
                        }
                    } else {
                        odpowiedz.setBlad(true).setKomunikat("Nie udało się wczytać pliku z danymi!");
                    }
                } else {
                    odpowiedz.setBlad(true).setKomunikat("Wybrana tabela nie znajduje się w zakresie danych mozliwym do importu!");
                }
            } else {
                odpowiedz.setBlad(true).setKomunikat("Nie podano tabeli do importu!");
            }
        } else {
            odpowiedz.setBlad(true).setKomunikat("Przesyłanie danych nie powiodło się!");
        }
        return odpowiedz;
    }
    
    private String sprawdzNaglowki(Collection<String> listaNaglowkowZPliku, String tabela){
        List<String> kolumnyOk = new ArrayList<>();
        List<String> kolumnyBrak = new ArrayList<>();
        List<String> kolumnyWymagane = new ArrayList<>();
        
        StringBuilder odpowiedz = new StringBuilder();
        
        switch(tabela){
            case "pacjenci":
                for (Pacjenci.kolumnyDoImportu kolumna : Pacjenci.COLUMN_LIST) {
                    if (listaNaglowkowZPliku.contains(kolumna.name())){
                        kolumnyOk.add(kolumna.name());
                    } else {
                        kolumnyBrak.add(kolumna.name());
                    }
                }
                
                for(String kolumna : kolumnyBrak){
                    if(Pacjenci.OBLIGATORY_COLUMNS_MAP.containsKey(Pacjenci.kolumnyDoImportu.valueOf(kolumna))){
                        if(Pacjenci.OBLIGATORY_COLUMNS_MAP.get(Pacjenci.kolumnyDoImportu.valueOf(kolumna))){
                            kolumnyWymagane.add(kolumna);
                        }
                    }
                }
                break;
            case "pracownicy":
                for (Pracownicy.kolumnyDoImportu kolumna : Pracownicy.COLUMN_LIST) {
                    if (listaNaglowkowZPliku.contains(kolumna.name())){
                        kolumnyOk.add(kolumna.name());
                    } else {
                        kolumnyBrak.add(kolumna.name());
                    }
                }
                
                for(String kolumna : kolumnyBrak){
                    if(Pracownicy.OBLIGATORY_COLUMNS_MAP.containsKey(Pracownicy.kolumnyDoImportu.valueOf(kolumna))){
                        if(Pracownicy.OBLIGATORY_COLUMNS_MAP.get(Pracownicy.kolumnyDoImportu.valueOf(kolumna))){
                            kolumnyWymagane.add(kolumna);
                        }
                    }
                }
                break;
            case "kontrahenci":
                for (Kontrahenci.kolumnyDoImportu kolumna : Kontrahenci.COLUMN_LIST) {
                    if (listaNaglowkowZPliku.contains(kolumna.name())){
                        kolumnyOk.add(kolumna.name());
                    } else {
                        kolumnyBrak.add(kolumna.name());
                    }
                }
                
                for(String kolumna : kolumnyBrak){
                    if(Kontrahenci.OBLIGATORY_COLUMNS_MAP.containsKey(Kontrahenci.kolumnyDoImportu.valueOf(kolumna))){
                        if(Kontrahenci.OBLIGATORY_COLUMNS_MAP.get(Kontrahenci.kolumnyDoImportu.valueOf(kolumna))){
                            kolumnyWymagane.add(kolumna);
                        }
                    }
                }
                break;
        }
        
        if(!kolumnyWymagane.isEmpty()){
            odpowiedz.append("W pliku brakuje wymaganych kolumn").append("</br></br>");
            for(String kolumna : kolumnyBrak){
                String wymagana = (kolumnyWymagane.contains(kolumna) ? " - kolumna wymagana" : "");
                odpowiedz.append(kolumna).append(wymagana).append("</br>");
            }
            odpowiedz.append("</br>").append("Sprawdź przykładowy plik w ceru weryfikacji");
        }
        return odpowiedz.toString();
    }
}
