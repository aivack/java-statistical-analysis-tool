package jsat.datatransform;

import jsat.DataSet;
import jsat.classifiers.DataPoint;
import jsat.linear.*;

/**
 * An extension of {@link WhitenedPCA}, is the Whitened Zero Component Analysis.
 * Whitened ZCA can not project to a lower dimension, as it rotates the output 
 * in the original dimension.
 * 
 * @author Edward Raff
 */
public class WhitenedZCA extends WhitenedPCA implements InPlaceTransform
{
    private final ThreadLocal<Vec> tempVecs;
    
    /**
     * Creates a new Whitened ZCA.
     * 
     * @param dataSet the data set to whiten
     * @param regularization the amount of regularization to add, avoids 
     * numerical instability
     */
    public WhitenedZCA(DataSet dataSet, double regularization)
    {
        super(dataSet, regularization);
        tempVecs = getThreadLocal(dataSet.getNumNumericalVars());
    }
    
    /**
     * Creates a new Whitened ZCA. The regularization parameter will be
     * chosen as the log of the condition of the covariance. 
     * 
     * @param dataSet the data set to whiten
     */
    public WhitenedZCA(DataSet dataSet)
    {
        super(dataSet);
        tempVecs = getThreadLocal(dataSet.getNumNumericalVars());
    }

    @Override
    public void mutableTransform(DataPoint dp)
    {
        Vec target = tempVecs.get();
        target.zeroOut();
        transform.multiply(dp.getNumericalValues(), 1.0, target);
        target.copyTo(dp.getNumericalValues());
    }

    @Override
    public boolean mutatesNominal()
    {
        return false;
    }

    @Override
    protected void setUpTransform(SingularValueDecomposition svd)
    {
        double[] s = svd.getSingularValues();
        Vec diag = new DenseVector(s.length);

        for(int i = 0; i < s.length; i++)
            diag.set(i, 1.0/Math.sqrt(s[i]+regularization));
        
        Matrix U = svd.getU();
        
        transform = U.multiply(Matrix.diag(diag)).multiply(U.transpose());
    }

    private ThreadLocal<Vec> getThreadLocal(final int dim)
    {
        return new ThreadLocal<Vec>()
        {

            @Override
            protected Vec initialValue()
            {
                return new DenseVector(dim);
            }
        };
    }
    
    static public class WhitenedZCATransformFactory implements DataTransformFactory
    {
        private Double reg;

        /**
         * Creates a new WhitenedZCA factory 
         * @param reg the regularization to use
         */
        public WhitenedZCATransformFactory(double reg)
        {
            this.reg = reg;
        }

        /**
         * Creates a new WhitenedZCA
         */
        public WhitenedZCATransformFactory()
        {
            reg = null;
        }
        
        @Override
        public DataTransform getTransform(DataSet dataset)
        {
            if(reg == null)
                return new WhitenedZCA(dataset);
            return new WhitenedZCA(dataset, reg);
        }
        
    }
}
