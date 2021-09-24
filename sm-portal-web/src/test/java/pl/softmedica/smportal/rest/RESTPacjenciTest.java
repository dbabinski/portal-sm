/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.rest;

import org.junit.Assert;
import org.junit.Test;
import pl.softmedica.smportal.common.utilities.JSONBuilder;
import pl.softmedica.smportal.common.utilities.JSONObjectExt;
import pl.softmedica.smportal.common.utilities.Utilities;
import pl.softmedica.smportal.jpa.UstawieniaPacjenta;

/**
 *
 * @author chiefu
 */
public class RESTPacjenciTest {

    public RESTPacjenciTest() {
    }

    /**
     * Test of parse method, of class RESTPacjenci.
     */
    @Test
    public void testParse() throws Exception {
        System.out.println("RESTPacjenci.parse");

        Odpowiedz odpowiedz = new Odpowiedz();

        JSONBuilder builder = new JSONBuilder();
        builder
                .put("nrLokalu", "A")
                .put("numerDokumentuTozsamosci", "ABC123456")
                .put("miejsceUrodzenia", null)
                .put("miejscowosc", "Chojnice")
                .put("kodPocztowy", "89-600")
                .put("nrDomu", null)
                .put("plec", "mężczyzna")
                .put("dataUrodzenia", "1978-03-03")
                .put("imie", "Jan")
                .put("ulica", "Testowa")
                .put("nazwisko", "Testowy")
                .put("telefonKontaktowy", "600100100")
                .put("idTypDokumentuTozsamosci", 1)
                .put("pesel", "78030308339")
                .put("email", "testowy@gmail.com");

        UstawieniaPacjenta ustawieniaPacjenta = new UstawieniaPacjenta()
                .setObowiazkoweDataUrodzenia(true)
                .setObowiazkoweMiejceUrodzenia(true)
                .setObowiazkoweTelefon(true)
                .setObowiazkoweDaneAdresowe(true);

        odpowiedz = new RESTPacjenci().parse(new JSONObjectExt(builder.build()), ustawieniaPacjenta);
        System.out.println(odpowiedz.getDane() != null ? odpowiedz.getDane().toJSONString() : null);
        Assert.assertTrue(odpowiedz.getDane() != null
                && odpowiedz.getDane().get("miejsceUrodzenia") != null
                && odpowiedz.getDane().get("nrDomu") != null);

        builder.put("pesel", "78030308338")
                .put("miejsceUrodzenia", "Chojnice")
                .put("nrDomu", "12");
        odpowiedz = new RESTPacjenci().parse(new JSONObjectExt(builder.build()), ustawieniaPacjenta);
        System.out.println(odpowiedz.getDane() != null ? odpowiedz.getDane().toJSONString() : null);
        Assert.assertTrue(odpowiedz.getDane() != null
                && odpowiedz.getDane().get("pesel") != null);

        builder.put("pesel", "78030308339")
                .put("dataUrodzenia", "1978-03-01");
        odpowiedz = new RESTPacjenci().parse(new JSONObjectExt(builder.build()), ustawieniaPacjenta);
        System.out.println(odpowiedz.getDane() != null ? odpowiedz.getDane().toJSONString() : null);
        Assert.assertTrue(odpowiedz.getDane() != null
                && odpowiedz.getDane().get("dataUrodzenia") != null);

        builder.put("dataUrodzenia", "1978-03-03")
                .put("plec", "kobieta");
        odpowiedz = new RESTPacjenci().parse(new JSONObjectExt(builder.build()), ustawieniaPacjenta);
        System.out.println(odpowiedz.getDane() != null ? odpowiedz.getDane().toJSONString() : null);
        Assert.assertTrue(odpowiedz.getDane() != null
                && odpowiedz.getDane().get("plec") != null);

        builder.put("plec", "mężczyzna");
        ustawieniaPacjenta.setMinimalnyWiekPacjenta(Utilities.calculateAge(
                Utilities.stringToDate(
                        Utilities.getDateFromPesel((String) builder.get("pesel")))) + 1);
        odpowiedz = new RESTPacjenci().parse(new JSONObjectExt(builder.build()), ustawieniaPacjenta);
        System.out.println(odpowiedz.getDane() != null ? odpowiedz.getDane().toJSONString() : null);
        Assert.assertTrue(odpowiedz.getDane() != null
                && odpowiedz.getDane().get("dataUrodzenia") != null);

        ustawieniaPacjenta.setMinimalnyWiekPacjenta(10);
        builder.put("kodPocztowy", "01234xx");
        odpowiedz = new RESTPacjenci().parse(new JSONObjectExt(builder.build()), ustawieniaPacjenta);
        System.out.println(odpowiedz.getDane() != null ? odpowiedz.getDane().toJSONString() : null);
        Assert.assertTrue(odpowiedz.getDane() != null
                && odpowiedz.getDane().get("kodPocztowy") != null);

        builder.put("kodPocztowy", "89-600")
                .put("email", "test@test");
        odpowiedz = new RESTPacjenci().parse(new JSONObjectExt(builder.build()), ustawieniaPacjenta);
        System.out.println(odpowiedz.getDane() != null ? odpowiedz.getDane().toJSONString() : null);
        Assert.assertTrue(odpowiedz.getDane() != null
                && odpowiedz.getDane().get("email") != null);

        builder .put("email", "test@test.pl")
                .put("telefonKontaktowy", "000");
        odpowiedz = new RESTPacjenci().parse(new JSONObjectExt(builder.build()), ustawieniaPacjenta);
        System.out.println(odpowiedz.getDane() != null ? odpowiedz.getDane().toJSONString() : null);
        Assert.assertTrue(odpowiedz.getDane() != null
                && odpowiedz.getDane().get("telefonKontaktowy") != null);
    }
}
