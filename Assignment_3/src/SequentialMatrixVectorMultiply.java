
public class SequentialMatrixVectorMultiply {

    public static double[] sequentialMultiply(double[][] matrix, double[] vector, int matrix_size) {
        double[] result = new double[matrix_size];
        for (int i=0;i<matrix_size;i++){
            result[i] = 0;
            for (int j=0;j<matrix_size;j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;

    }
}
