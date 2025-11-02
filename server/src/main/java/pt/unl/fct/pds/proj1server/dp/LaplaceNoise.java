package pt.unl.fct.pds.proj1server.dp;

import java.util.Random;

public class LaplaceNoise {
    private static final Random random = new Random();

    public static double addNoise(double trueValue, double sensitivity, double epsilon) {
        double scale = sensitivity / epsilon;
        double u = random.nextDouble() - 0.5;
        double noise = -scale * Math.signum(u) * Math.log(1 - 2 * Math.abs(u));
        return trueValue + noise;
    }
}
