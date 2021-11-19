/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.common.utilities;

import java.util.LinkedHashMap;

/**
 *
 * @author chiefu
 */
public class LinkedHashMapBuilder<K, V> {

    private LinkedHashMap<K, V> mapa = new LinkedHashMap<K, V>();

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public LinkedHashMapBuilder() {
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------
    public LinkedHashMap<K, V> build() {
        return mapa;
    }

    public LinkedHashMapBuilder put(K key, V value) {
        this.mapa.put(key, value);
        return this;
    }
}
