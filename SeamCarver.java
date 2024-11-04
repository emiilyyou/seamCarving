import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    /* @citation Adapted from: https://introcs.cs.princeton.edu/java/stdlib
     * /javadoc/Picture.html
     * Accessed 04/16/2024. */

    // Picture object
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0) throw new IllegalArgumentException();
        if (x > picture.width() || y > picture.height())
            throw new IllegalArgumentException();

        // x-index of next column
        int x1rgb = picture.getRGB((x + 1 + width()) % width(), y);
        // x-index of previous column
        int x2rgb = picture.getRGB((x - 1 + width()) % width(), y);
        // y-index of next row
        int y1rgb = picture.getRGB(x, (y + 1 + height()) % height());
        // y-index of previous row
        int y2rgb = picture.getRGB(x, (y - 1 + height()) % height());

        // calculate gradients for x and y components respectively
        double xgradient = gradientcalc(x1rgb, x2rgb);
        double ygradient = gradientcalc(y1rgb, y2rgb);

        return Math.sqrt(xgradient + ygradient);
    }


    // private helper method to calculate respective gradients
    private double gradientcalc(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = (rgb1) & 0xFF;

        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >> 8) & 0xFF;
        int b2 = (rgb2) & 0xFF;

        int redDiff = r1 - r2;
        int greenDiff = g1 - g2;
        int blueDiff = b1 - b2;

        return Math.pow(redDiff, 2) + Math.pow(greenDiff, 2) + Math.pow(blueDiff, 2);
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[][] edgeTo = new int[picture.width()][picture.height()];
        double[][] distTo = new double[picture.width()][picture.height()];

        // make an array to store energies
        double[][] energystore = new double[picture.width()][picture.height()];

        // fill energy array throughout the pixels of picture
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                energystore[i][j] = energy(i, j);
            }
        }

        // initialization of first row
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 1; j < picture.height(); j++) {
                // set distTo to infinity and edgeTo to null with -1
                distTo[i][j] = Integer.MAX_VALUE;
                edgeTo[i][j] = -1;
            }
            // copy energy store array to distTo array
            distTo[i][0] = energystore[i][0];
        }

        // update values as you increment
        for (int i = 0; i < picture.height() - 1; i++) { // row
            for (int j = 0; j < picture.width(); j++) { // col

                if (j != 0) {
                    // left bottom
                    relax(j, j - 1, i + 1, edgeTo, distTo, energystore);
                }
                if (j != picture.width() - 1) {
                    // right bottom
                    relax(j, j + 1, i + 1, edgeTo, distTo, energystore);
                }
                // direct bottom
                relax(j, j, i + 1, edgeTo, distTo, energystore);
            }

        }

        // array to store x-values of seam
        int[] x = new int[picture.height()];

        double min = Double.POSITIVE_INFINITY;
        int minindex = -1;

        // find minimum across bottom row
        for (int i = 0; i < distTo.length; i++) {
            if (distTo[i][picture.height() - 1] < min) {
                min = distTo[i][picture.height() - 1];
                minindex = i;
            }
        }

        x[picture.height() - 1] = minindex;
        // iterate through edgeTo and store x-values
        for (int i = picture.height() - 1; i > 0; i--) {
            x[i - 1] = edgeTo[minindex][i];
            minindex = x[i - 1];
        }
        return x;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        // create newPic with transposed dimensions
        Picture newPic = new Picture(picture.height(), picture.width());
        Picture picture2 = new Picture(picture);

        // get and set pixels
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                int color = picture.getRGB(i, j);
                newPic.setRGB(j, i, color);
            }
        }
        picture = newPic;
        int[] horizontalseam = findVerticalSeam();
        // revert picture to original
        picture = picture2;

        return horizontalseam;
    }

    // relax edges and update distTo and edgeTo
    private void relax(int v, int w, int row, int[][] edgeTo, double[][] distTo,
                       double[][] energystored) {
        if (distTo[w][row] > distTo[v][row - 1] + energystored[w][row]) {
            distTo[w][row] = distTo[v][row - 1] + energystored[w][row];
            edgeTo[w][row] = v;
        }
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (picture.height() == 1)
            throw new IllegalArgumentException();
        if (seam.length != picture.width()) {
            throw new IllegalArgumentException();
        }

        // create newPicture with new bounds
        Picture newPicture = new Picture(picture.width(), picture.height() - 1);

        for (int i = 0; i < picture.width(); i++) {
            if (seam[i] < 0 || seam[i] >= picture.height()) {
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < picture.height(); j++) {
                if (i < picture.width() - 1 && Math.abs(seam[i] - seam[i + 1]) > 1) {
                    throw new IllegalArgumentException();
                }
                // move seams to shrink image
                if (seam[i] > j) {
                    newPicture.setRGB(i, j, picture.getRGB(i, j));
                }
                else if (seam[i] < j) {
                    newPicture.setRGB(i, j - 1, picture.getRGB(i, j));
                }
            }
        }
        picture = newPicture;
    }


    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (picture.width() == 1)
            throw new IllegalArgumentException();
        if (seam.length != picture.height()) {
            throw new IllegalArgumentException();
        }

        // create newPicture with new bounds
        Picture newPicture = new Picture(picture.width() - 1, picture.height());

        for (int i = 0; i < picture.height(); i++) {
            if (seam[i] < 0 || seam[i] >= picture.width()) {
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < picture.width(); j++) {
                if (i < picture.height() - 1 && Math.abs(seam[i] - seam[i + 1]) > 1) {
                    throw new IllegalArgumentException();
                }
                // move seams to shrink image
                if (seam[i] > j) {
                    newPicture.setRGB(j, i, picture.getRGB(j, i));
                }
                else if (seam[i] < j) {
                    newPicture.setRGB(j - 1, i, picture.getRGB(j, i));
                }
            }
        }
        picture = newPicture;
    }

    //  unit testing (required)
    public static void main(String[] args) {
        Picture picture = new Picture(2000, 10);
        StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
        picture.show();
        // Picture random = SCUtility.randomPicture(51200, 2000);
        SeamCarver sc = new SeamCarver(picture);
        StdOut.println(sc.findVerticalSeam());
        StdOut.println(sc.findHorizontalSeam());
        StdOut.println(sc.energy(0, 0));
        int[] array = { 4 };
        int[] array2 = { 4, 5, 6 };
        sc.removeHorizontalSeam(array);
        sc.removeVerticalSeam(array2);
        StdOut.print(sc.picture());
        // Stopwatch stopwatch = new Stopwatch();
        // sc.removeVerticalSeam(sc.findVerticalSeam());
        // sc.removeHorizontalSeam(sc.findHorizontalSeam());
        // StdOut.println(stopwatch.elapsedTime());

    }
}
