package lu.kolja.percentagedura;

import java.text.DecimalFormat;

public class NumberUtil {
    private final static String[] SUFFIXES = {"", "K", "M", "B", "T", "P", "E", "Z", "Y"};
    /**
     * Formats positive numbers with suffixes (K, M, B, T, etc.) with one decimal place.
     * Negative numbers will be returned as-is without formatting.
     */
    public static String formatNumber(double number) {
        // Early return for negative numbers - no formatting applied
        if (number < 0) {
            return String.valueOf(number);
        }

        int suffixIndex = 0;

        while (number >= 1000 && suffixIndex < SUFFIXES.length - 1) {
            number /= 1000;
            suffixIndex++;
        }

        // Truncate to two decimal places
        double truncated = Math.floor(number * 100) / 100.0;

        // Format without trailing zeros
        DecimalFormat df = new DecimalFormat("#.##");
        String result = df.format(truncated);

        return result + SUFFIXES[suffixIndex];
    }

    /**
     * Formats a number as a percentage with one decimal place.
     * @param number The number to format as percentage (where 1.0 = 100%)
     * @return Formatted percentage string with one decimal place
     */
    public static String formatPercentage(double number) {
        double percentage = number * 100;

        String result = String.format("%.1f", Math.round(percentage * 10) / 10.0);

        // Remove trailing zero if decimal part is exactly .0
        if (result.endsWith(".0")) {
            result = result.substring(0, result.length() - 2);
        }

        return result + "%";
    }
}
