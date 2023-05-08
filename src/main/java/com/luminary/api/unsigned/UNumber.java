package com.luminary.api.unsigned;

import java.math.BigInteger;
public abstract class UNumber extends Number {
    public BigInteger toBigInteger() {
        return new BigInteger(toString());
    }
}