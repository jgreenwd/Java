package processor;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    final public static Scanner scanner = new Scanner(System.in);

    static class Matrix {
        int rows, cols;
        double[][] matrix;
        boolean displayMantissa = false;

        Matrix(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            this.matrix = new double[rows][cols];
        }

        public void setElement(int row, int col, double el) {
            this.matrix[row][col] = el;
        }

        public void setRow(int row, double[] vals) {
            this.matrix[row] = vals;
        }

        public String toString() {
            if (this.matrix.length == 0) return "";

            StringBuilder result = new StringBuilder();
            for (double[] nums : this.matrix) {
                for (int j = 0; j < this.matrix[0].length; j++) {
                    if (this.displayMantissa) {
                        result.append(nums[j]).append(" ");
                    } else {
                        int el = (int) nums[j];
                        result.append(el).append(" ");
                    }
                }
                result.append("\n");
            }

            return result.toString();
        }

        public static Matrix add(Matrix A, Matrix B) {
            if (A.rows != B.rows || A.cols != B.cols) {
                throw new IllegalArgumentException("Matrix shapes incompatible");
            }

            Matrix result = new Matrix(A.rows, A.cols);
            if (A.displayMantissa || B.displayMantissa) result.displayMantissa = true;

            for (int i = 0; i < A.rows; i++) {
                for (int j = 0; j < A.cols; j++) {
                    double value = A.matrix[i][j] + B.matrix[i][j];
                    result.setElement(i, j, value);
                }
            }

            return result;
        }

        public static Matrix multiplyScalar(Matrix A, double scalar) {
            Matrix result = new Matrix(A.rows, A.cols);
            if (A.displayMantissa) result.displayMantissa = true;

            double value;
            for (int i = 0; i < A.rows; i++) {
                for (int j = 0; j < A.cols; j++) {
                    value = A.matrix[i][j] * scalar;
                    result.setElement(i, j, value);
                }
            }

            return result;
        }

        public static Matrix multiplyMatrix(Matrix A, Matrix B) {
            if (A.cols != B.rows) throw new IllegalArgumentException("Matrix shapes incompatible");

            Matrix result = new Matrix(A.rows, B.cols);
            if (A.displayMantissa || B.displayMantissa) result.displayMantissa = true;

            double temp = 0;
            for (int i = 0; i < result.matrix.length; i++) {
                for (int j = 0; j < B.matrix[0].length; j++) {
                    for (int k = 0; k < A.matrix[0].length; k++) {
                        temp += A.matrix[i][k] * B.matrix[k][j];
                    }
                    result.matrix[i][j] = temp;
                    temp = 0;
                }
            }

            return result;
        }

        public static Matrix transpose(Matrix A, int mode) {
            Matrix result;

            if (mode == 2) {
                // along the secondary diagonal
                result = new Matrix(A.cols, A.rows);
                for (int i = 0; i < A.rows; i++) {
                    for (int j = 0; j < A.cols; j++) {
                        result.matrix[A.cols - 1 - j][A.rows - 1 - i] = A.matrix[i][j];
                    }
                }
            } else if (mode == 3) {
                // Along the vertical axis
                result = new Matrix(A.rows, A.cols);
                int limit = A.matrix[0].length;
                for (int i = 0; i < A.matrix.length; i++) {
                    for (int j = 0; j < limit; j++) {
                        result.setElement(i, j, A.matrix[i][limit - 1 - j]);
                    }
                }
            } else if (mode == 4) {
                // Along the horizontal axis
                result = new Matrix(A.rows, A.cols);
                for (int i = 0; i < A.matrix.length; i++) {
                    result.setRow(result.matrix.length - 1 - i, A.matrix[i]);
                }
            } else {
                // along the main diagonal
                result = new Matrix(A.cols, A.rows);
                for (int i = 0; i < A.rows; i++) {
                    for (int j = 0; j < A.cols; j++) {
                        result.matrix[j][i] = A.matrix[i][j];
                    }
                }
            }

            return result;
        }

        public static double findDeterminant(Matrix A) {
            return determinantSolver(A.matrix);
        }

        public static double[][] buildSubMatrix(double[][] m, int skipCol) {
            return buildSubMatrix(m, skipCol, 0);
        }

        public static double[][] buildSubMatrix(double[][] m, int skipCol, int skipRow) {
            int aRow = 0, aCol;

            double[][] b = new double[m.length - 1][m.length - 1];

            for (int i = 0; i < b.length; i++) {
                if (i == skipRow) aRow++;
                aCol = 0;

                for (int j = 0; j < b.length; j++) {
                    if (j == skipCol) aCol++;
                    b[i][j] = m[aRow][aCol++];
                }

                aRow++;
            }

            return b;
        }

        public static double determinantSolver(double[][] d) {
            if (d.length != d[0].length) throw new IllegalArgumentException();
            else if (d.length == 3) {
                double a = d[0][0] * (d[1][1] * d[2][2] - d[1][2] * d[2][1]);
                double b = d[0][1] * (d[1][0] * d[2][2] - d[1][2] * d[2][0]);
                double c = d[0][2] * (d[1][0] * d[2][1] - d[1][1] * d[2][0]);

                return a - b + c;
            }
            else if (d.length == 2) return d[0][0] * d[1][1] - d[0][1] * d[1][0];
            else if (d.length == 1) return d[0][0];
            else {
                double[][] submatrix;
                double result = 0;

                for (int i = 0; i < d.length; i++) {
                    if (d[0][i] != 0) {
                        // find Minor for given Matrix
                        submatrix = buildSubMatrix(d, i);
                        result += determinantSolver(submatrix) * d[0][i] * Math.pow(-1, i % 2);
                    }
                }
                return result;
            }
        }

        public static Matrix findInverse(Matrix A) {
            double det = Matrix.findDeterminant(A);
            Matrix result = new Matrix(A.rows, A.cols);
            result.displayMantissa = true;

            if (det == 0) {
                result = null;
            } else {
                result.matrix = cofactorMatrixBuilder(A.matrix);
                result = Matrix.multiplyScalar(result, 1 / det);
            }

            return result;
        }

        public static double[][] cofactorMatrixBuilder(double[][] d) {
            if (d.length != d[0].length) throw new IllegalArgumentException();

            double[][] submatrix;
            double[][] result = new double[d.length][d.length];

            for (int i = 0; i < d.length; i++) {
                for (int j = 0; j < d.length; j++) {
                    submatrix = buildSubMatrix(d, i, j);
                    result[i][j] = determinantSolver(submatrix) * Math.pow(-1, i + j);
                }
            }

            return result;
        }
    }

    public static void displayMenu() {
        System.out.println("\n1. Add matrices\n2. Multiply matrix by a constant\n3. Multiply matrices\n" +
                "4. Transpose matrix\n5. Calculate a determinant\n6. Inverse matrix\n0. Exit");
        System.out.print("Your choice: ");
    }

    public static int transposeMenu() {
        System.out.println("\n1. Main diagonal\n2. Side diagonal\n3. Vertical line\n4. Horizontal line");
        System.out.print("Your choice: ");
        return scanner.nextInt();
    }

    public static double scalarPrompt() {
        System.out.println("Enter constant: > ");
        return scanner.nextDouble();
    }

    public static Matrix matrixBuilder(int num) {
        String sizeMessage = "Enter size of " + (num == 1 ? "first" : "second") + " matrix: ";
        System.out.print(sizeMessage);
        int[] shape = new int[] {scanner.nextInt(), scanner.nextInt()};
        Matrix m = new Matrix(shape[0], shape[1]);

        String entryMessage = "Enter " + (num == 1 ? "first" : "second") + " matrix: ";
        System.out.println(entryMessage);
        double el;
        for (int i = 0; i < shape[0]; i++) {
            for (int j = 0; j < shape[1]; j++) {
                if (!scanner.hasNextInt()) m.displayMantissa = true;
                el = scanner.nextDouble();
                m.setElement(i, j, el);
            }
        }

        return m;
    }

    public static void main(String[] args) {
        int option = 0;
        int tOption;
        Matrix result;
        double det = 0;

        do {
            displayMenu();

            try {
                option = scanner.nextInt();
                switch (option) {
                    case 1: result = Matrix.add(matrixBuilder(1), matrixBuilder(2));  break;
                    case 2: result = Matrix.multiplyScalar(matrixBuilder(1), scalarPrompt()); break;
                    case 3: result = Matrix.multiplyMatrix(matrixBuilder(1), matrixBuilder(2)); break;
                    case 4: tOption = transposeMenu(); result = Matrix.transpose(matrixBuilder(1), tOption); break;
                    case 5: result = null; det = Matrix.findDeterminant(matrixBuilder(1)); break;
                    case 6: result = Matrix.findInverse(matrixBuilder(1)); break;
                    default: result = new Matrix(0,0);
                }

                if (option == 6 && Objects.isNull(result)) {
                    System.out.println("This matrix doesn't have an inverse.");
                } else if (option != 0) {
                    System.out.println("The result is:");
                    System.out.print(Objects.isNull(result) ? det : result);
                }
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("The operation cannot be performed.");
                scanner.reset();
            }

        } while (option != 0);
    }
}
