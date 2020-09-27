public class TestClass {
    public static void main(String[] args) {
        int[][] pixels = {
                {3,3,3,3},
                {3,3,3,3},
                {3,3,3,3},
                {3,3,3,3}
        };

        double[][] compressedImage = new double[3][3];

        double scale = pixels.length / (double)compressedImage.length;
        System.out.println("Scale: " + scale);

        for (int[] pixelRow: pixels){
            for (int pixel: pixelRow){
                System.out.print(pixel + " ");
            }
            System.out.println();
        }

        System.out.println();

        for (double[] pixelRow: compressedImage){
            for (double pixel: pixelRow){
                System.out.print(pixel + " ");
            }
            System.out.println();
        }
        System.out.println();

        for (int y = 0; y < pixels[0].length; y++) {
            for (int x = 0; x < pixels.length; x++) {
                System.out.println(x + " " + y + " -> " + divMod(x, scale)+ " " + divMod(y, scale));
                System.out.println("Factor: " + (1.0 - x % scale) * (1.0 - y % scale));
                double newValue = pixels[x][y] * (1.0 - x % scale) * (1.0 - y % scale);
                compressedImage[divMod(x, scale)][divMod(y, scale)] += newValue;
            }
        }
        System.out.println();
        for (double[] pixelRow: compressedImage){
            for (double pixel: pixelRow){
                System.out.print(roundToNDecimal(pixel, 3) + " ");
            }
            System.out.println();
        }
    }

    public static double roundToNDecimal(double value, int n) {
        return (double)Math.round(value * Math.pow(10, n)) / Math.pow(10, n);
    }

    public static int divMod(int number, double divisor) {
        int timesDivided = 0;

        if (divisor == 0) {
            return 0;
        }

        for (double i = 0; i < number; i += divisor) {
            if (i + divisor <= number) {
                timesDivided++;
            }
        }
        return timesDivided;
    }
}
