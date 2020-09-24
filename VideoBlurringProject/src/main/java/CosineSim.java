
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.MathArrays;

import static org.apache.commons.math3.util.MathArrays.safeNorm;

public class CosineSim implements DistanceMeasure{

        /** Serializable version identifier. */
        private static final long serialVersionUID = 1717556319784040041L;

        /** {@inheritDoc} */
        public double compute(double[] a, double[] b) {
            return 1- ( MathArrays.linearCombination(a, b) / (safeNorm(a) * safeNorm(b)));
        }

}

