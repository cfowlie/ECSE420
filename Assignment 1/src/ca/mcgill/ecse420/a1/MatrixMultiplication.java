package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatrixMultiplication {
   
   private static final int NUMBER_THREADS = Runtime.getRuntime().availableProcessors();
   private static final int THRESHOLD = 64;
   private static final int MATRIX_SIZE = 5;
   private final ExecutorService exec = Executors.newFixedThreadPool(NUMBER_THREADS);
    public static void main(String[] args) {
      
      // Generate two random matrices, same size
      double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
      double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
      sequentialMultiplyMatrix(a,b);
//    parallelMultiplyMatrix(a, b);
   }
   /**
    * Returns the result of a sequential matrix multiplication
    * The two matrices are randomly generated
    * @param a is the first matrix
    * @param b is the second matrix
    * @return the result of the multiplication
    * */
   public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
      double[][] resultMatrix = new double[MATRIX_SIZE][MATRIX_SIZE];
      for (int i=0;i<MATRIX_SIZE;i++){
         for (int j=0;j<MATRIX_SIZE;j++){            
            resultMatrix[i][j]=0;
            for (int k=0;k<MATRIX_SIZE;k++){
               resultMatrix[i][j]+=a[i][k]*b[k][j];
            }
         }
      }
   
      return resultMatrix;
   }
   
   /**
    * Returns the result of a concurrent matrix multiplication
    * The two matrices are randomly generated
    * @param a is the first matrix
    * @param b is the second matrix
    * @return the result of the multiplication
    * */
         public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {

  }

        
        /**
         * Populates a matrix of given size with randomly generated integers between 0-10.
         * @param numRows number of rows
         * @param numCols number of cols
         * @return matrix
         */
        private static double[][] generateRandomMatrix (int numRows, int numCols) {
             double matrix[][] = new double[numRows][numCols];
        for (int row = 0 ; row < numRows ; row++ ) {
            for (int col = 0 ; col < numCols ; col++ ) {
                matrix[row][col] = (double) ((int) (Math.random() * 10.0));
            }
        }
        return matrix;
    }
   
}

