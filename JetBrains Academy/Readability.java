package readability;

import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    static class Readability {
        int chars;
        int words;
        int sents;
        int sylls;
        int polys;

        Readability(int chars, int words, int sents, int sylls, int polys) {
            this.chars = chars;
            this.words = words;
            this.sents = sents;
            this.sylls = sylls;
            this.polys = polys;
        }

        double getARScore() {
            return 4.71 * this.chars / this.words + 0.5 * this.words / this.sents - 21.43;
        }

        double getFKScore() {
            return 0.39 * this.words / this.sents + 11.8 * this.sylls / this.words - 15.59;
        }

        double getSMOGScore() {
            return 1.043 * Math.sqrt((double) this.polys * 30 / this.sents) + 3.1291;
        }

        double getCLScore() {
            return 0.0588 * this.chars / this.words * 100  - 0.296 * this.sents / this.words * 100 - 15.8;
        }

        int getAppropriateAge(double score) {
            int value = (int) Math.ceil(score);

            if (value < 3) {
                return value + 5;
            } else if (value > 12) {
                return value + 11;
            } else {
                return value + 6;
            }
        }

        double getAvgAge() {
            int ageAR = getAppropriateAge(getARScore());
            int ageFK = getAppropriateAge(getFKScore());
            int ageSM = getAppropriateAge(getSMOGScore());
            int ageCL = getAppropriateAge(getCLScore());

            return (double) (ageAR + ageFK + ageSM + ageCL) / 4;
        }

        String getScoreReport(String option, double score) {
            String preface;
            int age;
            switch (option) {
                case "ARI":
                    preface = "Automated Readability Index: ";
                    age = getAppropriateAge(score);
                    break;
                case "FK":
                    preface = "Flesch-Kincaid readability tests: ";
                    age = getAppropriateAge(score);
                    break;
                case "SMOG":
                    preface = "Simple Measure of Gobbledygook: ";
                    age = getAppropriateAge(score);
                    break;
                case "CL":
                    preface = "Coleman-Liau index: ";
                    age = getAppropriateAge(score);
                    break;
                default:
                    preface = "Unknown test: ";
                    age = getAppropriateAge(score);
            }

            String post = String.format("%.2f (about %d-year-olds).", score, age);

            return preface + post;
        }
    }

    public static String readFile(String filepath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filepath)));
    }

    public static int[] getSyllableCounts(String data) {
        String[] words = data.split("\\s+");
        int[] counts = new int[words.length];
        int preLength;
        int postLength;

        for (int i = 0; i < counts.length; i++) {
            String word = words[i];
            String temp = word.replaceAll("[.!?,'\"]", "")
                    .replaceAll("[eE]\\b", "")
                    .replaceAll("[aeiouAEIOU]{2}","a")
                    .replaceAll("[eiouy]", "a");

            preLength = temp.length();
            postLength = temp.replaceAll("a", "").length();
            counts[i] = preLength - postLength == 0 ? 1 : preLength - postLength;
        }

        return counts;
    }

    public static void main(String[] args) {
        try {
            String data = readFile(args[0]);

            int wordCount = data.split("\\s+").length;
            int sentenceCount = data.split("\\b+[.!?]").length;
            int charCount = data.replaceAll("\\s+", "").length();
            int syllCount = Arrays.stream(getSyllableCounts(data)).sum();
            int polySyllCount = Arrays.stream(getSyllableCounts(data)).filter(x -> x > 2).toArray().length;

            Readability score = new Readability(charCount, wordCount, sentenceCount, syllCount, polySyllCount);

            System.out.println("Words: " + wordCount + "\nSentences: " + sentenceCount + "\nCharacters: " + charCount);
            System.out.println("Syllables: " + syllCount + "\nPolysyllables: " + polySyllCount);

            System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            Scanner scanner = new Scanner(System.in);
            String option = scanner.next();
            String result;

            switch (option) {
                case "ARI": result = score.getScoreReport(option, score.getARScore());
                break;
                case "FK": result = score.getScoreReport(option, score.getFKScore());
                break;
                case "SMOG": result = score.getScoreReport(option, score.getSMOGScore());
                break;
                case "CL": result = score.getScoreReport(option, score.getCLScore());
                break;
                default:
                    result = score.getScoreReport("ARI", score.getARScore()) + "\n" +
                             score.getScoreReport("FK", score.getFKScore()) + "\n" +
                             score.getScoreReport("SMOG", score.getSMOGScore()) + "\n" +
                             score.getScoreReport("CL", score.getCLScore()) + "\n\n" +
                             String.format("This text should be understood in average by %.2f-year-olds.",
                                     score.getAvgAge());
                break;
            }

            System.out.println("\n" + result);
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }
}
