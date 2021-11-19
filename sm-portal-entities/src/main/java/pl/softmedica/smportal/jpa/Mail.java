/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.jpa;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import org.json.simple.JSONObject;
import pl.softmedica.smportal.common.interfaces.InterfaceJSON;
import pl.softmedica.smportal.common.interfaces.InterfaceUUID;
import pl.softmedica.smportal.common.utilities.JSONArrayBuilder;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.JSONObjectExt;
import pl.softmedica.smportal.common.utilities.Utilities;

/**
 *
 * @author Lucek
 */
public class Mail implements Serializable, InterfaceJSON<Mail>, InterfaceUUID<Mail> {

    private static final long serialVersionUID = 4270029219633418864L;

    private String uuid = UUID.randomUUID().toString();
    private Konfiguracja konfiguracja = null;
    private KonfiguracjaSerweraPoczty konfiguracjaPoczty = null;
    private Session session = null;
    private List<String> odbiorcy = new LinkedList<>();
    private String tematWiadomosci = null;
    private String trescWiadomosci = null;
    private String token = null;

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------   
    public KonfiguracjaSerweraPoczty getKonfiguracjaPoczty() {
        return konfiguracjaPoczty;
    }

    public Mail setKonfiguracjaPoczty(KonfiguracjaSerweraPoczty konfiguracja) {
        this.konfiguracjaPoczty = konfiguracja;
        return this;
    }

    public Konfiguracja getKonfiguracja() {
        return konfiguracja;
    }

    public Mail setKonfiguracja(Konfiguracja konfiguracja) {
        this.konfiguracja = konfiguracja;
        return this;
    }

    public List<String> getOdbiorcy() {
        return odbiorcy;
    }

    public Mail setOdbiorcy(List<String> odbiorcy) {
        this.odbiorcy = odbiorcy;
        return this;
    }

    public String getTematWiadomosci() {
        return tematWiadomosci;
    }

    public Mail setTematWiadomosci(String tematWiadomosci) {
        this.tematWiadomosci = tematWiadomosci;
        return this;
    }

    public String getTrescWiadomosci() {
        return trescWiadomosci;
    }

    public Mail setTrescWiadomosci(String trescWiadomosci) {
        this.trescWiadomosci = trescWiadomosci;
        return this;
    }

    public Session getSession() {
        return session;
    }

    public Mail setSession(Session session) {
        this.session = session;
        return this;
    }

