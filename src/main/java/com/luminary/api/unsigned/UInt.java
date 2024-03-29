package com.luminary.api.unsigned;

import java.io.ObjectStreamException;
import java.io.Serial;
import java.math.BigInteger;

/**
 * The <code>unsigned int</code> type
 */
public final class UInt extends UNumber implements Comparable<UInt> {

    private static final Class<UInt> CLASS                 = UInt.class;
    private static final String          CLASS_NAME            = CLASS.getName();

    /**
     * System property name for the property to set the size of the pre-cache.
     */
    private static final String          PRECACHE_PROPERTY     = CLASS_NAME + ".precacheSize";

    /**
     * Default size for the value cache.
     */
    private static final int             DEFAULT_PRECACHE_SIZE = 256;

    /**
     * Cached values
     */
    private static final UInt[]      VALUES                = mkValues();

    /**
     * A constant holding the minimum value an <code>unsigned int</code> can
     * have, 0.
     */
    public static final long             MIN_VALUE             = 0x00000000;

    /**
     * A constant holding the maximum value an <code>unsigned int</code> can
     * have, 2<sup>32</sup>-1.
     */
    public static final long             MAX_VALUE             = 0xffffffffL;

    /**
     * A constant holding the minimum value an <code>unsigned int</code> can
     * have as UInteger, 0.
     */
    public static final UInt MIN                   = valueOf(MIN_VALUE);

    /**
     * A constant holding the maximum value an <code>unsigned int</code> can
     * have as UInteger, 2<sup>32</sup>-1.
     */
    public static final UInt MAX                   = valueOf(MAX_VALUE);

    /**
     * The value modelling the content of this <code>unsigned int</code>
     */
    private final long                   value;

    /**
     * Figure out the size of the precache.
     *
     * @return The parsed value of the system property
     *         {@link #PRECACHE_PROPERTY} or {@link #DEFAULT_PRECACHE_SIZE} if
     *         the property is not set, not a number or retrieving results in a
     *         {@link SecurityException}. If the parsed value is zero or
     *         negative no cache will be created. If the value is larger than
     *         {@link Integer#MAX_VALUE} then Integer#MAX_VALUE will be used.
     */
    private static int getPrecacheSize() {
        String prop = null;
        long propParsed;

        try {
            prop = System.getProperty(PRECACHE_PROPERTY);
        }
        catch (SecurityException e) {
            // security manager stopped us so use default
            // FIXME: should we log this somewhere?
            return DEFAULT_PRECACHE_SIZE;
        }
        if (prop == null)
            return DEFAULT_PRECACHE_SIZE;

        // empty value
        // FIXME: should we log this somewhere?
        if (prop.length() == 0)
            return DEFAULT_PRECACHE_SIZE;

        try {
            propParsed = Long.parseLong(prop);
        }
        catch (NumberFormatException e) {
            // not a valid number
            // FIXME: should we log this somewhere?
            return DEFAULT_PRECACHE_SIZE;
        }

        // treat negative value as no cache...
        if (propParsed < 0)
            return 0;

        // FIXME: should we log this somewhere?
        if (propParsed > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;

        return (int) propParsed;
    }

    /**
     * Generate a cached value for initial unsigned integer values.
     *
     * @return Array of cached values for UInteger
     */
    private static UInt[] mkValues() {
        int precacheSize = getPrecacheSize();
        UInt[] ret;

        if (precacheSize <= 0)
            return null;

        ret = new UInt[precacheSize];
        for (int i = 0; i < precacheSize; i++)
            ret[i] = new UInt(i);

        return ret;
    }

    /**
     * Unchecked internal constructor. This serves two purposes: first it allows
     * {@link #UInteger(long)} to stay deprecated without warnings and second
     * constructor without unnecessary value checks.
     *
     * @param value The value to wrap
     * @param unused Unused parameter to distinguish between this and the
     *            deprecated public constructor.
     */
    private UInt(long value, boolean unused) {
        this.value = value;
    }

    /**
     * Retrieve a cached value.
     *
     * @param value Cached value to retrieve
     * @return Cached value if one exists. Null otherwise.
     */
    private static UInt getCached(long value) {
        if (VALUES != null && value < VALUES.length)
            return VALUES[(int) value];

        return null;
    }

    /**
     * Get the value of a long without checking the value.
     */
    private static UInt valueOfUnchecked(long value) {
        UInt cached;

        if ((cached = getCached(value)) != null)
            return cached;

        return new UInt(value, true);
    }

    /**
     * Create an <code>unsigned int</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned int</code>.
     */
    public static UInt valueOf(String value) throws NumberFormatException {
        return valueOfUnchecked(rangeCheck(Long.parseLong(value)));
    }

    /**
     * Create an <code>unsigned int</code> by masking it with
     * <code>0xFFFFFFFF</code> i.e. <code>(int) -1</code> becomes
     * <code>(uint) 4294967295</code>
     */
    public static UInt valueOf(int value) {
        return valueOfUnchecked(value & MAX_VALUE);
    }

    /**
     * Create an <code>unsigned int</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned byte</code>
     */
    public static UInt valueOf(long value) throws NumberFormatException {
        return valueOfUnchecked(rangeCheck(value));
    }

    /**
     * Create an <code>unsigned int</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned int</code>
     */
    private UInt(long value) throws NumberFormatException {
        this.value = rangeCheck(value);
    }

    /**
     * Create an <code>unsigned int</code> by masking it with
     * <code>0xFFFFFFFF</code> i.e. <code>(int) -1</code> becomes
     * <code>(uint) 4294967295</code>
     */
    private UInt(int value) {
        this.value = value & MAX_VALUE;
    }

    /**
     * Create an <code>unsigned int</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned int</code>.
     */
    private UInt(String value) throws NumberFormatException {
        this.value = rangeCheck(Long.parseLong(value));
    }

    /**
     * Throw exception if value out of range (long version)
     *
     * @param value Value to check
     * @return value if it is in range
     * @throws NumberFormatException if value is out of range
     */
    private static long rangeCheck(long value) throws NumberFormatException {
        if (value < MIN_VALUE || value > MAX_VALUE)
            throw new NumberFormatException("Value is out of range : " + value);

        return value;
    }

    /**
     * Replace version read through deserialization with cached version.
     *
     * @return cached instance of this object's value if one exists, otherwise
     *         this object
     * @throws ObjectStreamException
     */
    @Serial
    private Object readResolve() throws ObjectStreamException {
        UInt cached;

        // the value read could be invalid so check it
        rangeCheck(value);
        if ((cached = getCached(value)) != null)
            return cached;

        return this;
    }

    @Override
    public int intValue() {
        return (int) value;
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
        return Long.hashCode(value);
        /* [/java-8] */
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof UInt)
            return value == ((UInt) obj).value;

        return false;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    @Override
    public int compareTo(UInt o) {
        return (Long.compare(value, o.value));
    }

    public UInt add(final UInt val) {
        return valueOf(value + val.value);
    }

    public UInt add(final int val) {
        return valueOf(value + val);
    }

    public UInt subtract(final UInt val) {
        return valueOf(value - val.value);
    }

    public UInt subtract(final int val) {
        return valueOf(value - val);
    }
}
