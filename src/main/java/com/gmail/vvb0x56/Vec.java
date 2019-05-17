package com.gmail.vvb0x56;

public class Vec {
    public int r;
    public int c;

    Vec(int r, int c) {
        this.r = r;
        this.c = c;
    }

    Vec(Vec v) {
        this.r = v.r;
        this.c = v.c;
    }

    Vec() {
        this.r = 0;
        this.c = 0;
    }
}
