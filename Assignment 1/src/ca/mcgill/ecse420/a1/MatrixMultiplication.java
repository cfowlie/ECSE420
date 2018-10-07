package ca.mcgill.ecse420.a1;

import  com.sun.org.apache.xpath.internal.operations.Mult;

import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MatrixMultiplication {
   
   private static final int NUMBER_THREADS = 32;
   private static final int MATRIX_SIZE = 2048 ;
   private static final int THRESHOLD = 64;
   private static final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_THREADS);
    public static void main(String[] args) {
      
      // Generate two random matrices, same size
      double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
      double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);






      System.out.println("Matrix size: " + MATRIX_SIZE +" x " + MATRIX_SIZE);
      System.out.println("Max Threads: " + NUMBER_THREADS);
      measureTime(a,b);




    }

    public static void measureTime(double[][] a, double[][] b){

        double[][] c;
        double[][] d;

        double seqStart = System.nanoTime();
//       c=sequentialMultiplyMatrix(a,b);
        double seqEnd = System.nanoTime();

        double parStart = System.nanoTime();
        d=parallelMultiplyMatrix(a, b);
        double parEnd = System.nanoTime();

        System.out.println("Time taken for Sequential :" +(seqEnd-seqStart)/1000000000 + " Seconds");
        System.out.println("Time taken for Parallel :" +(parEnd-parStart)/1000000000 + " Seconds");
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
   //only works on MATRIX_SIZE = 2^n for now
    public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
//        return multiply(a,b,0,0,0,0,MATRIX_SIZE);
        double[][] res=new double[MATRIX_SIZE][MATRIX_SIZE];
        Future f1 = executor.submit(new Multiply(a,b,res,0,0,0,0,0,0
        ,MATRIX_SIZE));
        try{
            f1.get();
            executor.shutdown();
        } catch (Exception e){

        }
        return res;
     }

     static class Multiply implements Runnable{
        private double[][] a;
        private double[][] b;
        private double[][] res;
        private int a_row, a_col, b_row, b_col, res_row, res_col, size;

        Multiply(double[][] a, double[][] b, double[][] res, int a_row, int a_col, int b_row, int b_col
        , int res_row, int res_col, int size){
            this.a = a;
            this.b = b;
            this.res = res;

            this.a_row = a_row;
            this.a_col = a_col;
            this.b_row = b_row;
            this.b_col = b_col;
            this.res_row = res_row;
            this.res_col = res_col;

            this.size = size;
        }

        public void run(){
            int half = size/2;
            if(size<=THRESHOLD){
                for (int i=0;i<size;i++){
                    for (int j=0;j<size;j++){
                        for (int l=0;l<size;l++){
                            res[res_row+i][res_col+j] +=a[a_row+i][a_col+l]*b[b_row+l][b_col+j];
                        }
                    }
                }
            } else{
                Multiply[] todo = {
                        new Multiply(a,b,res,a_row,a_col,b_row,b_col,res_row,res_col,half),
                        new Multiply(a,b,res,a_row,a_col+half,b_row+half,b_col,res_row
                                ,res_col,half),


                        new Multiply(a,b,res,a_row,a_col,b_row,b_col+half,res_row,res_col+half,
                        half),
                        new Multiply(a,b,res,a_row,a_col+half,b_row+half,b_col+half,
                                res_row, res_col+half, half),


                        new Multiply(a, b, res, a_row+half, a_col, b_row, b_col, res_row+half,
                                res_col, half),
                        new Multiply(a, b, res, a_row+half, a_col+half, b_row+half, b_col,
                                res_row+half, res_col, half),


                        new Multiply(a, b, res, a_row+half, a_col, b_row, b_col+half,
                                res_row+half, res_col+half, half),
                        new Multiply(a, b, res, a_row+half, a_col+half, b_row+half,
                                b_col+half, res_row+half, res_col+half, half)

                };
                FutureTask[] fs1 = new FutureTask[todo.length/2];

                for (int i=0;i<todo.length;i+=2) {
                    fs1[i / 2] = new FutureTask(new HelperSeq(todo[i], todo[i + 1]), null);
                    executor.execute(todo[i/2]);
                }
                for (int i=0;i<fs1.length;++i){
                    fs1[i].run();
                }
                try {
                    for (int i = 0; i < fs1.length; ++i) {
                        fs1[i].get();
                    }
                }   catch (Exception e){

                    }
                }
            }
         }


     static class HelperSeq implements Runnable{


        private Multiply m1, m2;
        HelperSeq(Multiply m1, Multiply m2){
            this.m1 = m1;
            this.m2 = m2;
        }
        public void run(){
            m1.run();
            m2.run();
        }

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

