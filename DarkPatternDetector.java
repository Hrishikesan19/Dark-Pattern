import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DarkPatternDetector {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a URL: ");
        String url = scanner.nextLine();

        // Fetch the page using Jsoup
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select("a, button, label, span, div");

        int total = 0;
        int detected = 0;

        for (Element el : elements) {
            String text = el.text().trim();

            // Skip very short or empty elements
            if (!text.isEmpty() && text.length() > 10) {
                total++;
                System.out.println("\n[" + total + "] Text: \"" + text + "\"");

                String[] result = classifyWithPythonAPI(text);
                String label = result[0];
                double confidence = Double.parseDouble(result[1]);

                System.out.printf("→ Pattern: %s (%.0f%% confidence)\n", label, confidence * 100);

                // Count as detected if confidence > 80%
                if (confidence > 0.8) {
                    detected++;
                }
            }
        }

        double percentage = (total == 0) ? 0 : ((double) detected / total) * 100;
        System.out.printf("\n✅ Total Elements: %d\n❌ Dark Patterns Detected: %d\n🎯 Detection Rate: %.2f%%\n",
                total, detected, percentage);
    }

    public static String[] classifyWithPythonAPI(String text) {
        try {
            URL url = new URL("http://127.0.0.1:8000/classify");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = "{\"text\": \"" + text.replace("\"", "\\\"") + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read the response
            Scanner scanner = new Scanner(conn.getInputStream(), "utf-8");
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            // Extract label and confidence from the response
            String json = response.toString();
            String label = extractJsonValue(json, "label");
            String confidenceStr = extractJsonValue(json, "confidence");

            return new String[] { label, confidenceStr };

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return new String[] { "none", "0.0" };
        }
    }

    public static String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\":";
        int index = json.indexOf(search);
        if (index != -1) {
            int start = index + search.length();
            char quote = json.charAt(start);
            if (quote == '"') {
                int end = json.indexOf("\"", start + 1);
                return json.substring(start + 1, end);
            } else {
                int end = json.indexOf(",", start);
                if (end == -1)
                    end = json.indexOf("}", start);
                return json.substring(start, end).trim();
            }
        }
        return "";
    }
}