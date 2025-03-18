public class StringReverse {
    public static void main(String[] args) {
        String input = "Hello World";
        char[] chars = input.toCharArray();
        int left = 0;
        int right = chars.length - 1;

        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }

        String reversed = new String(chars);
        System.out.println("Original: " + input);
        System.out.println("Reversed: " + reversed);
    }
}