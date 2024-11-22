package org.example.newjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.*;

@SpringBootApplication
public class NewJobApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(NewJobApplication.class, args);

        if (args.length != 1) {
            System.out.println("Usage: java -jar new-job.jar <lng.txt>");
            return;
        }

        String inputFileName = args[0];
        long startTime = System.currentTimeMillis();

        Set<String> uniqueLines = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isValidLine(line)) {
                    uniqueLines.add(line);
                }
            }
        }

        Map<String, List<String>> groups = new HashMap<>();
        for (String line : uniqueLines) {
            String[] values = line.split(";");
            for (String value : values) {
                if (!value.isEmpty()) {
                    groups.computeIfAbsent(value, k -> new ArrayList<>()).add(line);
                }
            }
        }

        List<List<String>> finalGroups = new ArrayList<>();
        for (List<String> lines : groups.values()) {
            if (lines.size() > 1) {
                finalGroups.add(new ArrayList<>(new HashSet<>(lines)));
            }
        }

        finalGroups.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            writer.write("Number of groups with more than one element: " + finalGroups.size() + "\n\n");
            for (int i = 0; i < finalGroups.size(); i++) {
                writer.write("Group " + (i + 1) + "\n");
                for (String groupLine : finalGroups.get(i)) {
                    writer.write(groupLine + "\n");
                }
                writer.write("\n");
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Number of groups with more than one element: " + finalGroups.size());
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }

    private static boolean isValidLine(String line) {
        return !line.matches(".*\".*\".*") && !line.isEmpty();
    }
}