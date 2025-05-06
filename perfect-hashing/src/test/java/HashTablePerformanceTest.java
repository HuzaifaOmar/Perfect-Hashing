import hashing.tables.IPerfectHashTable;
import hashing.tables.LinearSpaceHashTable;
import hashing.tables.QuadraticSpaceHashTable;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HashTablePerformanceTest {

    private static final int[] DATASET_SIZES = {10, 100, 500};
    private static final int ITERATIONS = 20;

    private List<String> generateDataset(int size) {
        List<String> result = new ArrayList<>(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            StringBuilder sb = new StringBuilder();
            int length = random.nextInt(10) + 5; //length of string [5, 14]
            for (int j = 0; j < length; j++)
                sb.append((char) (random.nextInt(26) + 'a'));
            result.add(sb.toString());
        }
        return result;
    }

    @Test
    public void testBuildTimeAndRebuildCount() {
        System.out.println("\n=== Build Time and Rebuild Attempts Test ===");
        System.out.printf("%-10s | %-25s | %-25s | %-20s | %-20s%n",
                "Size", "Quadratic Build Time (ms)", "Linear Build Time (ms)",
                "Quadratic Rebuilds", "Linear Rebuilds");
        System.out.println("-".repeat(100));

        for (int size : DATASET_SIZES){
            long quadraticBuildTime = 0;
            long linearBuildTime = 0;
            int quadraticRebuilds = 0;
            int linearRebuilds = 0;

            for (int i = 0; i < ITERATIONS; i++) {
                List<String> dataset = generateDataset(size);

                IPerfectHashTable quadraticHashTable = new QuadraticSpaceHashTable();
                long quadraticBuildStart = System.currentTimeMillis();
                quadraticRebuilds += quadraticHashTable.build(dataset);
                quadraticBuildTime += System.currentTimeMillis() - quadraticBuildStart;

                IPerfectHashTable linearHashTable = new LinearSpaceHashTable();
                long linearBuildStart = System.currentTimeMillis();
                linearRebuilds += linearHashTable.build(dataset);
                linearBuildTime += System.currentTimeMillis() - linearBuildStart;
            }
            double quadraticBuildTimeAvg = quadraticBuildTime / (double) ITERATIONS;
            double linearBuildTimeAvg = linearBuildTime / (double) ITERATIONS;
            System.out.printf("%-10d | %-25.2f | %-25.2f | %-20d | %-20d%n",
                    size, quadraticBuildTimeAvg, linearBuildTimeAvg, quadraticRebuilds, linearRebuilds);
        }
    }

    @Test
    public void testSpaceConsumption(){
        System.out.println("\n=== Space Consumption Test ===");
        System.out.printf("%-10s | %-25s | %-25s%n",
                "Size", "Quadratic Space", "Linear Space");
        System.out.println("-".repeat(85));

        for (int size : DATASET_SIZES) {
            List<String> data = generateDataset(size);

            IPerfectHashTable quadraticHashTable = new QuadraticSpaceHashTable();
            quadraticHashTable.build(data);
            int quadraticSpace = quadraticHashTable.getSpace();

            IPerfectHashTable linearHashTable = new LinearSpaceHashTable();
            linearHashTable.build(data);
            int linearSpace = linearHashTable.getSpace();

            System.out.printf("%-10d | %-25d | %-25d%n",
                    size, quadraticSpace, linearSpace);
        }
    }
}
