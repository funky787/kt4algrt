package org.example.kt4;

import java.io.*;
import java.util.*;

public class Main {

    static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0;
        private int len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;

                if (len <= 0) {
                    return -1;
                }
            }

            return buffer[ptr++];
        }

        long nextLong() throws IOException {
            int c;

            do {
                c = read();
            } while (c <= ' ');

            boolean negative = false;

            if (c == '-') {
                negative = true;
                c = read();
            }

            long value = 0;

            while (c > ' ') {
                value = value * 10 + (c - '0');
                c = read();
            }

            return negative ? -value : value;
        }

        int nextInt() throws IOException {
            return (int) nextLong();
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();

        System.out.println("Введите номер задачи (1-6):");
        int task = fs.nextInt();

        switch (task) {
            case 1 -> task1(fs);
            case 2 -> task2(fs);
            case 3 -> task3(fs);
            case 4 -> task4(fs);
            case 5 -> task5(fs);
            case 6 -> task6(fs);
            default -> System.out.println("Нет такой задачи");
        }
    }

    static void task1(FastScanner fs) throws Exception {
        int n = fs.nextInt();

        long[] value = new long[n];
        int[] left = new int[n];
        int[] right = new int[n];

        for (int i = 0; i < n; i++) {
            value[i] = fs.nextLong();
            left[i] = fs.nextInt();
            right[i] = fs.nextInt();
        }

        int[] queue = new int[n];
        int head = 0;
        int tail = 0;

        queue[tail++] = 0;

        StringBuilder result = new StringBuilder();

        while (head < tail) {
            int levelSize = tail - head;
            long sum = 0;

            for (int i = 0; i < levelSize; i++) {
                int vertex = queue[head++];
                sum += value[vertex];

                if (left[vertex] != -1) {
                    queue[tail++] = left[vertex];
                }

                if (right[vertex] != -1) {
                    queue[tail++] = right[vertex];
                }
            }

            if (result.length() > 0) {
                result.append(" ");
            }

            result.append(sum);
        }

        System.out.println(result);
    }

    static void task2(FastScanner fs) throws Exception {
        int n = fs.nextInt();
        int k = fs.nextInt();

        HashMap<Long, Integer> frequencies = new HashMap<>();

        for (int i = 0; i < n; i++) {
            long number = fs.nextLong();
            frequencies.put(number, frequencies.getOrDefault(number, 0) + 1);
        }

        ArrayList<Map.Entry<Long, Integer>> numbers =
                new ArrayList<>(frequencies.entrySet());

        numbers.sort((a, b) -> {
            int compareFrequency = Integer.compare(b.getValue(), a.getValue());

            if (compareFrequency != 0) {
                return compareFrequency;
            }

            return Long.compare(a.getKey(), b.getKey());
        });

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < k; i++) {
            if (i > 0) {
                result.append(" ");
            }

            result.append(numbers.get(i).getKey());
        }

        System.out.println(result);
    }

    static void task3(FastScanner fs) throws Exception {
        int n = fs.nextInt();

        PriorityQueue<Long> left =
                new PriorityQueue<>(Comparator.reverseOrder());
        PriorityQueue<Long> right =
                new PriorityQueue<>();

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < n; i++) {
            long number = fs.nextLong();

            if (left.isEmpty() || number <= left.peek()) {
                left.add(number);
            } else {
                right.add(number);
            }

            if (left.size() > right.size() + 1) {
                right.add(left.poll());
            }

            if (right.size() > left.size()) {
                left.add(right.poll());
            }

            if (i > 0) {
                result.append(" ");
            }

            if (left.size() > right.size()) {
                result.append(left.peek()).append(".0");
            } else {
                appendAverage(result, left.peek(), right.peek());
            }
        }

        System.out.println(result);
    }

    static void task4(FastScanner fs) throws Exception {
        int n = fs.nextInt();

        long[] value = new long[n];
        int[] left = new int[n];
        int[] right = new int[n];

        for (int i = 0; i < n; i++) {
            value[i] = fs.nextLong();
            left[i] = fs.nextInt();
            right[i] = fs.nextInt();
        }

        int[] stack = new int[n];
        int[] order = new int[n];

        int stackSize = 0;
        int orderSize = 0;

        stack[stackSize++] = 0;

        while (stackSize > 0) {
            int vertex = stack[--stackSize];
            order[orderSize++] = vertex;

            if (left[vertex] != -1) {
                stack[stackSize++] = left[vertex];
            }

            if (right[vertex] != -1) {
                stack[stackSize++] = right[vertex];
            }
        }

        long[] bestDown = new long[n];
        long answer = Long.MIN_VALUE;

        for (int i = orderSize - 1; i >= 0; i--) {
            int vertex = order[i];

            long leftSum = 0;
            long rightSum = 0;

            if (left[vertex] != -1) {
                leftSum = Math.max(0, bestDown[left[vertex]]);
            }

            if (right[vertex] != -1) {
                rightSum = Math.max(0, bestDown[right[vertex]]);
            }

            long path = value[vertex] + leftSum + rightSum;
            answer = Math.max(answer, path);

            bestDown[vertex] =
                    value[vertex] + Math.max(leftSum, rightSum);
        }

        System.out.println(answer);
    }

    static class PairNode {
        int i;
        int j;
        long sum;

        PairNode(int i, int j, long sum) {
            this.i = i;
            this.j = j;
            this.sum = sum;
        }
    }

    static void task5(FastScanner fs) throws Exception {
        int n = fs.nextInt();
        int m = fs.nextInt();
        int k = fs.nextInt();

        long[] a = new long[n];
        long[] b = new long[m];

        for (int i = 0; i < n; i++) {
            a[i] = fs.nextLong();
        }

        for (int i = 0; i < m; i++) {
            b[i] = fs.nextLong();
        }

        if (a.length > b.length) {
            long[] temp = a;
            a = b;
            b = temp;
        }

        PriorityQueue<PairNode> queue =
                new PriorityQueue<>(Comparator.comparingLong(x -> x.sum));

        int rows = Math.min(a.length, k);

        for (int i = 0; i < rows; i++) {
            queue.add(new PairNode(i, 0, a[i] + b[0]));
        }

        long answer = 0;

        for (int count = 0; count < k; count++) {
            PairNode current = queue.poll();
            answer = current.sum;

            if (current.j + 1 < b.length) {
                int nextJ = current.j + 1;

                queue.add(
                        new PairNode(
                                current.i,
                                nextJ,
                                a[current.i] + b[nextJ]
                        )
                );
            }
        }

        System.out.println(answer);
    }

    static class SlidingMedian {
        TreeMap<Long, Integer> left = new TreeMap<>();
        TreeMap<Long, Integer> right = new TreeMap<>();

        int leftSize = 0;
        int rightSize = 0;

        void add(long number) {
            if (leftSize == 0 || number <= left.lastKey()) {
                addToMap(left, number);
                leftSize++;
            } else {
                addToMap(right, number);
                rightSize++;
            }

            balance();
        }

        void remove(long number) {
            if (left.containsKey(number)) {
                removeFromMap(left, number);
                leftSize--;
            } else {
                removeFromMap(right, number);
                rightSize--;
            }

            balance();
        }

        void balance() {
            while (leftSize > rightSize + 1) {
                long number = left.lastKey();

                removeFromMap(left, number);
                leftSize--;

                addToMap(right, number);
                rightSize++;
            }

            while (leftSize < rightSize) {
                long number = right.firstKey();

                removeFromMap(right, number);
                rightSize--;

                addToMap(left, number);
                leftSize++;
            }
        }

        long leftMiddle() {
            return left.lastKey();
        }

        long rightMiddle() {
            return right.firstKey();
        }

        void addToMap(TreeMap<Long, Integer> map, long number) {
            map.put(number, map.getOrDefault(number, 0) + 1);
        }

        void removeFromMap(TreeMap<Long, Integer> map, long number) {
            int count = map.get(number);

            if (count == 1) {
                map.remove(number);
            } else {
                map.put(number, count - 1);
            }
        }
    }

    static void task6(FastScanner fs) throws Exception {
        int n = fs.nextInt();
        int k = fs.nextInt();

        long[] numbers = new long[n];

        for (int i = 0; i < n; i++) {
            numbers[i] = fs.nextLong();
        }

        SlidingMedian median = new SlidingMedian();

        for (int i = 0; i < k; i++) {
            median.add(numbers[i]);
        }

        StringBuilder result = new StringBuilder();

        for (int start = 0; start <= n - k; start++) {
            if (start > 0) {
                result.append(" ");
            }

            if (k % 2 == 1) {
                result.append(median.leftMiddle()).append(".0");
            } else {
                appendAverage(
                        result,
                        median.leftMiddle(),
                        median.rightMiddle()
                );
            }

            if (start + k < n) {
                median.remove(numbers[start]);
                median.add(numbers[start + k]);
            }
        }

        System.out.println(result);
    }

    static void appendAverage(StringBuilder result, long a, long b) {
        long sum = a + b;

        if (sum % 2 == 0) {
            result.append(sum / 2).append(".0");
        } else if (sum > 0) {
            result.append(sum / 2).append(".5");
        } else {
            result.append("-")
                    .append(Math.abs(sum) / 2)
                    .append(".5");
        }
    }
}
