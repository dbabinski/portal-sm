/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.softmedica.smportal.common.utilities;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Weryfikator numerów NIP, PESEL, REGON, IBAN
 *
 * @author chiefu
 */
public abstract class Validator {

    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String TIME_FORMAT = "HH:mm";

    /**
     * Weryfikacja numeru REGON
     *
     * @param regon
     * @return true jeśli prawidłowy
     */
    public static boolean isValidRegon(String regon) {

        int lengthBeforeTrim = regon.length();
        regon = trimInput(regon);
        int lenghtAfterTrim = regon.length();
        if (lenghtAfterTrim != lengthBeforeTrim) {
            return false;
        }
        if (!((lenghtAfterTrim == 9) || (lenghtAfterTrim == 14))) {
            return false;
        }
        int[] weights9 = {8, 9, 2, 3, 4, 5, 6, 7};
        int[] weights14 = {2, 4, 8, 5, 0, 9, 7, 3, 6, 1, 2, 4, 8};
        int j, sum = 0, control;
        int csum = Integer.parseInt(regon.substring(lenghtAfterTrim - 1));
        for (int i = 0; i < lenghtAfterTrim - 1; i++) {
            char c = regon.charAt(i);
            j = Integer.parseInt(String.valueOf(c));
            if (lenghtAfterTrim == 9) {
                sum += j * weights9[i];
            } else if (lenghtAfterTrim == 14) {
                sum += j * weights14[i];
            }
        }
        control = sum % 11;
        if (control == 10) {
            control = 0;
        }
        return (control == csum);
    }

    /**
     * Weryfikacja numeru REGON
     *
     * @param regon
     * @return true jeśli prawidłowy
     */
    public static boolean isValidRegon(long regon) {
        return isValidRegon(Long.toString(regon));
    }

    /**
     * Weryfikacja numeru PKWiU
     *
     * @param pkwiu
     * @return true jeśli prawidłowy
     */
    public static boolean isValidPKWiU(String pkwiu) {

        boolean result = false;
        if (pkwiu == null) {
            return result;
        }
        pkwiu = pkwiu.trim();

        //Pozycje PKWiU 1997, 2004
        //xx.xx.xx-xx       - "[0-9]{2}.[0-9]{2}.[0-9]{2}-[0-9]{2} "
        //xx.xx.xx-xx.x     - "[0-9]{2}.[0-9]{2}.[0-9]{2}-[0-9]{2}.[0-9]{1,2}"
        //xx.xx.xx-xx.xx    - "[0-9]{2}.[0-9]{2}.[0-9]{2}-[0-9]{2}.[0-9]{1,2}"
        //Pozycje PKWiU 2008
        //xx.xx.xx.x        - "[0-9]{2}.[0-9]{2}.[0-9]{2}.[0-9]{1}"
        Pattern pattern1 = Pattern.compile("[0-9]{2}.[0-9]{2}.[0-9]{2}-[0-9]{2}");
        Matcher matcher1 = pattern1.matcher(pkwiu);
        Pattern pattern2 = Pattern.compile("[0-9]{2}.[0-9]{2}.[0-9]{2}-[0-9]{2}.[0-9]{1,2}");
        Matcher matcher2 = pattern2.matcher(pkwiu);
        Pattern pattern3 = Pattern.compile("[0-9]{2}.[0-9]{2}.[0-9]{2}.[0-9]{1}");
        Matcher matcher3 = pattern3.matcher(pkwiu);
        if (matcher1.matches() || matcher2.matches() || matcher3.matches()) {
            result = true;
        }
        return result;
    }

