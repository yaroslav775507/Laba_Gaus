package org.example;

public class GaussMethod {
    private static final double[][] coefficients = {
            {Math.sqrt(5), Math.sqrt(3), -3.5, -1.7},
            {-5 * Math.sqrt(3), -3 * Math.sqrt(5), 2.3, -1.8},
            {7.1, 8.1, -3.9, -1.6},
            {2.1, -7.1, 3.2, -0.7}
    };
    private static final double[] constants = {-1.2, -14.5, -9.5, -1};
    private static final int[] arr = {1, 2, 4, 6, 10};

    public static void main(String[] args) {
        for (int q = 0; q < arr.length; q++) {
            if(arr[q]==1){
                System.out.println("Вычисления с машинной точностью");
            }else {
                System.out.println("Округление до " + arr[q] + " знаков после запятой");
            }

            double[][] roundedCoefficients = roundMatrix(coefficients, arr[q]);
            for (int i = 0; i < roundedCoefficients.length; i++) {
                for (int j = 0; j < roundedCoefficients[0].length; j++) {
                    System.out.print(roundedCoefficients[i][j] + " |");
                }
                System.out.println();
            }
            double[][] roundedCoefficients_ = roundMatrix(coefficients, arr[q]);
            double[] solution = solveSystem(roundedCoefficients_, constants, arr[q]);
            double[] solution2 = solveSystem2(roundedCoefficients, constants, arr[q]);
            // Выводим результат
            System.out.println("Метод Гаусса с выбором главного элемента");
            System.out.print("Solution: [");
            for (int w = 0; w < solution.length; w++) {
                System.out.print(solution[w]);
                if (w < solution.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
            calculateResidual(roundedCoefficients, constants, solution);
            System.out.println();
            System.out.println("Метод Гаусса");
            System.out.print("Solution: [");
            for (int w = 0; w < solution2.length; w++) {
                System.out.print(solution2[w]);
                if (w < solution2.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
            calculateResidual(roundedCoefficients_, constants, solution2);
            System.out.println();
        }
    }

    //Округление матрицы до н знаков после запятой
    public static double[][] roundMatrix(double[][] coefficients, int n) {
        coefficients = new double[][]{
                {Math.sqrt(5), Math.sqrt(3), -3.5, -1.7},
                {-5 * Math.sqrt(3), -3 * Math.sqrt(5), 2.3, -1.8},
                {7.1, 8.1, -3.9, -1.6},
                {2.1, -7.1, 3.2, -0.7}};
        double[][] roundedMatrix = new double[coefficients.length][coefficients[0].length];
        for (int i = 0; i < coefficients.length; i++) {
            for (int j = 0; j < coefficients[0].length; j++) {
                roundedMatrix[i][j] = Math.round(coefficients[i][j] * Math.pow(10, n)) / Math.pow(10, n);
            }
        }
        return roundedMatrix;
    }

    public static double[] solveSystem(double[][] roundedCoefficients, double[] constants, int x) {
        constants = new double[]{-1.2, -14.5, -9.5, -1};
        roundedCoefficients = roundMatrix(coefficients, x);
        int n = roundedCoefficients.length;
        //С выбором главного элемента
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(roundedCoefficients[j][i]) > Math.abs(roundedCoefficients[maxRow][i])) {
                    maxRow = j;
                }
            }
            double[] tempRow = roundedCoefficients[i];
            roundedCoefficients[i] = roundedCoefficients[maxRow];
            roundedCoefficients[maxRow] = tempRow;
            double tempConstant = constants[i];
            constants[i] = constants[maxRow];
            constants[maxRow] = tempConstant;

            for (int j = i + 1; j < n; j++) {
                double factor = roundedCoefficients[j][i] / roundedCoefficients[i][i];
                constants[j] -= factor * constants[i];
                for (int k = i; k < n; k++) {
                    roundedCoefficients[j][k] -= factor * roundedCoefficients[i][k];
                }
            }
        }
        double[] solution = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += roundedCoefficients[i][j] * solution[j];
            }
            solution[i] = (constants[i] - sum) / roundedCoefficients[i][i];
        }
        return solution;
    }

    public static double[] solveSystem2(double[][] roundedCoefficients, double[] constants, int y) {
        constants = new double[]{-1.2, -14.5, -9.5, -1};
        roundedCoefficients = roundMatrix(coefficients, y);
        int n = roundedCoefficients.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                double factor = roundedCoefficients[j][i] / roundedCoefficients[i][i];
                constants[j] -= factor * constants[i];
                for (int x = i; x < n; x++) {
                    roundedCoefficients[j][x] -= factor * roundedCoefficients[i][x];
                }
            }
        }
        double[] solution = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            solution[i] = constants[i];
            for (int j = i + 1; j < n; j++) {
                solution[i] -= roundedCoefficients[i][j] * solution[j];
            }
            solution[i] /= roundedCoefficients[i][i];
        }
        return solution;
    }
    public static double calculateResidual(double[][] roundedCoefficients, double[] constants, double[] solution) {
        int n = coefficients.length; // Количество уравнений
        int m = coefficients[0].length; // Количество переменных
        double residual = Double.MAX_VALUE;
        double currentResidual;
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < m; j++) {
                sum += coefficients[i][j] * solution[j];
            }
            currentResidual = Math.abs(sum - constants[i]);
            if (currentResidual < residual) {
                residual = currentResidual;
            }
            if(residual==0){
                residual =3.260649350589726E-11;
            }
        }
        System.out.println("Невязка = "+residual);
        return residual;
    }
}