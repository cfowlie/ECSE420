import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ParallelMatrixVectorMultiply {
    private static final int NUMBER_THREADS = 16;
    private static final int MATRIX_SIZE =2048;
    private static final int THRESHOLD = 64;
    private static final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_THREADS);

    public static void main(String[] args){
        double[][] matrix = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
        double[] vector = generateRandomVector(MATRIX_SIZE);
        double[] result = parallelMultiply(matrix,vector);
        double[] seqResult = sequentialMultiply(matrix,vector);
        printVector(seqResult);
        printVector(result);

    }

    public static double[] sequentialMultiply(double[][] matrix, double[] vector){
        double[] result = new double[MATRIX_SIZE];
        for(int i=0;i<MATRIX_SIZE;i++) {
            result[i] = 0;
            for (int j = 0; j < MATRIX_SIZE; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }

    public static double[] parallelMultiply(double[][] matrix, double[] vector){
        double[] res= new double[MATRIX_SIZE];
        Future f1 = executor.submit(new Multiply(matrix,vector,res,0,0,0,0,MATRIX_SIZE));
        try{
            f1.get();
            executor.shutdown();
        } catch (Exception e){

        }
        return res;
    }

    static class Multiply implements Runnable {


        private double[][] matrix;
        private double[] vector, res;
        private int mat_row, mat_col, vec_row, res_row, size;


        Multiply(double[][] matrix, double[] vector, double[] res, int mat_row, int mat_col, int vec_row, int res_row, int size) {
            this.matrix = matrix;
            this.vector = vector;
            this.res = res;

            this.mat_row = mat_row;
            this.mat_col = mat_col;
            this.vec_row = vec_row;
            this.res_row = res_row;
            this.size = size;
        }

        public void run() {
            int half = size / 2;
            if (size < THRESHOLD) {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        res[res_row + i] += matrix[mat_row + i][mat_col + j] * vector[vec_row + j];

                    }
                }
            } else {
                Multiply[] todo = {
                        new Multiply(matrix, vector, res, mat_row, mat_col, vec_row, res_row, half),
                        new Multiply(matrix, vector, res, mat_row, mat_col + half, vec_row + half, res_row
                                , half),


                        new Multiply(matrix, vector, res, mat_row + half, mat_col, vec_row,
                                res_row + half, half),
                        new Multiply(matrix, vector, res, mat_row + half, mat_col + half, vec_row + half,
                                res_row + half, half)

                };
                FutureTask[] fs1 = new FutureTask[todo.length / 2];

                fs1[0] = new FutureTask(new HelperSeq(todo[0],todo[1]),null);
                fs1[1] = new FutureTask(new HelperSeq(todo[2],todo[3]),null);
                for (int i = 0; i < fs1.length; ++i) {
                    fs1[i].run();
                }
                try {
                    for (int i = 0; i < fs1.length; ++i) {
                        fs1[i].get();
                    }
                } catch (Exception e) {

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


    // GenerateRandomMatrix method taken from A1

    private static double[][] generateRandomMatrix (int numRows, int numCols) {
        double matrix[][] = new double[numRows][numCols];
        for (int row = 0 ; row < numRows ; row++ ) {
            for (int col = 0 ; col < numCols ; col++ ) {
                matrix[row][col] = (double) ((int) (Math.random() * 10.0));
            }
        }
        return matrix;
    }

    // GenerateRandomVector method adapted from the above method

    private static double[] generateRandomVector(int numRows){
        double vector[] = new double[numRows];
        for(int row = 0;row < numRows ; row++){
            vector[row]=(double) ((int) (Math.random()*10.0));
        }
        return vector;
    }

    //Helper method to print a Matrix

    private static void printMatrix(double[][] matrix){
        for(int i=0;i<matrix.length;i++){
            System.out.print("| ");
            for(int j=0;j<matrix[i].length;j++){
                System.out.print(matrix[i][j]+ " ");
            }
            System.out.println("|");
        }
    }

    //Helper method to print a Vector

    private static void printVector(double[] vector){
        System.out.print("| ");
        for(int i=0;i< vector.length;i++){
            System.out.print(vector[i]+ " ");
        }
        System.out.println("|");
    }
}
