package me.driftay.core.utils;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/15/2020
 */
public class Placeholder {

    private String key, value;

    public Placeholder(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

}
