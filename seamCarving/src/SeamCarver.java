import java.awt.Color;

/*************************************************************************
 * Compilation: javac SeamCarver.java <br>
 * Execution: IllegalArgumentException IndexOutOfBoundsException<br>
 * Dependencies: Picture.java <br>
 * 
 * @author sumy
 * 
 *************************************************************************/
public class SeamCarver {
    private Picture pic;
    private int picWidth;
    private int picHeight;
    private double[][] picEnergy;
    private int[][] pixelIndex; // 删除之后剩余的元素对应原来图片的像素

    public SeamCarver(Picture picture) {
        this.pic = new Picture(picture);
        this.picWidth = pic.width();
        this.picHeight = pic.height();
        picEnergy = new double[picHeight][picWidth];
        pixelIndex = new int[picHeight][picWidth];

        // 计算像素映射
        // BUG:要先计算映射再计算能量，因为计算能量需要映射的辅助。
        for (int i = 0; i < picHeight; i++) {
            for (int j = 0; j < picWidth; j++) {
                pixelIndex[i][j] = i * picWidth + j;
            }
        }
        // 计算像素能量
        for (int i = 0; i < picHeight; i++) {
            for (int j = 0; j < picWidth; j++) {
                picEnergy[i][j] = energy(j, i);
            }
        }
    }

    public Picture picture() {
        // 未修改图片，直接返回原图片
        if (picWidth == pic.width() && picHeight == pic.height())
            return pic;

        Picture tmp = new Picture(picWidth, picHeight);
        // 根据像素映射设置新图片像素
        for (int i = 0; i < picHeight; i++) {
            for (int j = 0; j < picWidth; j++) {
                tmp.set(j, i, pic.get(pixelIndex[i][j] % pic.width(),
                        pixelIndex[i][j] / pic.width()));
            }
        }

        pic = tmp;

        // 重新计算像素映射
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
        checkbound(x, y);
        if (x == 0 || y == 0 || x == picWidth - 1 || y == picHeight - 1)
            return 195075.0;
        return delta(getx(pixelIndex[y + 1][x]), gety(pixelIndex[y + 1][x]),
                getx(pixelIndex[y - 1][x]), gety(pixelIndex[y - 1][x]))
                + delta(getx(pixelIndex[y][x + 1]), gety(pixelIndex[y][x + 1]),
                        getx(pixelIndex[y][x - 1]), gety(pixelIndex[y][x - 1]));
    }

    public int[] findHorizontalSeam() {
        double[][] dp = new double[picHeight][picWidth]; // 当前点(j,i)到最后的最短距离
        int[][] par = new int[picHeight][picWidth]; // 当前点的最小值是根据哪个计算出来的

        // dp 初始化
        for (int i = 0; i < picHeight; i++) {
            dp[i][picWidth - 1] = picEnergy[i][picWidth - 1];
            par[i][picWidth - 1] = -1;
        }

        // 简单 dp
        // 转移方程 f(i,j) = max( f(i+1,j),f(i+1,j+1),f(i+1,j-1) )
        // 结果 ans = min( f(i,0) ) 0 <= i <= height-1
        // 时间复杂度 O(N^2)
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

        // 根据“指针”逐层返回
        int i = 0;
        int[] ret = new int[picWidth];
        while (minpoint != -1) {
            ret[i] = minpoint;
            minpoint = par[minpoint][i];
            i++;
        }

        return ret;
    }

    // 同上
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
        if (picHeight <= 1 || picWidth <= 1)
            throw new java.lang.IllegalArgumentException("Height or width is 1");
        if (a == null)
            throw new java.lang.IllegalArgumentException("input is null");
        if (a.length != picWidth)
            throw new java.lang.IllegalArgumentException("need input length:"
                    + picWidth + " but found:" + a.length);
        for (int i = 0; i < picWidth; i++) {
            if (a[i] < 0 || a[i] >= picHeight)
                throw new java.lang.IllegalArgumentException("Bound!");
            if (i == 0)
                continue;
            if (Math.abs(a[i] - a[i - 1]) > 1)
                throw new java.lang.IllegalArgumentException("Jump!");
        }

        // 拷贝删除
        for (int i = 0; i < picWidth; i++) {
            for (int j = a[i]; j < picHeight - 1; j++) {
                picEnergy[j][i] = picEnergy[j + 1][i];
                pixelIndex[j][i] = pixelIndex[j + 1][i];
            }
        }
        picHeight = picHeight - 1;

        // 更新删除附近的 energy
        for (int i = 0; i < picWidth; i++) {

            // BUG:特判一下，之前没特判，一直抛出异常
            if (a[i] < picHeight) {
                picEnergy[a[i]][i] = energy(i, a[i]);
            }
            if (a[i] - 1 >= 0) {
                picEnergy[a[i] - 1][i] = energy(i, a[i] - 1);
            }
        }

    }

    // 同上
    public void removeVerticalSeam(int[] a) {
        if (picHeight <= 1 || picWidth <= 1)
            throw new java.lang.IllegalArgumentException("Height or width is 1");
        if (a == null)
            throw new java.lang.IllegalArgumentException("input is null");
        if (a.length != picHeight)
            throw new java.lang.IllegalArgumentException("need input length:"
                    + picHeight + " but found:" + a.length);
        for (int i = 0; i < picHeight; i++) {
            if (a[i] < 0 || a[i] >= picWidth)
                throw new java.lang.IllegalArgumentException("Bound!");
            if (i == 0)
                continue;
            if (Math.abs(a[i] - a[i - 1]) > 1)
                throw new java.lang.IllegalArgumentException("Jump!");
        }

        // 使用 System.arraycopy() 函数
        for (int i = 0; i < picHeight; i++) {
            System.arraycopy(picEnergy[i], a[i] + 1, picEnergy[i], a[i],
                    picWidth - a[i] - 1);
            System.arraycopy(pixelIndex[i], a[i] + 1, pixelIndex[i], a[i],
                    picWidth - a[i] - 1);
        }
        picWidth = picWidth - 1;

        for (int i = 0; i < picHeight; i++) {
            if (a[i] < picWidth) {
                picEnergy[i][a[i]] = energy(a[i], i);
            }
            if (a[i] - 1 >= 0) {
                picEnergy[i][a[i] - 1] = energy(a[i] - 1, i);
            }
        }
    }

    // 计算 (ax,ay) 与 (bx,by) 的颜色方差
    private double delta(int ax, int ay, int bx, int by) {
        Color a = pic.get(ax, ay);
        Color b = pic.get(bx, by);
        double red = a.getRed() - b.getRed();
        double green = a.getGreen() - b.getGreen();
        double blue = a.getBlue() - b.getBlue();
        return red * red + green * green + blue * blue;
    }

    // 索引的图片横坐标
    private int getx(int index) {
        return index % pic.width();
    }

    // 索引的图片纵坐标
    private int gety(int index) {
        return index / pic.width();
    }

    // 检查范围
    private void checkbound(int x, int y) {
        if (x < 0 || y < 0 || x >= picWidth || y >= picHeight)
            throw new java.lang.IndexOutOfBoundsException("width=" + picWidth
                    + " height=" + picHeight + ",x=" + x + " y=" + y);
    }

    public static void main(String[] args) {
        Picture pic = new Picture(6, 6);
        SeamCarver seam = new SeamCarver(pic);
        for (int i = 0; i < 5; i++) {
            int len = pic.width();
            int[] a = new int[pic.width()];
            for (int j = 0; j < len; j++) {
                a[j] = 0;
            }
            seam.removeHorizontalSeam(a);
        }
        StdOut.println("Over!");
    }
}