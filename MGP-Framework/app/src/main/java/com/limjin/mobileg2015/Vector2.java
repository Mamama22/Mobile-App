package com.limjin.mobileg2015;

/*************************************************************************************
Vector2
 *************************************************************************************/
public class Vector2
{
    public float x;
    public float y;

    public Vector2(){this.x = 0;this.y = 0;}
    public Vector2(float x, float y){this.x = x;this.y = y;}
    public Vector2(Vector2 s){this.x = s.x;this.y = s.y;}

    public void Set(float x, float y){this.x = x; this.y = y;}

    public void Set(Vector2 setTo){this.x = setTo.x; this.y = setTo.y;}

    public float Length()
    {
        return (float)Math.sqrt(x * x + y * y);
    }

    public float LengthSquared()
    {
        return x * x + y * y;
    }

    public float Dot(Vector2 rhs)
    {
        return x * rhs.x + y * rhs.y;
    }

    public boolean IsZero()
    {
        return x == 0.f && y == 0.f;
    }

    public void AddWith(Vector2 rhs)
    {
        x += rhs.x;
        y += rhs.y;
    }

    public void SubtractWith(Vector2 rhs)
    {
        x -= rhs.x;
        y -= rhs.y;
    }

    public void SetZero(){x = y = 0.f;}

    public void MultiplyWith(float val)
    {
        x *= val;
        y *= val;
    }

    public void MultiplyWithVec(Vector2 val)
    {
        x *= val.x;
        y *= val.y;
    }

    public void Copy(Vector2 copyMe)
    {
        x = copyMe.x;
        y = copyMe.y;
    }

    public void Normalized()
    {
        float d = this.Length();
	/*if(d <= Math::EPSILON && -d <= Math::EPSILON)
	  throw DivideByZero();*/
        x /= d;
        y /= d;
    }

    public boolean Same(Vector2 checkMe)
    {
        return Math.abs(x - checkMe.x) == 0 &&
                Math.abs(y - checkMe.y) == 0;
    }

    public boolean Same(Vector2 checkMe, float offset)
    {
        return Math.abs(x - checkMe.x) <= offset &&
                Math.abs(y - checkMe.y) <= offset;
    }

    public void Inverse()
    {
        x = -x;
        y = -y;
    }
}
