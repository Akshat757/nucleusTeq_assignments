public class AnagramChecker {
    public static void main(String[] args) {
        String s1 = "listen";
        String s2 = "silent";
        char[] arr1 = s1.toCharArray();
        char[] arr2 = s2.toCharArray();
        java.util.Arrays.sort(arr1);
        java.util.Arrays.sort(arr2);
        System.out.println(java.util.Arrays.equals(arr1, arr2));
    }
}