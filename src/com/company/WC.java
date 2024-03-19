package com.company;

import java.io.*;

public class WC {
    private static boolean noArgs;
    private static boolean basicArg;
    private static boolean pipeArg;
    public static void start() throws IOException {
        while(true) {
            noArgs = false;
            basicArg = false;
            pipeArg = false;
            System.out.print('>');
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String command = reader.readLine();
            if(command.length() == 0) continue;
            validateCommand(command);
            if(!noArgs && !basicArg && !pipeArg) continue;
            String[] cmdArgs = command.split(" ");

            if(noArgs) {
                runNoArgCommand(cmdArgs[1]);
            }
            if(basicArg) {
                runArgCommand(cmdArgs[2], cmdArgs[1]);
            }
            if(pipeArg) {
                String filePath = cmdArgs[1];
                if(cmdArgs.length > 5) {
                    System.out.println("Invalid command");
                }
                else if(cmdArgs.length == 4) {
                    runNoArgCommand(filePath);
                }
                else {
                    runArgCommand(filePath, cmdArgs[4]);
                }
            }
        }
    }

    private static void validateCommand(String command) {
        String[] cmdArgs = command.split(" ");
        if(cmdArgs.length < 3) {
            if(!cmdArgs[0].equals("ccwc")) {
                System.out.println("Unknown command "+cmdArgs[0]);
            }
            else if(cmdArgs.length == 1) {
                printInstructions();
            }
            else {
                if(cmdArgs[1].matches("^-[a-zA-Z]$")) {
                    System.out.println("No file argument passed!");
                    System.out.println("Expected format is ccwc -[c|l|m|w] file");
                }
                else {
                    noArgs = true;
                }
            }
        }
        else if(cmdArgs.length > 3) {
            if(!cmdArgs[0].equals("cat")) {
                System.out.println("Invalid command format!");
            }
            else if(!cmdArgs[2].equals("|")) {
                System.out.println("Invalid pipe argument "+cmdArgs[2]);
            }
            else if(!cmdArgs[3].equals("ccwc")) {
                System.out.println("Unknown command "+cmdArgs[3]);
            }
            else if(cmdArgs.length > 4 && !cmdArgs[4].matches("^-[a-zA-Z]$")) {
                System.out.println("Invalid argument "+cmdArgs[4]);
                System.out.println("Possible options are -c, -l, -w, -m");
            }
            else {
                pipeArg = true;
            }
        }
        else {
            if(!cmdArgs[0].equals("ccwc")) {
                System.out.println("Unknown command "+cmdArgs[0]);
            }
            else if(!cmdArgs[1].matches("^-[a-zA-Z]$")) {
                System.out.println("Invalid argument "+cmdArgs[1]);
                System.out.println("Possible options are -c, -l, -w, -m");
            }
            else {
                basicArg = true;
            }
        }
    }

    private static int countBytes(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(filePath);
        return inputStream.available();
    }

    private static int countLines(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }

    private static int countWords(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int words = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            if(line.trim().length() != 0) {
                words += line.trim().split("\\s+").length;
            }
        }
        reader.close();
        return words;
    }

    private static int countCharacters(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int chars = 0;
        while ((line = reader.readLine()) != null) {
            chars += line.length()+2;
        }
        reader.close();
        return chars;
    }

    private static void runNoArgCommand(String filePath) {
        try {
            int bytes = countBytes(filePath);
            int lines = countLines(filePath);
            int words = countWords(filePath);
            System.out.println(lines + " " + words + " " + bytes + " " + filePath);
        }
        catch (FileNotFoundException e) {
            System.out.println("No file found at " + filePath);
        }
        catch (IOException e) {
            System.out.println("Error in reading the file at "+ filePath);
        }
    }

    private static void runArgCommand(String filePath, String option) {
        if(option.equals("-c")) {
            try {
                System.out.println(countBytes(filePath) + " " + filePath);
            }
            catch (FileNotFoundException e) {
                System.out.println("No file found at " + filePath);
            }
            catch (IOException e) {
                System.out.println("Error in reading the file at "+ filePath);
            }
        }
        else if(option.equals("-l")) {
            try {
                System.out.println(countLines(filePath) + " " + filePath);
            }
            catch (FileNotFoundException e) {
                System.out.println("No file found at " + filePath);
            }
            catch (IOException e) {
                System.out.println("Error in reading the file at "+ filePath);
            }
        }
        else if(option.equals("-w")) {
            try {
                System.out.println(countWords(filePath) + " " + filePath);
            }
            catch (FileNotFoundException e) {
                System.out.println("No file found at " + filePath);
            }
            catch (IOException e) {
                System.out.println("Error in reading the file at "+ filePath);
            }
        }
        else if(option.equals("-m")) {
            try {
                System.out.println(countCharacters(filePath) + " " + filePath);
            }
            catch (FileNotFoundException e) {
                System.out.println("No file found at " + filePath);
            }
            catch (IOException e) {
                System.out.println("Error in reading the file at "+ filePath);
            }
        }
        else {
            System.out.println("Invalid argument "+option);
            System.out.println("Possible options are -c, -l, -w, -m");
        }
    }

    private static void printInstructions() {
        String instructions = "CCWC(1)                User Commands                CCWC(1)\n" +
                "\n" +
                "NAME\n" +
                "       ccwc - print newline, word, and byte counts for each file\n" +
                "\n" +
                "SYNOPSIS\n" +
                "       ccwc [OPTION]... [FILE]...\n" +
                "\n" +
                "DESCRIPTION\n" +
                "       Print newline, word, and byte counts for each FILE, and a total line if more than one FILE is specified.  A word is a non-zero-length\n" +
                "       sequence of characters delimited by white space.\n" +
                "\n" +
                "OPTIONS\n" +
                "       -c, --bytes\n" +
                "              print the byte counts\n" +
                "\n" +
                "       -m, --chars\n" +
                "              print the character counts\n" +
                "\n" +
                "       -l, --lines\n" +
                "              print the newline counts\n" +
                "\n" +
                "       -w, --words\n" +
                "              print the word counts\n" +
                "\n" +
                "EXAMPLES\n" +
                "       Count the number of lines, words, and bytes in a file:\n" +
                "\n" +
                "              wc filename\n" +
                "\n" +
                "       Count the number of lines in a file:\n" +
                "\n" +
                "              wc -l file1 \n";
        System.out.println(instructions);
    }

}
