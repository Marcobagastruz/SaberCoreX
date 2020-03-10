package me.driftay.core.utils;

import java.text.DecimalFormat;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public enum DecimalFormatType {
    MONEY(new DecimalFormat("#,###.##")),
    SECONDS(new DecimalFormat("#.#")),
    LOCATION(new DecimalFormat("#.##"));

    private DecimalFormat format;

    DecimalFormatType(DecimalFormat format) {
        this.format = format;
    }

    public String format(Number value) {
        return this.format.format(value);
    }

    public DecimalFormat getFormat() {
        return this.format;
    }
}