    /**
     * Weryfikacja numeru NIP
     *
     * @param nip
     * @return true jeśli prawidłowy
     */
    public static boolean isValidNip(String nip) {

        boolean result = false;
        if (nip == null) {
            return result;
        }
        //nip = trimInput(nip);

        //                    1234567890    "\\d{10}+"
        //dla osób fizycznych 123-456-78-90 "\\d{3}+-\\d{3}+-\\d{2}+-\\d{2}+"
        //a dla firm na grupy 123-45-67-890 "\\d{3}+-\\d{2}+-\\d{2}+-\\d{3}+"
        Pattern pattern1 = Pattern.compile("\\d{10}+");
        Matcher matcher1 = pattern1.matcher(nip);
        Pattern pattern2 = Pattern.compile("\\d{3}+-\\d{3}+-\\d{2}+-\\d{2}+");
        Matcher matcher2 = pattern2.matcher(nip);
        Pattern pattern3 = Pattern.compile("\\d{3}+-\\d{2}+-\\d{2}+-\\d{3}+");
        Matcher matcher3 = pattern3.matcher(nip);
        if (matcher1.matches() || matcher2.matches() || matcher3.matches()) {
            nip = nip.replaceAll("-", "");

            int nsize = nip.length();
            if (nsize != 10) {
                return false;
            }
            int[] weights = {6, 5, 7, 2, 3, 4, 5, 6, 7};
            int j, sum = 0, control;
            int csum = Integer.parseInt(nip.substring(nsize - 1));
            if (csum == 0) {
                csum = 10;
            }
            for (int i = 0; i < nsize - 1; i++) {
                char c = nip.charAt(i);
                j = Integer.parseInt(String.valueOf(c));
                sum += j * weights[i];
            }
            control = sum % 11;
            return (control == csum);
        }
        return result;
    }

    /**
     * Weryfikacje numeru NIP
     *
     * @param nip
     * @return true jeśli prawidłowy
     */
    public static boolean isValidNip(long nip) {
        return isValidNip(Long.toString(nip));
    }

    /**
     * Weryfikacja numeru PESEL
     *
     * @param pesel
     * @return true jeśli prawidłowy
     */
    public static boolean isValidPesel(String pesel) {
        if (pesel == null) {
            return false;
        }
        int lengthBeforeTrim = pesel.length();
        pesel = trimInput(pesel);
        int lengthAfterTrim = pesel.length();
        if (lengthAfterTrim != lengthBeforeTrim) {
            return false;
        }
        if (lengthAfterTrim != 11) {
            return false;
        }
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int j, sum = 0, control;
        int csum = Integer.parseInt(pesel.substring(lengthAfterTrim - 1));
        for (int i = 0; i < lengthAfterTrim - 1; i++) {
            char c = pesel.charAt(i);
            j = Integer.parseInt(String.valueOf(c));
            sum += j * weights[i];
        }
        control = 10 - (sum % 10);
        if (control == 10) {
            control = 0;
        }
        return (control == csum);
    }

    /**
     * Weryfikacja numeru PESEL
     *
     * @param pesel
     * @return true jeśli prawidłowy
     */
    public static boolean isValidPesel(long pesel) {
        return isValidPesel(Long.toString(pesel));
    }

    /**
     * Weryfikacje numeru IBAN
     *
     * @param iban
     * @return true jeśli prawidłowy
     */
    public static boolean isValidIban(String iban) {
        // Algorytm (c)  R.J.Żyłła 2000-2004 */
        // 0. Zamiana na wielkie litery i usunięcie śmieci
        // i spacji
        iban = iban.toUpperCase().replaceAll("[\\p{Punct}\\p{Space}]*", "");
        if (!iban.matches("^[A-Z]{2}[0-9]{12,}")) {
            return false;
        }
        // if (iban.length() < 16)
        //  return false;
        // 1. Pierwsze 4 znaki na koniec
        iban = iban.substring(4, iban.length()) + iban.substring(0, 4);
        // 2. Litery na cyfry
        for (int i = 0; i < iban.length(); i++) {
            char c = iban.charAt(i);
            if (Character.isUpperCase(c)) {
                int code = Character.getNumericValue(c);
                iban = iban.substring(0, i) + code + iban.substring(i + 1, iban.length());
            }
        }
        // 3. Modulo 97
        int mod = 0;
        int isize = iban.length();
        for (int i = 0; i < isize; i = i + 6) {
            try {
                mod = Integer.parseInt("" + mod + iban.substring(i, i + 6), 10) % 97;
            } catch (StringIndexOutOfBoundsException e) {
                return false;
            }
        }
        return (mod == 1);
    }

    /**
     * Weryfikacja numeru IBAN
     *
     * @param iban
     * @return true jeśli prawidłowy
     */
    public static boolean isValidIban(long iban) {
        return isValidIban(Long.toString(iban));
    }

