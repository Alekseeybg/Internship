import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //a)
        System.out.print("Enter how many numbers you want to enter: ");
        int m = sc.nextInt();
        int[] arr = new int[m];
        System.out.print("Enter the numbers: ");
        int max = arr[0] = sc.nextInt();
        for (int i = 1; i < arr.length; i++) {
            arr[i] = sc.nextInt();
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        System.out.println("Max number: " + max);

        //b)
        System.out.print("Enter how many numbers you want to enter: ");
        int m2 = sc.nextInt();
        int[] arr2 = new int[m2];
        System.out.print("Enter the numbers: ");
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = sc.nextInt();
        }
        Arrays.sort(arr2);
        for (int el : arr2) {
            System.out.print(el + " ");
        }

        //c)
        System.out.print("Enter how many numbers you want to enter: ");
        int m3 = sc.nextInt();
        int[] arr3 = new int[m3];
        System.out.print("Enter the numbers: ");
        for (int i = 0; i < arr3.length; i++) {
            arr3[i] = sc.nextInt();
        }
        StringBuilder clusters = new StringBuilder();

        for (int i = 0; i < arr3.length; i++) {
            for (int j = i + 1; j < arr3.length; j++) {
                if (arr3[i] == arr3[j]) {
                    clusters.append(arr3[j]).append(" ");
                    break;
                }

                if (arr3[i] != arr3[j]) {
                    break;
                }
            }
        }
        String[] result = clusters.toString().split("\\s");
        System.out.println(result.length);

        //d)
        System.out.print("Enter how many numbers you want to enter: ");
        int m4 = sc.nextInt();
        int[] arr4 = new int[m4];
        System.out.print("Enter the numbers: ");
        for (int i = 0; i < arr4.length; i++) {
            arr4[i] = sc.nextInt();
        }
        int sumLeft = 0;
        int sumRight;
        int index = -1;
        for (int i = 0; i < arr4.length; i++) {
            sumLeft += arr4[i];
            sumRight = 0;

            for (int j = arr4.length - 1; j >= 0; j--) {
                sumRight += arr4[j];
                if (i > 0 && j < arr4.length - 1) {
                    if (sumLeft == sumRight) {
                        index = i;
                        break;
                    }
                }
            }
            if (index != -1) {
                System.out.println(index);
                break;
            }
        }
        if (index == -1) {
            System.out.println("NO");
        }
        //e)
        System.out.print("Enter how many numbers you want to enter: ");
        int m5 = sc.nextInt();
        int[] arr5 = new int[m5];
        System.out.print("Enter the numbers: ");
        for (int i = 0; i < arr5.length; i++) {
            arr5[i] = sc.nextInt();
        }
        StringBuilder sequence = new StringBuilder();
        StringBuilder longestSequence = new StringBuilder();
        sequence.append(arr5[0]).append(" ");
        for (int i = 1; i < arr5.length; i++) {
            if (arr5[i] > arr5[i - 1]) {
                sequence.append(arr5[i]).append(" ");
            } else {
                sequence.setLength(0);
                sequence.append(arr5[i]).append(" ");
            }
            if (sequence.length() > longestSequence.length()) {
                longestSequence.setLength(0);
                longestSequence.append(sequence);
            }
        }
        System.out.println(longestSequence);

        //f)


        //g)
        System.out.print("Enter how many numbers you want to enter: ");
        int m6 = sc.nextInt();
        int[] arr6 = new int[m6];
        System.out.print("Enter the numbers: ");
        for (int i = 0; i < arr6.length; i++) {
            arr6[i] = sc.nextInt();
        }

        int sum = 0;
        int maxSum = 0;
        int start = 0;
        int end = 0;

        for (int i = 0; i < arr6.length; i++) {
            sum += arr6[i];
            if (sum > maxSum) {
                maxSum = sum;
                end = i;
            } else if (sum < 0) {
                sum = 0;
                start = i;
            }
        }

        System.out.print("The sequence with the largest sum is: ");
        for (int i = start + 1; i <= end; i++) {
            System.out.print(arr6[i] + " ");
        }

        //h)
        System.out.print("Enter how many numbers you want to enter: ");
        int m7 = sc.nextInt();
        int[] arr7 = new int[m7];
        System.out.print("Enter the numbers: ");
        for (int i = 0; i < arr7.length; i++) {
            arr7[i] = sc.nextInt();
        }

        int number = 0;
        int count = 0;

        for (int i = 0; i < arr7.length; i++) {
            for (int j = i + 1; j < arr7.length; j++) {

                if (arr7[i] == arr7[j]) {
                    number = arr7[i];
                    count++;
                } else {
                    count = 0;
                }
            }
            count++;
            if (count == 1) {
                System.out.println(number);
                break;
            } else {
                count = 0;
            }
        }

    }
}
