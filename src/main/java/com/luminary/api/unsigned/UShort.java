package com.luminary.api.unsigned;

import java.math.BigInteger;

/**
 * The <code>unsigned short</code> type
 */
public final class UShort extends UNumber implements Comparable<UShort> {

    /**
     * A constant holding the minimum value an <code>unsigned short</code> can
     * have, 0.
     */
    public static final int   MIN_VALUE        = 0x0000;

    /**
     * A constant holding the maximum value an <code>unsigned short</code> can
     * have, 2<sup>16</sup>-1.
     */
    public static final int   MAX_VALUE        = 0xffff;

    /**
     * A constant holding the minimum value an <code>unsigned short</code> can
     * have as UShort, 0.
     */
    public static final UShort MIN             = valueOf(MIN_VALUE);

    /**
     * A constant holding the maximum value an <code>unsigned short</code> can
     * have as UShort, 2<sup>16</sup>-1.
     */
    public static final UShort MAX             = valueOf(MAX_VALUE);

    /**
     * The value modelling the content of this <code>unsigned short</code>
     */
    private final int         value;

    /**
     * Create an <code>unsigned short</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned short</code>.
     */
    public static UShort valueOf(String value) throws NumberFormatException {
        return new UShort(value);
    }

    /**
     * Create an <code>unsigned short</code> by masking it with
     * <code>0xFFFF</code> i.e. <code>(short) -1</code> becomes
     * <code>(ushort) 65535</code>
     */
    public static UShort valueOf(short value) {
        return new UShort(value);
    }

    /**
     * Create an <code>unsigned short</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned short</code>
     */
    public static UShort valueOf(int value) throws NumberFormatException {
        return new UShort(value);
    }

    /**
     * Create an <code>unsigned short</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned short</code>
     */
    private UShort(int value) throws NumberFormatException {
        this.value = value;
        rangeCheck();
    }

    /**
     * Create an <code>unsigned short</code> by masking it with
     * <code>0xFFFF</code> i.e. <code>(short) -1</code> becomes
     * <code>(ushort) 65535</code>
     */
    private UShort(short value) {
        this.value = value & MAX_VALUE;
    }

    /**
     * Create an <code>unsigned short</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned short</code>.
     */
    private UShort(String value) throws NumberFormatException {
        this.value = Integer.parseInt(value);
        rangeCheck();
    }

    private void rangeCheck() throws NumberFormatException {
        if (value < MIN_VALUE || value > MAX_VALUE)
            throw new NumberFormatException("Value is out of range : " + value);
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public BigInteger toBigInteger() {
        return BigInteger.valueOf(value);
    }

    @Override
    public int hashCode() {
        /* [java-8] */
        return Integer.hashCode(value);
        /* [/java-8] */
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UShort)
            return value == ((UShort) obj).value;

        return false;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public int compareTo(UShort o) {
        return (Integer.compare(value, o.value));
    }

    public UShort add(UShort val) throws NumberFormatException {
        return valueOf(value + val.value);
    }

    public UShort add(int val) throws NumberFormatException {
        return valueOf(value + val);
    }

    public UShort subtract(final UShort val) {
        return valueOf(value - val.value);
    }

    public UShort subtract(final int val) {
        return valueOf(value - val);
    }
}
