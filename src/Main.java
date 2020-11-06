public class Main {

    static final int size = 10000000;
    static final int h = size / 2;
    static Object lock = new Object();

    public static void main(String[] args) {
        handleArraySimple();
        System.out.println("=======================");
        handleArrayParallel();
    }

    static void handleArraySimple() {
        float[] arr = new float[size];
        for (int i = 0; i < size; i++) {
            arr[i] = 1f;
        }
        calculateArray(arr, size, "Simple array");
    }


    static void handleArrayParallel() {
        float[] arr = new float[size];
        float[] arr1 = new float[h];
        float[] arr2 = new float[h];
        for (int i = 0; i < size; i++) {
            arr[i] = 1f;
        }
        long a = System.currentTimeMillis();
        System.arraycopy(arr, 0, arr1, 0, h);
        System.arraycopy(arr, h, arr2, 0, h);
        long b = System.currentTimeMillis();
        System.out.println("Forking elapsed time: " + (b - a));

        new Thread(() -> calculateArray(arr1, h, "Array1")).start();
        new Thread(() -> calculateArray(arr2, h, "Array2")).start();

        long i = System.currentTimeMillis();
        synchronized (arr1) {
            System.arraycopy(arr1, 0, arr, 0, h);
        }
        synchronized (arr2) {
            System.arraycopy(arr2, 0, arr, h, h);
        }
        System.out.println("Joining elapsed time: " + (System.currentTimeMillis() - i));

        System.out.println("Parallel method elapsed time: " + (System.currentTimeMillis() - a));
    }

    static void calculateArray(float[] arr, int size, String arrName) {
        synchronized (arr) {
            long c = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
            System.out.println(arrName + " calculation elapsed time: " + (System.currentTimeMillis() - c));
        }
    }
}

