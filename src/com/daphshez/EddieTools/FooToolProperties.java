package com.daphshez.EddieTools;

import java.nio.file.Path;

public class FooToolProperties
{
    private int n;
    private String s;
    private float f;
    private boolean b;
    private Path p;

    public int getN()
    {
        return n;
    }

    public void setN(int n)
    {
        this.n = n;
    }

    public String getS()
    {
        return s;
    }

    public void setS(String s)
    {
        this.s = s;
    }

    public float getF()
    {
        return f;
    }

    public void setF(float f)
    {
        this.f = f;
    }

    public boolean isB()
    {
        return b;
    }

    public void setB(boolean b)
    {
        this.b = b;
    }

    public Path getP()
    {
        return p;
    }

    public void setP(Path p)
    {
        this.p = p;
    }
}
