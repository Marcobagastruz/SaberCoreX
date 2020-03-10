package me.driftay.core.utils;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class LongHash {

    public static long toLong(int msw, int lsw) {
        return ((long) msw << 32) + (long) lsw - -2147483648L;
    }

    public static int msw(long l) {
        return (int) (l >> 32);
    }

    public static int lsw(long l) {
        return (int) l + -2147483648;
    }
}