    public Mail setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (konfiguracjaPoczty != null ? konfiguracjaPoczty.hashCode() : 0);
        hash = 89 * hash + Objects.hashCode(this.odbiorcy);
        hash = 89 * hash + Objects.hashCode(this.tematWiadomosci);
        hash = 89 * hash + Objects.hashCode(this.trescWiadomosci);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Mail)) {
            return false;
        }
        return System.identityHashCode(this) == System.identityHashCode(object);
    }

    @Override
    public String toString() {
        return "pl.softmedica.smportal.zarzadzanie.jpa.Mail[ uuid=" + uuid + " ]";
    }

    public Mail wyslij() throws MailException, NoSuchProviderException, MessagingException, Exception {
        if (session == null) {
            throw new MailException("Brak sesji");
        }
        if (konfiguracjaPoczty == null) {
            throw new MailException("Brak konfiguracji serwera pocztowego");
        }
        if (odbiorcy == null || odbiorcy.isEmpty()) {
            throw new MailException("Brak odbiorców wiadomości");
        }
        if (Utilities.stringToNull(tematWiadomosci) == null) {
            throw new MailException("Brak tematu wiadomości");
        }
        if (Utilities.stringToNull(trescWiadomosci) == null) {
            throw new MailException("Brak treści wiadomości");
        }

        if (konfiguracjaPoczty.isSSL()) {
            session.getProperties().setProperty("mail.smtps.host", konfiguracjaPoczty.getAdresSerweraPoczty());
            session.getProperties().setProperty("mail.smtps.port", konfiguracjaPoczty.getPortSerweraPoczty());
            session.getProperties().setProperty("mail.smtps.auth", "true");
            session.getProperties().setProperty("mail.smtps.localhost", konfiguracjaPoczty.getAdresSerweraPoczty());
            session.getProperties().setProperty("mail.smtps.starttls.enable", "true");
            session.getProperties().setProperty("mail.smtps.ssl.trust", konfiguracjaPoczty.getAdresSerweraPoczty());
        } else {
            session.getProperties().setProperty("mail.smtp.host", konfiguracjaPoczty.getAdresSerweraPoczty());
            session.getProperties().setProperty("mail.smtp.port", konfiguracjaPoczty.getPortSerweraPoczty());
            session.getProperties().setProperty("mail.smtp.auth", "true");
            session.getProperties().setProperty("mail.smtp.localhost", konfiguracjaPoczty.getAdresSerweraPoczty());
            session.getProperties().setProperty("mail.smtp.starttls.enable", "true");
            session.getProperties().setProperty("mail.smtp.ssl.trust", konfiguracjaPoczty.getAdresSerweraPoczty());
        }
        session.getProperties().put("mail.debug", "true");

        Transport transport;
        if (konfiguracjaPoczty.isSSL()) {
            transport = session.getTransport("smtps");
        } else {
            transport = session.getTransport("smtp");
        }
        transport.connect(konfiguracjaPoczty.getAdresSerweraPoczty(),
                konfiguracjaPoczty.getUzytkownik(),
                KonfiguracjaSerweraPoczty.decodePassword(konfiguracjaPoczty.getHasloSerweraPoczty()));

        MimeMessage message = new MimeMessage(session);
        if (konfiguracjaPoczty.getNadawca() != null && konfiguracjaPoczty.getEmail() != null) {
            try {
                message.setFrom(new InternetAddress(konfiguracjaPoczty.getEmail(), konfiguracjaPoczty.getNadawca()));
            } catch (UnsupportedEncodingException | MessagingException ex) {
            }
        }
        message.setRecipients(Message.RecipientType.TO, odbiorcy.stream().collect(Collectors.joining(",")));
        message.setSubject(MimeUtility.encodeText(this.tematWiadomosci, "utf-8", "B"));       

//        Multipart multipart = new MimeMultipart();
//        BodyPart htmlBodyPart = new MimeBodyPart();
//        htmlBodyPart.setContent(this.trescWiadomosci, "text/html; charset=\"UTF-8\"");
//        multipart.addBodyPart(htmlBodyPart);
//        message.setContent(multipart);
        message = setMessageContent(message, trescWiadomosci, konfiguracja);
        message.saveChanges();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

        return this;
    }

    public static MimeMessage setMessageContent(MimeMessage message, String html, Konfiguracja konfiguracja)
            throws MessagingException {
        if (message != null && html != null) {
            Multipart multipart = new MimeMultipart();
            BodyPart htmlBodyPart = new MimeBodyPart();
            htmlBodyPart.setContent(html, "text/html; charset=\"UTF-8\"");
            multipart.addBodyPart(htmlBodyPart);
            if (konfiguracja != null && konfiguracja.getLogo() != null && html.contains("<img src=\"cid:logo\">")) {
                String base64 = konfiguracja.getLogo();
                String dataType = "image/*";
                int index = base64.indexOf(";");
                if (index > 0) {
                    dataType = base64.substring(0, index).replaceAll("data:", "");
                }
                String extension = null;
                index = dataType.indexOf("/");
                if (index > 0 && index + 1 < dataType.length()) {
                    extension = dataType.substring(index + 1, dataType.length());
                }
                index = base64.indexOf("base64,");
                if (index > 0) {
                    base64 = base64.substring(index + "base64,".length());
                }
                byte[] imgBytes = Base64.getDecoder().decode(base64);
                ByteArrayDataSource dataSource = new ByteArrayDataSource(imgBytes, dataType);
                MimeBodyPart imageBodyPart = new MimeBodyPart();
                imageBodyPart.setDataHandler(new DataHandler(dataSource));
                imageBodyPart.setHeader("Content-ID", "<logo>");
                imageBodyPart.setFileName("logo" + (extension != null ? "." + extension : ""));
                multipart.addBodyPart(imageBodyPart);
            }
            message.setContent(multipart);
        }
        return message;
    }

    public static String wypelnijSzablon(Konta konto, Klienci klient, Konfiguracja konfiguracja, String szablon,
            String noweHaslo, String token) {
        if (konto != null && szablon != null && konfiguracja != null) {
            return szablon
                    .replaceAll("\\[logo\\]", konfiguracja.getLogo() != null ? "<img src=\"cid:logo\">" : "")
                    .replaceAll("\\[nazwa_serwisu\\]", Utilities.nullToString(konfiguracja.getNazwaSerwisu()).toString())
                    .replaceAll("\\[url_serwera\\]", Utilities.nullToString(konfiguracja.getUrlSerwera()).toString())
                    .replaceAll("\\[email_administratora\\]", Utilities.nullToString(konfiguracja.getEmailAdministratora()).toString())
                    .replaceAll("\\[uuid\\]", Utilities.nullToString(konto.getUUID()).toString())
                    .replaceAll("\\[login\\]", Utilities.nullToString(konto.getLogin()).toString())
                    .replaceAll("\\[imie\\]", klient != null ? Utilities.nullToString(klient.getImie()).toString() : "")
                    .replaceAll("\\[nazwisko\\]", klient != null ? Utilities.nullToString(klient.getNazwisko()).toString() : "")
                    .replaceAll("\\[email\\]", konto != null ? Utilities.nullToString(konto.getEmail()).toString()
                            : (klient != null ? Utilities.nullToString(klient.getEmail()).toString() : ""))
                    .replaceAll("\\[nowe_haslo\\]", noweHaslo != null ? noweHaslo : "")
                    .replaceAll("\\[token\\]", token != null ? token : "")
                    .replaceAll("\\[blokada_konta_do\\]",
                            konto.getBlokadaKontaDo() != null
                            ? Utilities.dateToDateTimeString(konto.getBlokadaKontaDo())
                            : (konto.isBlokadaKonta() ? "bezterminowo" : "null"));
        }
        return szablon;
    }

    //--------------------------------------------------------------------------
    // InterfaceJSON
    //--------------------------------------------------------------------------   
    @Override
    public JSONObject getJSON() {
        JSONBuilder builder = new JSONBuilder();
        if (this.odbiorcy != null && !this.odbiorcy.isEmpty()) {
            JSONArrayBuilder builderOdbiorcy = new JSONArrayBuilder();
            for (String odbiorca : this.odbiorcy) {
                builderOdbiorcy.add(odbiorca);
            }
            builder.put("odbiorcy", builderOdbiorcy.build());
        }
        builder
                .put("tematWiadomosci", this.tematWiadomosci)
                .put("trescWiadomosci", this.trescWiadomosci);
        return builder.build();
    }

    @Override
    public Mail setJSON(JSONObject json) {
        if (json != null) {
            JSONObjectExt jsone = new JSONObjectExt(json);
            this.odbiorcy = new LinkedList<>();
            jsone.getArray("odbiorcy").forEach(item -> {
                this.odbiorcy.add(item.toString());
            });
            this.tematWiadomosci = jsone.getString("tematWiadomosci");
            this.trescWiadomosci = jsone.getString("trescWiadomosci");
        }
        return this;
    }

    //--------------------------------------------------------------------------
    // InterfaceUUID
    //--------------------------------------------------------------------------
    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public Mail setUUID(String sid) {
        this.uuid = sid;
        return this;
    }

    @Override
    public Mail setUUID() {
        if (uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
        return this;
    }

    //--------------------------------------------------------------------------
    // Klas wyjątku
    //--------------------------------------------------------------------------
    public class MailException extends Exception {

        public MailException() {
        }

        public MailException(String message) {
            super(message);
        }
    }

}
