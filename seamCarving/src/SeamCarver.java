import java.awt.Color;

public class SeamCarver {
    private Picture pic;
    private int picWidth;
    private int picHeight;
    private double[][] picEnergy;
    private int[][] pixelIndex;

    public SeamCarver(Picture picture) {
        this.pic = new Picture(picture);
        this.picWidth = pic.width();
        this.picHeight = pic.height();
        picEnergy = new double[picHeight][picWidth];
        pixelIndex = new int[picHeight][picWidth];
        for (int i = 0; i < picHeight; i++) {
            for (int j = 0; j < picWidth; j++) {
                pixelIndex[i][j] = i * picWidth + j;
            }
        }
        for (int i = 0; i < picHeight; i++) {
            for (int j = 0; j < picWidth; j++) {
                picEnergy[i][j] = energy(j, i);
            }
        }
    }

    public Picture picture() {
        if (picWidth == pic.width() && picHeight == pic.height())
            return pic;
        Picture tmp = new Picture(picWidth, picHeight);
        for (int i = 0; i < picHeight; i++) {
            for (int j = 0; j < picWidth; j++) {
                tmp.set(j, i, pic.get(pixelIndex[i][j] % pic.width(),
                        pixelIndex[i][j] / pic.width()));
            }
        }
        pic = tmp;
        pixelIndex = new int[picHeight][picWidth];
        for (int i = 0; i < picHeight; i++) {
            for (int j = 0; j < picWidth; j++) {
                pixelIndex[i][j] = i * picWidth + j;
            }
        }
        return pic;
    }

    public int width() {
        return picWidth;
    }

    public int height() {
        return picHeight;
    }

    public double energy(int x, int y) {
        if (x == 0 || y == 0 || x == picWidth - 1 || y == picHeight - 1)
            return 195075.0;
        return delta(getx(pixelIndex[y + 1][x]), gety(pixelIndex[y + 1][x]),
                getx(pixelIndex[y - 1][x]), gety(pixelIndex[y - 1][x]))
                + delta(getx(pixelIndex[y][x + 1]), gety(pixelIndex[y][x + 1]),
                        getx(pixelIndex[y][x - 1]), gety(pixelIndex[y][x - 1]));
    }

    public int[] findHorizontalSeam() {

        double[][] dp = new double[picHeight][picWidth];
        int[][] par = new int[picHeight][picWidth];

        for (int i = 0; i < picHeight; i++) {
            dp[i][picWidth - 1] = picEnergy[i][picWidth - 1];
            par[i][picWidth - 1] = -1;
        }
        for (int j = picWidth - 2; j >= 0; j--) {
            for (int i = 0; i < picHeight; i++) {
                dp[i][j] = dp[i][j + 1];
                par[i][j] = i;
                if (i - 1 >= 0) {
                    if (dp[i - 1][j + 1] < dp[i][j]) {
                        dp[i][j] = dp[i - 1][j + 1];
                        par[i][j] = i - 1;
                    }
                }
                if (i + 1 < picHeight) {
                    if (dp[i + 1][j + 1] < dp[i][j]) {
                        dp[i][j] = dp[i + 1][j + 1];
                        par[i][j] = i + 1;
                    }
                }
                dp[i][j] += picEnergy[i][j];
            }
        }

        int minpoint = 0;
        double minemergy = dp[0][0];
        for (int i = 1; i < picHeight; i++) {
            if (dp[i][0] < minemergy) {
                minemergy = dp[i][0];
                minpoint = i;
            }
        }
        int i = 0;
        int[] ret = new int[picWidth];
        while (minpoint != -1) {
            ret[i] = minpoint;
            minpoint = par[minpoint][i];
            i++;
        }

        return ret;
    }

    public int[] findVerticalSeam() {

        double[][] dp = new double[picHeight][picWidth];
        int[][] par = new int[picHeight][picWidth];

        for (int i = 0; i < picWidth; i++) {
            dp[picHeight - 1][i] = picEnergy[picHeight - 1][i];
            par[picHeight - 1][i] = -1;
        }
        for (int i = picHeight - 2; i >= 0; i--) {
            for (int j = 0; j < picWidth; j++) {
                dp[i][j] = dp[i + 1][j];
                par[i][j] = j;
                if (j - 1 >= 0) {
                    if (dp[i + 1][j - 1] < dp[i][j]) {
                        dp[i][j] = dp[i + 1][j - 1];
                        par[i][j] = j - 1;
                    }
                }
                if (j + 1 < picWidth) {
                    if (dp[i + 1][j + 1] < dp[i][j]) {
                        dp[i][j] = dp[i + 1][j + 1];
                        par[i][j] = j + 1;
                    }
                }
                dp[i][j] += picEnergy[i][j];

            }
        }

        int minpoint = 0;
        double minemergy = dp[0][0];
        for (int i = 1; i < picWidth; i++) {
            if (dp[0][i] < minemergy) {
                minemergy = dp[0][i];
                minpoint = i;
            }
        }
        int i = 0;
        int[] ret = new int[picHeight];
        while (minpoint != -1) {
            ret[i] = minpoint;
            minpoint = par[i][minpoint];
            i++;
        }
        return ret;
    }

    public void removeHorizontalSeam(int[] a) {
        for (int i = 0; i < picWidth; i++) {
            for (int j = a[i]; j < picHeight - 1; j++) {
                picEnergy[j][i] = picEnergy[j + 1][i];
                pixelIndex[j][i] = pixelIndex[j + 1][i];
            }
        }
        picHeight = picHeight - 1;
        for (int i = 0; i < picWidth; i++) {
            picEnergy[a[i]][i] = energy(i, a[i]);
            if (a[i] - 1 >= 0) {
                picEnergy[a[i] - 1][i] = energy(i, a[i] - 1);
            }
        }

    }

    public void removeVerticalSeam(int[] a) {
        for (int i = 0; i < picHeight; i++) {
            System.arraycopy(picEnergy[i], a[i] + 1, picEnergy[i], a[i],
                    picWidth - a[i] - 1);
            System.arraycopy(pixelIndex[i], a[i] + 1, pixelIndex[i], a[i],
                    picWidth - a[i] - 1);
        }
        picWidth = picWidth - 1;
        for (int i = 0; i < picHeight; i++) {
            picEnergy[i][a[i]] = energy(a[i], i);
            if (a[i] - 1 >= 0) {
                picEnergy[i][a[i] - 1] = energy(a[i] - 1, i);
            }
        }
    }

    private double delta(int ax, int ay, int bx, int by) {
        Color a = pic.get(ax, ay);
        Color b = pic.get(bx, by);
        double red = a.getRed() - b.getRed();
        double green = a.getGreen() - b.getGreen();
        double blue = a.getBlue() - b.getBlue();
        return red * red + green * green + blue * blue;
    }

    private int getx(int index) {
        return index % pic.width();
    }

    private int gety(int index) {
        return index / pic.width();
    }

}