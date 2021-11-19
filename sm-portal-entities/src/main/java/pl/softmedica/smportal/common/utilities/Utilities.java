/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.softmedica.smportal.common.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author chiefu
 */
public class Utilities {

    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_SEC_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Get a diff between two dates
     *
     * @param date1 the newest date
     * @param date2 the oldest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date1.getTime() - date2.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static String getCurrentDate() {
        return getCurrentDate(DATE_FORMAT);
    }

    public static String getCurrentDate(String formatStr) {
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        return formatter.format(today);
    }

    public static String dateToTimeString(Date date) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT);
            return formatter.format(date);
        }
        return null;
    }

    public static String dateToDateTimeString(Date date) {
        if (date != null) {
            return new SimpleDateFormat(DATE_TIME_SEC_FORMAT).format(date);
        }
        return null;
    }

    public static String timeToString(long time) {
        return dateToTimeString(new Date(time));
    }

    public static Date timeStringToDate(String timeString) {
        Date date = new Date();
        date.setTime(toTime(date, timeString));
        return date;
    }

    public static long toTime(Date date, String timeStr) {
        DateFormat formatter = new SimpleDateFormat(TIME_FORMAT);
        int hours;
        int minutes;
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(formatter.parse(timeStr));
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minutes = calendar.get(Calendar.MINUTE);
        } catch (ParseException e) {
            // invalid date format
            return new Date().getTime();
        }
        return toCalendar(date, hours, minutes).getTime().getTime();
    }

    public static Calendar toCalendar(Date date, int hour, int minute) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        return new GregorianCalendar(year, month, day, hour, minute);
    }

    public static Calendar toCalendar(int hour, int minutes) {
        return toCalendar(new Date(), hour, minutes);
    }

    public static Integer getYear(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static Integer getMonth(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static Integer getDayOfMonth(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String dateToString(java.util.Date date, String formatStr) {
        if (date != null) {
            SimpleDateFormat targetDateFormat = new SimpleDateFormat(formatStr);
            return targetDateFormat.format(date);
        }
        return "";
    }

    public static String dateToString(java.util.Date date) {
        return dateToString(date, DATE_FORMAT);
    }

    public static Date stringToDate(String dateStr, String formatStr) {
        Date date = null;
        if (dateStr != null && dateStr.length() > 0) {
            SimpleDateFormat targetDateFormat = new SimpleDateFormat(formatStr);
            try {
                date = targetDateFormat.parse(dateStr.trim());
            } catch (ParseException ex) {
            }
        }
        return date;
    }

    public static Date stringToDate(String dateStr) {
        return stringToDate(dateStr, DATE_FORMAT);
    }

    public static String stringToNull(String str) {
        if (str != null && str.trim().length() > 0) {
            return str.trim();
        }
        return null;
    }

    public static Object nullToString(Object object) {
        return object != null ? object : "";
    }

    public static String usunPodwojneSpacje(String tekst) {
        if (tekst != null) {
            tekst = zamienZnaki(tekst, "  ", " ");
            tekst = tekst.trim();
        }
        return tekst;
    }

    public static String zamienZnaki(String tekst, String zamieniane, String zamienniki) {
        if (tekst != null && zamieniane != null && zamienniki != null) {
            while (tekst.contains(zamieniane)) {
                tekst = tekst.replaceAll(zamieniane, zamienniki);
            }
        }
        return tekst;
    }

    public static String[] podzielImieNazwisko(String imieNazwisko) {
        String[] wynik = new String[2];
        if (imieNazwisko != null) {
            imieNazwisko = imieNazwisko.trim();
            if (!imieNazwisko.isEmpty()) {
                int index = imieNazwisko.indexOf(" ");
                if (index >= 0) {
                    wynik[0] = Utilities.stringToNull(imieNazwisko.substring(0, index).trim());
                    if (index + 1 < imieNazwisko.length()) {
                        wynik[1] = Utilities.stringToNull(
                                imieNazwisko.substring(index + 1, imieNazwisko.length()).trim());
                    }
                } else {
                    wynik[0] = Utilities.stringToNull(imieNazwisko.trim());
                }
            }
        }
        return wynik;
    }

    public static List<String> podzielTekst(String tekst, int maxLiczbaZnakow) {
        List<String> lista = new LinkedList<>();
        if (tekst != null && !tekst.isEmpty() && maxLiczbaZnakow > 0) {
            while (tekst.length() > maxLiczbaZnakow) {
                lista.add(tekst.substring(0, maxLiczbaZnakow));
                tekst = tekst.substring(maxLiczbaZnakow, tekst.length());
            }
            if (!tekst.isEmpty()) {
                lista.add(tekst);
            }
        }
        return lista;
    }

    public static List<String> podzielListe(String tekst, String separator, boolean trim) {
        List<String> lista = new LinkedList<>();
        if (tekst != null && !tekst.isEmpty()
                && separator != null && !separator.isEmpty()
                && (!trim || (trim && !tekst.trim().isEmpty() && !separator.trim().isEmpty()))) {
            int index = tekst.indexOf(separator);
            if (index >= 0) {
                String pozycjaListy = tekst.substring(0, index);
                lista.add(trim ? pozycjaListy.trim() : pozycjaListy);
                String reszta = tekst.substring(index + separator.length());
                lista.addAll(podzielListe(reszta, separator, trim));
            } else {
                lista.add(trim ? tekst.trim() : tekst);
            }
        }
        return lista;
    }

    public static Date calculateDate(Date date, int calendarField, int calendarAmount) {
        if (date != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendarField, calendarAmount);
            return calendar.getTime();
        }
        return null;
    }

    public static Integer calculateAge(Date birdthDate) {
        if (birdthDate != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(birdthDate);
            Calendar now = new GregorianCalendar();
            Integer res = now.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
            if ((calendar.get(Calendar.MONTH) > now.get(Calendar.MONTH))
                    || (calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && calendar.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
                res--;
            }
            return res;
        }
        return 0;
    }

    public static String sexConvert(String sex) {
        String result = null;
        if (sex != null && sex.trim().length() > 0) {
            sex = sex.trim();
            if (sex.length() == 1) {
                result = "Mężczyzna";
                if (sex.toLowerCase().equals("k")) {
                    result = "Kobieta";
                }
            } else {
                result = "m";
                if (sex.toLowerCase().equals("kobieta")) {
                    result = "k";
                }
            }
        }
        return result;
    }

    public static String sexConvert(Boolean sex) {
        String result = null;
        if (sex != null) {
            if (sex) {
                result = "Mężczyzna";
            } else {
                result = "Kobieta";
            }
        }
        return result;
    }

    public static String substring(String string, int maxLength) {
        return substring(string, maxLength, true);
    }

    public static String substring(String string, int maxLength, boolean withDots) {
        if (string != null && string.length() > maxLength && maxLength > 0) {
            if (withDots) {
                if (maxLength > 3) {
                    return string.substring(0, maxLength - 3) + "...";
                } else {
                    return string.substring(0, maxLength);
                }
            } else {
                return string.substring(0, maxLength);
            }
        }
        return string;
    }

    public static final String[] DNI_TYGODNIA = new String[]{"Niedziela", "Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota"};

    public static String getDzienTygodnia(Date date) {
        if (date != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            return DNI_TYGODNIA[day - 1];
        }
        return null;
    }

    public static String stringFill(String str, String fillChar, int length, boolean ahead) {
        if (str != null && fillChar != null && fillChar.length() == 1) {
            while (str.length() < length) {
                if (ahead) {
                    str = fillChar + str;
                } else {
                    str += fillChar;
                }
            }
        }
        return str;
    }

    public static int AFTER = -1;
    public static int BEFORE = 1;
    public static int EQUAL = 0;

    public static int compareDate(String dateString1, String dateString2)
            throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date1 = dateFormat.parse(dateString1);
        Date date2 = dateFormat.parse(dateString2);
        if (date1.equals(date2)) {
            return EQUAL;
        } // date1 equal date2 (date1 == date2)

        if (date1.before(date2)) {
            return BEFORE;
        } // date1 is before date2 (date1 < date2)

        return AFTER; // date1 is after date2 (date1 > date2)
    }

    public static int compareDate(Date date1, Date date2) {
        if (date1.equals(date2)) {
            return EQUAL;
        } // date1 equal date2 (date1 == date2)

        if (date1.before(date2)) {
            return BEFORE;
        } // date1 is before date2 (date1 < date2)

        return AFTER; // date1 is after date2 (date1 > date2)
    }

    public static int compareTime(String timeString1, String timeString2)
            throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        Date date1 = dateFormat.parse(timeString1);
        Date date2 = dateFormat.parse(timeString2);
        if (date1.equals(date2)) {
            return EQUAL;
        } // date1 equal date2 (date1 == date2)

        if (date1.before(date2)) {
            return BEFORE;
        } // date1 is before date2 (date1 < date2)

        return AFTER; // date1 is after date2 (date1 > date2)

    }

    public static boolean czyZakresyDatSieStykaja(Date s1, Date e1, Date s2, Date e2)
            throws Exception {
        if (s1 != null && e1 != null && s2 != null && e2 != null) {
            return s1.equals(e2) || s2.equals(e1);
        } else {
            throw new NullPointerException("Jedna z dat jest NULL");
        }
    }

    public static boolean czyZakresyDatMajaCzescWspolna(Date s1, Date e1, Date s2, Date e2)
            throws Exception {
        return czyZakresyDatMajaCzescWspolna(s1, e1, s2, e2, true);
    }

    public static boolean czyZakresyDatMajaCzescWspolna(Date s1, Date e1, Date s2, Date e2, boolean ignorujKrance)
            throws Exception {
        if (s1 == null || s2 == null) {
            throw new NullPointerException("Data s1 lub s2 jest NULL");
        }
        //e1 i e2 może być null
        if (e1 == null && e2 == null) {
            return true;
        }
        if (e1 == null && e2 != null) {
            if (ignorujKrance) {
                return s1.getTime() < e2.getTime();
            } else {
                return s1.getTime() <= e2.getTime();
            }
        }
        if (e1 != null && e2 == null) {
            if (ignorujKrance) {
                return s2.getTime() < e1.getTime();
            } else {
                return s2.getTime() <= e1.getTime();
            }
        }
        if (e1 != null && e2 != null) {
            if (ignorujKrance) {
                return (s1.getTime() <= s2.getTime() && e1.getTime() >= e2.getTime())
                        || (s1.getTime() >= s2.getTime() && s1.getTime() < e2.getTime())
                        || (e1.getTime() > s2.getTime() && e1.getTime() <= e2.getTime());
            } else {
                return (s1.getTime() <= s2.getTime() && e1.getTime() >= e2.getTime())
                        || (s1.getTime() >= s2.getTime() && s1.getTime() <= e2.getTime())
                        || (e1.getTime() >= s2.getTime() && e1.getTime() <= e2.getTime());
            }
        }

        return false;
    }

    public static boolean czyDataJestWZakresie(Date d, Date s, Date e) throws Exception {
        return czyDataJestWZakresie(d, s, e, false);
    }

    public static boolean czyDataJestWZakresie(Date d, Date s, Date e, boolean dopuszczalnaNaGranicy) throws Exception {
        if (d != null && s != null && e != null) {
            if (dopuszczalnaNaGranicy) {
                return d.getTime() >= s.getTime() && d.getTime() <= e.getTime();
            } else {
                return d.getTime() > s.getTime() && d.getTime() < e.getTime();
            }
        } else {
            throw new NullPointerException("Jedna z dat jest NULL");
        }
    }

    public static String listToString(List<String> list, String elementPrefix, String elementPostfix, String lastElementPostfix) {
        StringBuilder builder = new StringBuilder();
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                builder.append(elementPrefix != null ? elementPrefix : "").append(list.get(i));
                if (i + 1 == size) {
                    builder.append(lastElementPostfix != null ? lastElementPostfix : "");
                } else {
                    builder.append(elementPostfix != null ? elementPostfix : "");
                }
            }
        }
        return builder.toString();
    }

    public static String doubleToString(double number, String format) {
        NumberFormat formatter = new DecimalFormat(format);
        return formatter.format(number);
    }

    public static String bigDecimalToString(BigDecimal number) {
        return bigDecimalToString(number, "#0.00");
    }

    public static String bigDecimalToString(BigDecimal number, String format) {
        if (number != null) {
            return Utilities.doubleToString(((BigDecimal) number).doubleValue(), format);
        }
        return null;
    }

    public static Date calculateTimeTo(Date timeFrom, Integer minutes) {
        if (timeFrom != null && minutes != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timeFrom);
            calendar.add(Calendar.MINUTE, minutes);
            return calendar.getTime();
        }
        return null;
    }

    public static Date calculateTimeFrom(Date timeTo, Integer minutes) {
        if (timeTo != null && minutes != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timeTo);
            calendar.add(Calendar.MINUTE, -minutes);
            return calendar.getTime();
        }
        return null;
    }

    public static Integer calculateMinutes(Date timeFrom, Date timeTo) {
        if (timeFrom != null && timeTo != null) {
            return (int) TimeUnit.MINUTES.convert(timeTo.getTime() - timeFrom.getTime(), TimeUnit.MILLISECONDS);
        }
        return null;
    }

    public static String getSexFromPesel(String pesel) {
        if (Validator.isValidPesel(pesel)) {
            int sex = Integer.parseInt(pesel.substring(9, 10));
            if ((sex % 2) == 0) {
                return "Kobieta";
            }
        }
        return "Mężczyzna";
    }

    public static String getDateFromPesel(String pesel) {
        if (Validator.isValidPesel(pesel)) {
            String yearLow = "19";
            String yearHigh = pesel.substring(0, 2);
            int monthInt = Integer.parseInt(pesel.substring(2, 4));
            String monthStr = String.valueOf(monthInt);
            if (monthInt >= 21 && monthInt <= 32) {
                yearLow = "20";
                monthStr = String.valueOf(monthInt - 20);
            }
            if (monthInt >= 41 && monthInt <= 52) {
                yearLow = "21";
                monthStr = String.valueOf(monthInt - 40);
            }
            if (monthInt >= 61 && monthInt <= 72) {
                yearLow = "22";
                monthStr = String.valueOf(monthInt - 60);
            }
            if (monthInt >= 81 && monthInt <= 92) {
                yearLow = "18";
                monthStr = String.valueOf(monthInt - 80);
            }
            if (monthStr.length() < 2) {
                monthStr = "0" + monthStr;
            }
            String day = pesel.substring(4, 6);
            return yearLow + yearHigh + "-" + monthStr + "-" + day;
        }
        return "";
    }

    public static String polishPlural(String singularNominativ, String pluralNominativ, String pluralGenitive, int value) {
        if (value == 1) {
            return singularNominativ;
        } else if (value % 10 >= 2 && value % 10 <= 4 && (value % 100 < 10 || value % 100 >= 20)) {
            return pluralNominativ;
        } else {
            return pluralGenitive;
        }
    }

    public static String capitalizeFirstLetter(String text) {
        if (text != null && !text.isEmpty()) {
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        }
        return text;
    }

    public static String usunPolskieOgonki(String tekst) {
        if (tekst != null) {
            return tekst
                    .replaceAll("ą", "a")
                    .replaceAll("Ą", "A")
                    .replaceAll("ć", "c")
                    .replaceAll("Ć", "C")
                    .replaceAll("ę", "e")
                    .replaceAll("Ę", "E")
                    .replaceAll("ł", "l")
                    .replaceAll("Ł", "L")
                    .replaceAll("ń", "n")
                    .replaceAll("Ń", "N")
                    .replaceAll("ó", "o")
                    .replaceAll("Ó", "O")
                    .replaceAll("ś", "s")
                    .replaceAll("Ś", "S")
                    .replaceAll("ź", "z")
                    .replaceAll("Ź", "Z")
                    .replaceAll("ż", "z")
                    .replaceAll("Ż", "Z");
        }
        return tekst;
    }

    public static String inputStreamToString(InputStream inputStream) throws IOException {
        final char[] buffer = new char[8192];
        final StringBuilder builder = new StringBuilder();
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            int charsRead;
            while ((charsRead = reader.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, charsRead);
            }
        }
        return builder.toString();
    }
}
