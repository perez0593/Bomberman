/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.util;

import java.util.Random;

/**
 *
 * @author Asus
 */
public final class RNG
{
    private final Random rand;
    private long seed;
    
    public RNG(long seed)
    {
        rand = new Random(this.seed = seed);
    }
    public RNG() { this(new Random().nextLong()); }
    
    public final void setSeed(long seed)
    {
        this.seed = seed;
        rand.setSeed(seed);
    }
    public final long getSeed() { return seed; }
    
    public final int getInt() { return rand.nextInt(); }
    public final int getInt(int limit) { return rand.nextInt(limit); }
    public final int getInt(int min, int max) { return rand.nextInt(max - min) + min; }
    
    public final long getLong() { return rand.nextLong(); }
    public final long getLong(long limit) { return rand.nextLong() % limit; }
    public final long getLong(long min, long max) { return (rand.nextLong() % (max - min)) + min; }
    
    public final float getFloat() { return rand.nextFloat(); }
    public final float getFloat(float max) { return rand.nextFloat() * max; }
    public final float getFloat(float min, float max) { return rand.nextFloat() * (max - min) + min; }
    
    public final double getDouble() { return rand.nextDouble(); }
    public final double getDouble(double max) { return rand.nextDouble() * max; }
    public final double getDouble(double min, double max) { return rand.nextDouble() * (max - min) + min; }
    
    public final int d6() { return rand.nextInt(6); }
    public final int d10() { return rand.nextInt(10); }
    public final int d20() { return rand.nextInt(20); }
    public final int d50() { return rand.nextInt(50); }
    public final int d100() { return rand.nextInt(100); }
    public final int d200() { return rand.nextInt(200); }
    public final int d256() { return rand.nextInt(256); }
    public final int d500() { return rand.nextInt(500); }
    public final int d512() { return rand.nextInt(512); }
    public final int d1000() { return rand.nextInt(1000); }
    public final int d1024() { return rand.nextInt(1024); }
    public final int d(int sides) { return rand.nextInt(sides); }
    
    public final boolean d6(int prob) { return d6() < prob; }
    public final boolean d10(int prob) { return d10() < prob; }
    public final boolean d20(int prob) { return d20() < prob; }
    public final boolean d50(int prob) { return d50() < prob; }
    public final boolean d100(int prob) { return d100() < prob; }
    public final boolean d200(int prob) { return d200() < prob; }
    public final boolean d256(int prob) { return d256() < prob; }
    public final boolean d500(int prob) { return d500() < prob; }
    public final boolean d512(int prob) { return d512() < prob; }
    public final boolean d1000(int prob) { return d1000() < prob; }
    public final boolean d1024(int prob) { return d1024() < prob; }
    public final boolean d(int sides, int prob) { return d(sides) < prob; }
}
