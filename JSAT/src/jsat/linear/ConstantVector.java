package jsat.linear;

/**
 * This class provides a simple utility to represent an immutable vector where 
 * all values in the vector must have the same constant value. The standard 
 * mutable methods can not alter this vector. However, the length and constant 
 * value can be altered by calling the {@link #setLength(int) } and 
 * {@link #setConstant(double) } values respectively. The Constant Vector 
 * representation uses only constant space. 
 * <br><br>
 * This class can be useful in providing a generalized way to handle multiple 
 * unique values or a constant value. For example, a separate regularization 
 * constant could be used for every feature in a learning problem. Instead of 
 * writing code to handle multiple values separately from a single value, a 
 * ConstantVector can be used so that the constant value case can be an 
 * efficient call to the multiple value version of the code. 
 * 
 * @author Edward Raff
 */
public class ConstantVector extends Vec
{
    private double constant;
    private int length;

    /**
     * Creates a new vector where all values have a single implicit value
     * @param constant the constant to use as the single value for all indices
     * @param length the length of this vector
     */
    public ConstantVector(double constant, int length)
    {
        this.constant = constant;
        this.length = length;
    }

    /**
     * Sets the constant value that will be used as the value stored in every 
     * index of this vector. 
     * @param constant the constant value to represent as a vector
     */
    public void setConstant(double constant)
    {
        this.constant = constant;
    }

    /**
     * Sets the length of this vector. The length must be a non zero value
     * @param length the new length for this vector
     */
    public void setLength(int length)
    {
        if(length < 1)
            throw new ArithmeticException("Vector length must be a positive constant");
        this.length = length;
    }

    @Override
    public int length()
    {
        return length;
    }

    @Override
    public double get(int index)
    {
        return constant;
    }

    @Override
    public void set(int index, double val)
    {
        throw new ArithmeticException("ConstantVector does not support mutation");
    }

    @Override
    public boolean isSparse()
    {
        return false;
    }

    @Override
    public Vec clone()
    {
        return new ConstantVector(constant, length);
    }
    
    
}