    /**
     * Usuwa wszelkie znaki nie będące cyframi
     *
     * @param input
     * @return oczyszczony string
     */
    private static String trimInput(String input) {
        return input.replaceAll("\\D*", "");
    }

    /**
     * Validate whether the argument string can be parsed into a legal date.<br
     * />
     *
     * Does check for formating errors and illegal data (so an invalid month or
     * day number is detected).
     *
     * @param dateStr
     * @param allowPast set to true to allow dates in the past, false if only
     * dates in the future should be allowed.
     * @param formatStr date format string to be used to validate against
     * @return true if a correct date and conforms to the restrictions
     */
    public static boolean isValidDate(String dateStr, boolean allowPast, String formatStr) {
        if (formatStr == null) {
            return false;
        } // or throw some kinda exception, possibly a InvalidArgumentException

        DateFormat formatter = new SimpleDateFormat(formatStr);
        Date date;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            // invalid date format
            return false;
        }
        if (!allowPast) {
            // initialise the calendar to midnight to prevent
            // the current day from being rejected
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            if (cal.getTime().after(date)) {
                return false;
            }
        }
        return formatter.format(date).equals(dateStr);
    }

    public static boolean isValidTime(String timeStr, String formatStr) {
        if (timeStr == null) {
            return false;
        }
        DateFormat formatter = new SimpleDateFormat(formatStr);
        try {
            formatter.parse(timeStr);
        } catch (ParseException e) {
            // invalid date format
            return false;
        }
        return true;
    }

    public static boolean isValidNaturalNumber(String integerStr) {
        return isValidNaturalNumber(integerStr, false);
    }

    public static boolean isValidNaturalNumber(String integerStr, boolean greaterThanZero) {
        try {
            Integer integer = Integer.parseInt(integerStr);
            if (integer < 0) {
                return false;
            }
            if (greaterThanZero && integer == 0) {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static boolean isValidDecimalNumber(String decimalStr) {
        try {
            Double doubleNumber = Double.parseDouble(decimalStr);
            BigDecimal.valueOf(doubleNumber);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static boolean isValidPercentNumber(String percentStr) {
        try {
            Double doubleNumber = Double.parseDouble(percentStr);
            BigDecimal bigDecimal = BigDecimal.valueOf(doubleNumber);

            if (bigDecimal.compareTo(new BigDecimal("0")) < 0) {
                return false;
            }

            if (bigDecimal.compareTo(new BigDecimal("100")) > 0) {
                return false;
            }

            if (bigDecimal.scale() < 0 || bigDecimal.scale() > 2) {
                return false;
            }

        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static boolean isValidMoneyNumber(String moneyStr) {
        try {
            Double doubleNumber = Double.parseDouble(moneyStr);
            BigDecimal bigDecimal = BigDecimal.valueOf(doubleNumber);

            if (bigDecimal.intValue() > 9999999) {
                return false;
            }

            if (bigDecimal.scale() < 0 || bigDecimal.scale() > 2) {
                return false;
            }

        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

 

    public static void isValidOshCardNumber(String oshCardNumber) throws Exception {
        //Włocławek
        if (oshCardNumber == null) {
            throw new Exception("OSH number string is null");
        }
        Pattern pattern = Pattern.compile("\\D-\\d+");
        Matcher matcher = pattern.matcher(oshCardNumber);
        if (matcher.matches()) {
            String numberStr = oshCardNumber.substring(2, oshCardNumber.length());
            long number = Long.parseLong(numberStr);
            if (number < 1) {
                throw new Exception("OSH number < 1");
            }
        } else {
            throw new Exception("OSH number string '" + oshCardNumber + "' does not match the pattern");
        }
    }

    public static boolean isValidEmail(String email) {
        if (email != null) {
            Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return false;
    }

    public static boolean isValidPostalCode(String postalCode) {
        if (postalCode != null) {
            Pattern pattern = Pattern.compile("^\\d{2}-\\d{3}$");
            Matcher matcher = pattern.matcher(postalCode);
            return matcher.matches();
        }
        return false;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber != null) {
            Pattern pattern = Pattern.compile("^\\+{0,1}(?:[0-9] ?){6,14}[0-9]$");
            Matcher matcher = pattern.matcher(phoneNumber);
            return matcher.matches();
        }
        return false;
    }
}
