package multiplication;

import java.io.File;
import java.io.IOException;

/**
 * Основной класс
 */
public class Main {

    /**
     * Количество строк в первой матрице
     */
    private static int firstRow = 100;
    /**
     * Количество столбцов в первой матрице
     */
    private static int firstColumn = 300;
    /**
     * Количество столбцов во второй матрице
     */
    private static int secondColumn = 500;
    /**
     * Первая матрица
     */
    private static int[][] firstArray = new int[firstRow][firstColumn];
    /**
     * Вторая матрица
     */
    private static int[][] secondArray = new int[firstColumn][secondColumn];
    /**
     * Результирующая матрица
     */
    private static int[][] result = new int[firstRow][secondColumn];

    public static void main(String[] args) throws IOException {

        Multiply.randomMatrix(firstArray);
        Multiply.randomMatrix(secondArray);

        Multiply.writeFile(firstArray,new File("FirstMatrix.scv"));
        Multiply.writeFile(secondArray,new File("SecondMatrix.scv"));

        long workTime = System.currentTimeMillis();
        result= Multiply.multiplyMatrixMT(firstArray, secondArray, Runtime.getRuntime().availableProcessors());
        System.out.println("Multithreading Computing Speed = " + (System.currentTimeMillis() - workTime));
        Multiply.writeFile(result, new File("ResultmMatrix.scv"));
    }

}
