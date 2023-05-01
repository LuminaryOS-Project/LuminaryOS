package com.luminary.os.utils.async;

interface Handler<I, O> {
    O process(I input);
}
