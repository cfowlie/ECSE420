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
//         public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
//             double[][] resultMatrix = new double[MATRIX_SIZE][MATRIX_SIZE];
//             MultiplyMatrices test = new MultiplyMatrices(a,b,resultMatrix,0,0,0,0,0,0,MATRIX_SIZE);
//             test.split();
//             exec.shutdown();
//  }
//  private class Control implements Runnable{
//             private final MultiplyMatrices t1;
//             private final MultiplyMatrices t2;
//
//             Control(MultiplyMatrices t1, MultiplyMatrices t2, int length){
//                this.t1 = t1;
//                this.t2 = t2;
//
//                if(THRESHOLD>=length){
//                   exec.submit(this);
//          }else{
//                   t1.split();
//                   t2.split();
//          }
//       }
//
//       public void run(){
//                t1.multiply2();
//                t2.multiply2();
//       }
// }
//  private class MultiplyMatrices{
//             private final double[][] a;
//             private final double[][] b;
//       private final double[][] c;
//
//             private final int rowA;
//       private final int rowB;
//       private final int rowC;
//
//       private final int colA;
//       private final int colB;
//       private final int colC;
//
//       private final int length;
//
//       MultiplyMatrices(double[][] a, double[][] b, double[][] c, int rowA, int rowB, int rowC, int colA, int colB
//       , int colC, int length){
//          this.a = a;
//          this.b = b;
//          this.c = c;
//
//          this.rowA = rowA;
//          this.rowB = rowB;
//          this.rowC = rowC;
//
//          this.colA = colA;
//          this.colB = colB;
//          this.colC = colC;
//
//          this.length = length;
//       }
//       public void split(){
//          int tmpLength = length/2;
//
//          new Control(new MultiplyMatrices(a,b,c,rowA,rowB,rowC,colA,colB,colC,tmpLength),
//                new MultiplyMatrices(a,b,c,rowA,rowB+tmpLength,rowC,colA+tmpLength,colB,colC,tmpLength)
//                ,tmpLength);
//          new Control(new MultiplyMatrices(a,b,c,rowA,rowB,rowC,colA,colB+tmpLength,colC+tmpLength,tmpLength),
//                new MultiplyMatrices(a,b,c,rowA,rowB+tmpLength,rowC,colA+tmpLength,colB+tmpLength,
//                      colC+tmpLength,tmpLength),tmpLength);
//
//          new Control(new MultiplyMatrices(a,b,c,rowA+tmpLength,rowB,rowC+tmpLength,colA,colB,colC,tmpLength),
//                new MultiplyMatrices(a,b,c,rowA+tmpLength,rowB+tmpLength,rowC+tmpLength,
//                      colA+tmpLength,colB, colC,tmpLength),tmpLength);
//
//          new Control(new MultiplyMatrices(a,b,c,rowA+tmpLength,rowB,rowC+tmpLength,colA,colB+tmpLength,
//                colC+tmpLength,tmpLength),
//                new MultiplyMatrices(a,b,c,rowA+tmpLength,rowB+tmpLength,rowC+tmpLength,
//                      colA+tmpLength,colB+tmpLength,
//                      colC+tmpLength,tmpLength),tmpLength);
//       }
//       public void multiply2(){
//          for (int i=0;i<length;i+=2){
//             for (int j=0;j<length;j+=2){
//                double[] a0=a[rowA + j];
//                double[] a1=a[rowA + j + 1];
//
//                double tmp00 = 0.0;
//                double tmp01 = 0.0;
//                double tmp10 = 0.0;
//                double tmp11 = 0.0;
//
//                for (int l=0;l<length;l+=2){
//                   double[] b0 = b[rowB + l];
//
//                   tmp00 += a0[colA + l] * b0[colB + l];
//                   tmp10 += a1[colA + l] * b0[colB + l];
//                   tmp01 += a0[colA + l] * b0[colB + i + 1];
//                   tmp11 += a1[colA + l] * b0[colB + i + 1];
//
//                   double[] b1 = b[rowB + l + 1];
//
//                   tmp00 += a0[colA + l + 1] * b1[colB + i];
//                   tmp10 += a1[colA + l + 1] * b1[colB + i];
//                   tmp01 += a0[colA + l + 1] * b1[colB + i + 1];
//                   tmp11 += a1[colA + l + 1] * b1[colB + i + 1];
//
//                }
//                c[rowC + j][colC + i] += tmp00;
//                c[rowC + j][colC + i + 1] += tmp01;
//                c[rowC + j + 1][colC + i] += tmp10;
//                c[rowC + j + 1][colC + i + 1] += tmp11;
//             }
//          }
//     }
//  }
        
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

