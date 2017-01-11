/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.geom;

import java.awt.geom.AffineTransform;
import java.util.Arrays;

/**
 *
 * @author Marc
 */
public final class Matrix33
{
    final double[] m;
    
    public Matrix33()
    {
        m = new double[9];
    }
    
    public Matrix33(Matrix33 m)
    {
        this.m = Arrays.copyOf(m.m,9);
    }
    
    public Matrix33(double _11, double _12, double _13,
                    double _21, double _22, double _23,
                    double _31, double _32, double _33)
    {
        m = new double[]
        { _11, _12, _13,
          _21, _22, _23,
          _31, _32, _33 };
    }
    
    public final void clear()
    {
        m[0] = m[1] = m[2] = m[3] = m[4] = m[5] = m[6] = m[7] = m[8] = 0f;
    }
    
    public final void setIdentity()
    {
        m[1] = m[2] = m[3] = m[5] = m[6] = m[7] = 0f;
        m[0] = m[4] = m[8] = 1f;
    }
    
    public static final Matrix33 identity()
    {
        Matrix33 m = new Matrix33();
        m.setIdentity();
        return m;
    }
    
    public final Matrix33 localTranspose()
    {
        double f;
        f = m[3]; m[3] = m[1]; m[1] = f;
        f = m[6]; m[6] = m[2]; m[2] = f;
        f = m[7]; m[7] = m[5]; m[5] = f;
        return this;
    }
    
    public final Matrix33 multiply(Matrix33 om)
    {
        Matrix33 ret = new Matrix33();
        ret.m[0] = m[0] * om.m[0] + m[1] * om.m[3] + m[2] * om.m[6];
        ret.m[1] = m[0] * om.m[1] + m[1] * om.m[4] + m[2] * om.m[7];
        ret.m[2] = m[0] * om.m[2] + m[1] * om.m[5] + m[2] * om.m[8];
        
        ret.m[3] = m[3] * om.m[0] + m[4] * om.m[3] + m[5] * om.m[6];
        ret.m[4] = m[3] * om.m[1] + m[4] * om.m[4] + m[5] * om.m[7];
        ret.m[5] = m[3] * om.m[2] + m[4] * om.m[5] + m[5] * om.m[8];
        
        ret.m[6] = m[6] * om.m[0] + m[7] * om.m[3] + m[8] * om.m[6];
        ret.m[7] = m[6] * om.m[1] + m[7] * om.m[4] + m[8] * om.m[7];
        ret.m[8] = m[6] * om.m[2] + m[7] * om.m[5] + m[8] * om.m[8];
        return ret;
    }
    
    public final Matrix33 localMultiply(Matrix33 om)
    {
        Matrix33 t = new Matrix33(this);
        m[0] = t.m[0] * om.m[0] + t.m[1] * om.m[3] + t.m[2] * om.m[6];
        m[1] = t.m[0] * om.m[1] + t.m[1] * om.m[4] + t.m[2] * om.m[7];
        m[2] = t.m[0] * om.m[2] + t.m[1] * om.m[5] + t.m[2] * om.m[8];
        
        m[3] = t.m[3] * om.m[0] + t.m[4] * om.m[3] + t.m[5] * om.m[6];
        m[4] = t.m[3] * om.m[1] + t.m[4] * om.m[4] + t.m[5] * om.m[7];
        m[5] = t.m[3] * om.m[2] + t.m[4] * om.m[5] + t.m[5] * om.m[8];
        
        m[6] = t.m[6] * om.m[0] + t.m[7] * om.m[3] + t.m[8] * om.m[6];
        m[7] = t.m[6] * om.m[1] + t.m[7] * om.m[4] + t.m[8] * om.m[7];
        m[8] = t.m[6] * om.m[2] + t.m[7] * om.m[5] + t.m[8] * om.m[8];
        return this;
    }
    
    public static final Matrix33 storedMultiply(Matrix33 m1, Matrix33 m2, Matrix33 storeInto)
    {
        Matrix33 t = new Matrix33(m1);
        Matrix33 om = new Matrix33(m2);
        storeInto.m[0] = t.m[0] * om.m[0] + t.m[1] * om.m[3] + t.m[2] * om.m[6];
        storeInto.m[1] = t.m[0] * om.m[1] + t.m[1] * om.m[4] + t.m[2] * om.m[7];
        storeInto.m[2] = t.m[0] * om.m[2] + t.m[1] * om.m[5] + t.m[2] * om.m[8];
        
        storeInto.m[3] = t.m[3] * om.m[0] + t.m[4] * om.m[3] + t.m[5] * om.m[6];
        storeInto.m[4] = t.m[3] * om.m[1] + t.m[4] * om.m[4] + t.m[5] * om.m[7];
        storeInto.m[5] = t.m[3] * om.m[2] + t.m[4] * om.m[5] + t.m[5] * om.m[8];
        
        storeInto.m[6] = t.m[6] * om.m[0] + t.m[7] * om.m[3] + t.m[8] * om.m[6];
        storeInto.m[7] = t.m[6] * om.m[1] + t.m[7] * om.m[4] + t.m[8] * om.m[7];
        storeInto.m[8] = t.m[6] * om.m[2] + t.m[7] * om.m[5] + t.m[8] * om.m[8];
        return storeInto;
    }
    
