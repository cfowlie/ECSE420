package ca.mcgill.ecse420.a1;

import  com.sun.org.apache.xpath.internal.operations.Mult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatrixMultiplication {
   
   private static final int NUMBER_THREADS = 4;
   private static final int MATRIX_SIZE = 100 ;
//   private static final int THRESHOLD = 64;
   private final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_THREADS);
    public static void main(String[] args) {
      
      // Generate two random matrices, same size
      double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
      double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
      double[][] c;
      double[][] d;
      c=sequentialMultiplyMatrix(a,b);
      d=parallelMultiplyMatrix(a, b);
      System.out.println("Seq: " + java.util.Arrays.deepToString(c));
      System.out.println("parallel: " + java.util.Arrays.deepToString(d));
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
        return multiply(a,b,0,0,0,0,MATRIX_SIZE);
     }

     public static double[][] multiply(double[][] a, double[][] b, int aRow, int bRow, int aCol, int bCol, int size){
        double[][] resultMatrix = new double[size][size];
         if(size==1){
             resultMatrix[0][0]=a[aRow][aCol]*b[bRow][bCol];
         }
        else if(size<=64){
            int miniSize = size/2;
            add(resultMatrix,multiply(a, b, aRow, bRow, aCol, bCol, miniSize),multiply(a, b, aRow, bRow+ miniSize,
                    aCol+miniSize, bCol, miniSize),0, 0);

            add(resultMatrix,multiply(a, b, aRow, bRow, aCol, bCol + miniSize, miniSize),
                    multiply(a, b, aRow, bRow+ miniSize, aCol+miniSize, bCol+miniSize, miniSize),0, miniSize);

            add(resultMatrix,multiply(a, b, aRow+ miniSize, bRow, aCol, bCol, miniSize),
                    multiply(a, b, aRow+ miniSize, bRow+ miniSize, aCol+miniSize,
                            bCol, miniSize),miniSize, 0);

            add(resultMatrix,multiply(a, b, aRow+ miniSize, bRow, aCol, bCol+miniSize, miniSize)
                    ,multiply(a, b, aRow+ miniSize, bRow+ miniSize, aCol+miniSize, bCol+miniSize,
                      miniSize),miniSize, miniSize);


        }else{

            int miniSize = size/2;
            class MultiplyMatrices implements Runnable{
                private Thread t1;
                private String name;
                MultiplyMatrices(String threadName){
                    this.name = threadName;
                }
                public void run(){
                    if (name=="q1"){
                        add(resultMatrix,multiply(a,b,aRow,bRow,aCol,bCol,miniSize),
                                multiply(a,b,aRow,bRow+miniSize,aCol+miniSize,bCol,miniSize),0,0);
                    }else if(name=="q2"){
                        add(resultMatrix,multiply(a,b,aRow,bRow,aCol,bCol + miniSize,miniSize),
                                multiply(a,b,aRow,bRow+miniSize,aCol+miniSize,bCol+miniSize,miniSize),0,miniSize);
                    } else if(name=="q3"){
                        add(resultMatrix,multiply(a,b,aRow+miniSize,bRow,aCol,bCol,miniSize),
                                multiply(a,b,aRow+miniSize,bRow+miniSize,aCol+miniSize,bCol,miniSize),miniSize,0);
                    } else if(name=="q4"){
                        add(resultMatrix,multiply(a,b,aRow + miniSize,bRow,aCol,bCol + miniSize,miniSize),
                                multiply(a,b,aRow+miniSize,bRow+miniSize,aCol+miniSize,bCol+miniSize,miniSize),
                                miniSize,miniSize);
                    }
                }
                public void start(){
                    if(t1==null){
                        t1= new Thread(this,"Thread1");
                        t1.start();
                    }
                }
            }
            MultiplyMatrices mul1= new MultiplyMatrices("q1");
            mul1.start();
            MultiplyMatrices mul2= new MultiplyMatrices("q2");
            mul2.start();
            MultiplyMatrices mul3= new MultiplyMatrices("q3");
            mul3.start();
            MultiplyMatrices mul4= new MultiplyMatrices("q4");
            mul4.start();

            for (Thread t : new Thread[] {mul1.t1,mul2.t1,mul3.t1,mul4.t1}){
                try{
                    t.join();
                } catch (Exception e){

                }
            }
        }
        return resultMatrix;
     }
     public static void add(double[][] res, double[][] a, double[][] b, int resRow, int resCol){
        int size = a.length;
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                res[i+resRow][j+resCol] = a[i][j]+b[i][j];
            }
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

