/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.common.utilities;

import java.util.Random;

/**
 *
 * @author Łukasz Brzeziński <lukasz.brzezinski@softmedica.pl>
 */
public class GeneratorKodow {

    private static final String WIELKIE_LITERY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String MALE_LITERY = "abcdefghijklmnopqrstuvwxyz";
    private static final String CYFRY = "0123456789";
    private static final String ZNAKI_SPECJALNE = "!@#$%^&*_=+-/.?<>)";

    public static String generuj(int dlugoscKodu) {
        String mozliweWartosci = WIELKIE_LITERY + MALE_LITERY + CYFRY;
        Random random = new Random();
        char[] kod = new char[dlugoscKodu];
        for (int i = 0; i < dlugoscKodu; i++) {
            kod[i] = mozliweWartosci.charAt(random.nextInt(mozliweWartosci.length()));
        }
        return String.valueOf(kod);
    }
    
    public static String generujNoweHaslo(int dlugoscHasla) {
        String mozliweWartosci = WIELKIE_LITERY + MALE_LITERY + CYFRY + ZNAKI_SPECJALNE;
        Random random = new Random();
        char[] noweHaslo = new char[dlugoscHasla];
        for (int i = 0; i < dlugoscHasla; i++) {
            noweHaslo[i] = mozliweWartosci.charAt(random.nextInt(mozliweWartosci.length()));
        }
        return String.valueOf(noweHaslo);
    }
}
