import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class BitSetSort {

    public static void main(String[] args) {
        Integer[] nums = generateNumber(100000);
        System.out.println(sortByBitSet(nums));
        System.out.println(sort(nums));
    }

    private static Integer[] generateNumber(int size) {
        long start = System.currentTimeMillis();
        System.out.println("Initializing Data...");
        HashSet<Integer> hs = new HashSet<>();
        for (int i = 0; i < size; i++) {
            hs.add((int) (Math.random() * size));
        }
        System.out.println("Data prepared,time cost:" + (System.currentTimeMillis() - start) + "ms");
        List<Integer> tmpList = Arrays.asList(hs.toArray(new Integer[0]));
        Collections.shuffle(tmpList);
        System.out.println(tmpList);
        return tmpList.toArray(new Integer[0]);
    }


    private static String sortByBitSet(Integer[] nums) {
        long start = System.currentTimeMillis();
        System.out.println("Start BitSet Sort, total: " + nums.length);
        List<Integer> result = new ArrayList<>();

        BitSet bitSet = new BitSet();
        for (int i = 0; i < nums.length; i++) {
            bitSet.set(nums[i], true);
        }
        for (int i = 0; i < bitSet.length(); i++) {
            if (bitSet.get(i)) {
                result.add(i);
            }
        }
        System.out.println("Done!!! time:" + (System.currentTimeMillis() - start) + "ms");
        return result.toString();
    }


    private static List<Integer> sort(Integer[] nums) {
        long start = System.currentTimeMillis();
        System.out.println("Start Arrays.sort, total: " + nums.length);
        Arrays.sort(nums);
        System.out.println("Done!!! time:" + (System.currentTimeMillis() - start) + "ms");
        return Arrays.stream(nums).collect(Collectors.toList());
    }

}