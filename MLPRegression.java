
public class MLPRegressionAltura {

    private enum Activation { IDENTITY, LOGISTIC, RELU, TANH, SOFTMAX }

    private MLPRegressionAltura.Activation hidden;
    private MLPRegressionAltura.Activation output;
    private double[][] network;
    private double[][][] weights;
    private double[][] bias;
    public double predictTeste;

    public MLPRegressionAltura(String hidden, int[] layers, double[][][] weights, double[][] bias) {
        this.hidden = MLPRegressionAltura.Activation.valueOf(hidden.toUpperCase());

        this.network = new double[layers.length + 1][];
        for (int i = 0, l = layers.length; i < l; i++) {
            this.network[i + 1] = new double[layers[i]];
        }
        this.weights = weights;
        this.bias = bias;
    }

    public MLPRegressionAltura(String hidden,  int neurons, double[][][] weights, double[][] bias) {
        this(hidden, new int[] { neurons }, weights, bias);
    }

    private double[] compute(MLPRegressionAltura.Activation activation, double[] v, int nLayers) {
        switch (activation) {
            case LOGISTIC:
                if (nLayers > 1) {
                    for (int i = 0, l = v.length; i < l; i++) {
                        v[i] = 1. / (1. + Math.exp(-v[i]));
                    }
                } else {
                    for (int i = 0, l = v.length; i < l; i++) {
                        if (v[i] > 0) {
                            v[i] = -Math.log(1. + Math.exp(-v[i]));
                        } else {
                            v[i] = v[i] - Math.log(1. + Math.exp(-v[i]));
                        }
                    }
                }
                break;
            case RELU:
                for (int i = 0, l = v.length; i < l; i++) {
                    v[i] = Math.max(0, v[i]);
                }
                break;
            case TANH:
                for (int i = 0, l = v.length; i < l; i++) {
                    v[i] = Math.tanh(v[i]);
                }
                break;
            case SOFTMAX:
                double max = Double.NEGATIVE_INFINITY;
                for (double x : v) {
                    if (x > max) {
                        max = x;
                    }
                }
                for (int i = 0, l = v.length; i < l; i++) {
                    v[i] = Math.exp(v[i] - max);
                }
                double sum = 0.;
                for (double x : v) {
                    sum += x;
                }
                for (int i = 0, l = v.length; i < l; i++) {
                    v[i] /= sum;
                }
                break;
        }
        return v;
    }

    public double predict(double[] neurons) {
        this.network[0] = neurons;

        for (int i = 0; i < this.network.length - 1; i++) {
            for (int j = 0; j < this.network[i + 1].length; j++) {
                this.network[i + 1][j] = this.bias[i][j];
                for (int l = 0; l < this.network[i].length; l++) {
                    this.network[i + 1][j] += this.network[i][l] * this.weights[i][l][j];
                }
            }
            if ((i + 1) < (this.network.length - 1)) {
                this.network[i + 1] = this.compute(this.hidden, this.network[i + 1], this.network.length);
            }
        }

        //if (this.network[this.network.length - 1].length > 1) {
        // return this.network[this.network.length - 1];
        //}
        return this.network[this.network.length - 1][0];

    }




    public static double main(String[] args) {
        if (args.length == 2) {

            // Features:
            double[] features = new double[args.length];
            for (int i = 0, l = args.length; i < l; i++) {
                features[i] = Double.parseDouble(args[i]);
            }

            // Parameters:
            int[] layers = // Add Your Layers
            double[][][] weights = // Add Your Weights
            double[][] bias = //Add Your Bias
            // Prediction:
            MLPRegressionAltura clf = new MLPRegressionAltura("tanh", layers, weights, bias);
            double estimation = clf.predict(features);
            //System.out.println(estimation);
            return estimation;

        }
        return 0;
    }

}
