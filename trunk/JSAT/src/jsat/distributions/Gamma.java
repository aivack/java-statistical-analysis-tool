
package jsat.distributions;

import jsat.linear.Vec;
import static java.lang.Math.*;
import static jsat.math.SpecialMath.*;

/**
 *
 * @author Edward Raff
 */
public class Gamma extends ContinousDistribution
{
    private double k;
    private double theta;

    public Gamma(double k, double theta)
    {
        this.k = k;
        this.theta = theta;
    }
    
    @Override
    public double pdf(double x)
    {
        if(x < 0)
            return 0;
        
        return exp(logPdf(x));
    }

    @Override
    public double logPdf(double x)
    {
        /*
         *  k - 1    / -x  \
         * x      exp|-----|
         *           \theat/
         * -----------------
         *                k
         *  Gamma(k) theta
         */
        
        double p1 = -k *log(theta);
        double p2 = k*log(x);
        double p3 = -lnGamma(k);
        double p4 = -x/theta;
        double p5 = -log(x);
        
        return p1+p2+p3+p4+p5;
    }

    @Override
    public double cdf(double x)
    {
        if(x < 0)
            throw new ArithmeticException("CDF goes from 0 to Infinity, " + x + " is invalid");
        return gammaP(k, x/theta);
    }

    @Override
    public double invCdf(double p)
    {
        return invGammaP(p, k)*theta;
    }

    @Override
    public double min()
    {
        return 0;
    }

    @Override
    public double max()
    {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public String getDistributionName()
    {
        return "Gamma";
    }

    @Override
    public String[] getVariables()
    {
        return new String[] {"k", "theta"};
    }

    @Override
    public double[] getCurrentVariableValues()
    {
        return new double[] {k, theta};
    }

    @Override
    public void setVariable(String var, double value)
    {
        if(var.equals("k"))
            k = value;
        else if(var.equals("theta"))
            theta = value;
    }

    @Override
    public ContinousDistribution copy()
    {
        return new Gamma(k, theta);
    }

    @Override
    public void setUsingData(Vec data)
    {
        /*
         * Using:
         * mean = k*theat
         * variance = k*theta^2
         * 
         * k*theta^2 / (k*theta) = theta^2/theta = theta = mean/variance
         * 
         */
        theta = data.variance()/data.mean();
        k = data.mean()/theta;
    }

    @Override
    public double mean()
    {
        return k * theta;
    }

    @Override
    public double median()
    {
        return invGammaP(k, 0.5)*theta;
    }

    @Override
    public double mode()
    {
        if(k < 1)
            throw new ArithmeticException("No mode for k < 1");
        return (k-1)*theta;
    }

    @Override
    public double variance()
    {
        return k * theta*theta;
    }

    @Override
    public double skewness()
    {
        return 2 / sqrt(k);
    }
    
}