    public final Matrix33 transpose()
    {
        return new Matrix33(this).localTranspose();
    }
    
    public final void setRotate(double rad_angle)
    {
        clear();
        m[0] = StrictMath.cos(rad_angle);
        m[1] = StrictMath.sin(rad_angle);
        m[3] = -StrictMath.sin(rad_angle);
        m[4] = StrictMath.cos(rad_angle);
        m[8] = 1f;
    }
    
    public final void setTraslation(double x, double y)
    {
        setIdentity();
        m[6] = x;
        m[7] = y;
    }
    
    public final void setScale(double x, double y)
    {
        setIdentity();
        m[0] = x;
        m[4] = y;
    }
    
    public final Matrix33 rotate(double rad_angle)
    {
        Matrix33 r = new Matrix33();
        r.setRotate(rad_angle);
        return localMultiply(r);
    }
    public static final Matrix33 rotate(Matrix33 src, double rad_angle)
    {
        Matrix33 r = new Matrix33();
        r.setRotate(rad_angle);
        return src.multiply(r);
    }
    
    public final Matrix33 traslate(double x, double y)
    {
        Matrix33 t = new Matrix33();
        t.setTraslation(x,y);
        return localMultiply(t);
    }
    public static final Matrix33 traslate(Matrix33 src, double x, double y)
    {
        Matrix33 t = new Matrix33();
        t.setTraslation(x,y);
        return src.multiply(t);
    }
    
    public final Matrix33 scale(double x, double y)
    {
        Matrix33 s = new Matrix33();
        s.setScale(x,y);
        return localMultiply(s);
    }
    public static final Matrix33 scale(Matrix33 src, double x, double y)
    {
        Matrix33 s = new Matrix33();
        s.setScale(x,y);
        return src.multiply(s);
    }
    
    public static final Matrix33 toRotation(double rad_angle)
    {
        return new Matrix33().rotate(rad_angle);
    }
    
    public static final Matrix33 toTraslation(double x, double y)
    {
        return new Matrix33().traslate(x,y);
    }
    
    public static final Matrix33 toScale(double x, double y)
    {
        return new Matrix33().scale(x,y);
    }
    
    public final Matrix33 rotateLocal(double rad_angle)
    {
        Matrix33 r = new Matrix33();
        r.setRotate(rad_angle);
        return storedMultiply(r,this,this);
    }
    public static final Matrix33 rotateLocal(Matrix33 src, double rad_angle)
    {
        Matrix33 r = new Matrix33();
        r.setRotate(rad_angle);
        return r.multiply(src);
    }
    
    public final Matrix33 traslateLocal(double x, double y)
    {
        Matrix33 t = new Matrix33();
        t.setTraslation(x,y);
        return storedMultiply(t,this,this);
    }
    public static final Matrix33 traslateLocal(Matrix33 src, double x, double y)
    {
        Matrix33 t = new Matrix33();
        t.setTraslation(x,y);
        return t.multiply(src);
    }
    
    public static final Matrix33 toRotateLocal(double rad_angle)
    {
        return new Matrix33().rotateLocal(rad_angle);
    }
    
    public static final Matrix33 toTraslateLocal(double x, double y)
    {
        return new Matrix33().traslateLocal(x,y);
    }
    
    public static final Matrix33 newRotation(double rad_angle)
    {
        Matrix33 r = new Matrix33();
        r.setRotate(rad_angle);
        return r;
    }
    
    public static final Matrix33 newTraslation(double x, double y)
    {
        Matrix33 t = new Matrix33();
        t.setTraslation(x,y);
        return t;
    }
    
    public static final Matrix33 newScale(double x, double y)
    {
        Matrix33 s = new Matrix33();
        s.setScale(x,y);
        return s;
    }
    
    public final Vector2 getTraslation()
    {
        return new Vector2(m[6],m[7]);
    }
    public final double getTraslationX() { return m[6]; }
    public final double getTraslationY() { return m[7]; }
    
    public final Vector2 getScale()
    {
        return new Vector2(m[2],m[5]);
    }
    public final double getScaleX() { return m[2]; }
    public final double getScaleY() { return m[5]; }
    
    public final AffineTransform toAffineTransform()
    {
        return new AffineTransform(m[0],m[1],m[3],m[4],m[6],m[7]);
    }
    
    /*public final Block encodeToRB(String name)
    {
        Block base = new Block(name);
        base.addAtributeList("m",m);
        return base;
    }*/
    /*public final DataFloatArray encodeToRB()
    {
        return DataArray.valueOf(m);
    }*/
    
    /*public static final Matrix33 decodeFromRB(Block base)
    {
        List<? extends Data<?>> list = base.getAttributeList("m");
        Matrix33 m = new Matrix33();
        int i = 0;
        for(Data<?> d : list)
            m.m[i++] = d.getCastFloat();
        return m;
    }*/
    /*public static final Matrix33 decodeFromRB(DataFloatArray base)
    {
        Matrix33 m = new Matrix33();
        base.copyTo(m.m,0,0,m.m.length);
        return m;
    }*/
}
