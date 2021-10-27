/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.softmedica.smportal.rest;

/**
 *
 * @author Damian Babiński <damian.babinski@softmedica.pl>
 */
public class PermissionsException extends Exception{
    
    public static String FORBIDDEN = "Brak dostępu";
    
    public PermissionsException() {
        super(FORBIDDEN);
    }

    public PermissionsException(String message) {
        super(message);
    }
}
