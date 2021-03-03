package multiplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Класс, в котором происходит вычисление ячеек матрицы
 */
public class Multiply extends Thread{

    /**
     * Первая матрица
     */
    private int[][] firstMatrix;
    /**
     * Вторая матрица
     */
    private int[][] secondMatrix;
    /**
     * Результат умножения двух матриц, т.е. результирующая матрица
     */
    private int[][] resultMatrix;
    /**
     * Начальный индекс
     */
    private int firstIndex;
    /**
     * Конечный индекс
     */
    private int lastIndex;

    /** Конструктор с параметрами
     * @param firstMatrix  Первая матрица
     * @param secondMatrix Вторая матрица
     * @param resultMatrix Результирующая матрица
     * @param firstIndex   Начальный индекс (ячейка с этим индексом вычисляется).
     * @param lastIndex    Конечный индекс (ячейка с этим индексом не вычисляется).
     */
    public Multiply(int[][] firstMatrix, int[][] secondMatrix, int[][] resultMatrix,
                    int firstIndex, int lastIndex) {
        this.firstMatrix  = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;
        this.firstIndex   = firstIndex;
        this.lastIndex    = lastIndex;
    }

    /**
     * Конструктор без параметров
     */
    public Multiply() {
    }

    /**
     * Вычисление значения в ячейке
     * @param row Номер строки ячейки.
     * @param col Номер столбца ячейки.
     */
    private void calcValue(int row, int col) {
        int sum = 0;
        for (int i = 0; i < secondMatrix.length; ++i)
            sum += firstMatrix[row][i] * secondMatrix[i][col];
        resultMatrix[row][col] = sum;
    }

    /** Функция потока */
    @Override
    public void run() {
        System.out.println("Thread: " + getName() + " started. Calculating cells from " + firstIndex + " to " + lastIndex + "...");

        final int colCount = secondMatrix[0].length;
        for (int index = firstIndex; index < lastIndex; ++index)
            calcValue(index / colCount, index % colCount);

        System.out.println("Thread " + getName() + " finished.");
    }

    /**
     * Многопоточное умножение матриц
     * @param firstMatrix первая матрица
     * @param secondMatrix вторая матрица
     * @param threadCount количество потоков
     * @return результирующая матрица
     */
    public static int[][] multiplyMatrixMT(int[][] firstMatrix,int[][] secondMatrix, int threadCount) {

        final int rowCount = firstMatrix.length;
        final int colCount = secondMatrix[0].length;
        final int[][] result = new int[rowCount][colCount];

        final int cellsForThread = (rowCount * colCount) / threadCount;
        int firstIndex = 0;
        final Multiply[] multiplierThreads = new Multiply[threadCount];

        for (int threadIndex = threadCount-1; threadIndex >= 0; --threadIndex) {
            int lastIndex = firstIndex + cellsForThread;
            if (threadIndex == 0) {
                lastIndex = rowCount * colCount;
            }
            multiplierThreads[threadIndex] = new Multiply(firstMatrix, secondMatrix, result, firstIndex, lastIndex);
            multiplierThreads[threadIndex].start();
            firstIndex = lastIndex;
        }

        try {
            for (final Multiply multiplierThread : multiplierThreads)
                multiplierThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Заполнение матрицы случайными значениями
     * @param matrix матрица
     * @return
     */
    public static int[][] randomMatrix(int[][] matrix) {
        final Random random = new Random();
        for (int row = 0; row < matrix.length; ++row)
            for (int col = 0; col < matrix[row].length; ++col)
                matrix[row][col] = random.nextInt(50);
        return matrix;
    }

    /**
     * Запись матрицы в файл
     * @param matrix матрица
     * @throws IOException
     */
    public static void writeFile(int[][] matrix, File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("Row: " + matrix.length);
        fileWriter.write("\n");
        fileWriter.write("Column: " + matrix[0].length);
        fileWriter.write("\n");
        for (int j = 0; j < matrix.length; j++) {
            for (int k = 0; k < matrix[j].length; k++) {
                fileWriter.write(matrix[j][k]+";");
            }
            fileWriter.write("\n");
            fileWriter.flush();
        }
        fileWriter.close();
    }

}

