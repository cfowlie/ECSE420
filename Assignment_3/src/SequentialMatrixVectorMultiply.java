public class SequentialMatrixVectorMultiply {
    private static final int MATRIX_SIZE = 4;
    public static void main(String[] args){
        double[][] matrix = generateRandomMatrix(MATRIX_SIZE,MATRIX_SIZE);
        double[] vector = generateRandomVector(MATRIX_SIZE);
        double[] result = new double[MATRIX_SIZE];
        for(int i=0;i<MATRIX_SIZE;i++){
            result[i]=0;
            for(int j=0;j<MATRIX_SIZE;j++){
                result[i]+=matrix[i][j]*vector[j];
            }
        }

        //Test to see

        System.out.println("Test Matrix:\n");
        printMatrix(matrix);
        System.out.println("\nTest Vector:\n");
        printVector(vector);
        System.out.println("\nResult:\n");
        printVector(result);
    }

    private static void printMatrix(double[][] matrix){
        for(int i=0;i<matrix.length;i++){
            System.out.print("| ");
            for(int j=0;j<matrix[i].length;j++){
                System.out.print(matrix[i][j]+ " ");
            }
            System.out.println("|");
        }
    }
    private static void printVector(double[] vector){
        System.out.print("| ");
        for(int i=0;i< vector.length;i++){
            System.out.print(vector[i]+ " ");
        }
        System.out.println("|");
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
}
