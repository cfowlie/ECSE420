import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ParallelMatrixVectorMultiply {
    private static final int NUMBER_THREADS = 8;
    private static final int MATRIX_SIZE = 8192;
    private static final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_THREADS);
    private static final int THRESHOLD = MATRIX_SIZE/(int)(Math.log(NUMBER_THREADS)/Math.log(4)*2);


    public static double[] parallelMultiply(double[][] matrix, double[] vector,int matrix_size){
        double[] res= new double[matrix_size];
        Future f1 = executor.submit(new Multiply(matrix,vector,res,0,0,0,0,matrix_size));
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
                FutureTask[] fs1 = new FutureTask[2];
                fs1[0] = new FutureTask(new HelperSeq(todo[0],todo[1]),null);
                fs1[1] = new FutureTask(new HelperSeq(todo[2],todo[3]),null);
                for (int i = 0; i < fs1.length; ++i) {
                    fs1[i].run();
                }
            }

        }

    }
    static class HelperSeq implements Runnable{


        private Multiply m1, m2;
        HelperSeq(Multiply m1, Multiply m2
        ){
            this.m1 = m1;
            this.m2 = m2;
        }
        public void run(){
            m1.run();
            m2.run();
        }

    }

}
