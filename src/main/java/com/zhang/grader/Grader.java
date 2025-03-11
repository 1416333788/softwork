package com.zhang.grader;

import com.zhang.model.Fraction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grader {

    /**
     * 评估用户答案并生成评分报告
     * @param exerciseFile 题目文件路径
     * @param answerFile 答案文件路径
     */
    public void grade(String exerciseFile, String answerFile) throws IOException {
        List<String> exercises = readLines(exerciseFile);
        List<String> userAnswers = readLines(answerFile);

        List<Integer> correctProblems = new ArrayList<>(); // 正确题目编号列表
        List<Integer> wrongProblems = new ArrayList<>(); // 错误题目编号列表

        // 检查题目和答案数量是否匹配
        int minSize = Math.min(exercises.size(), userAnswers.size());

        for (int i = 0; i < minSize; i++) {
            String exercise = exercises.get(i);
            String userAnswer = userAnswers.get(i);

            // 提取题目编号和期望答案
            int problemNumber = extractProblemNumber(exercise);
            Fraction expectedAnswer = calculateAnswer(exercise);
            Fraction providedAnswer = parseAnswer(userAnswer);

            // 判断答案是否正确
            if (expectedAnswer != null && providedAnswer != null &&
                    expectedAnswer.equals(providedAnswer)) {
                correctProblems.add(problemNumber);
            } else {
                wrongProblems.add(problemNumber);
            }
        }

        // 将评分结果写入Grade.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter("Grade.txt"))) {
            writer.println("Correct: " + correctProblems.size() + " " + formatNumberList(correctProblems));
            writer.println("Wrong: " + wrongProblems.size() + " " + formatNumberList(wrongProblems));
        }

        System.out.println("评分完成。结果已保存到Grade.txt");
    }

    /**
     * 读取文件中的所有行
     */
    private List<String> readLines(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * 从行中提取题目编号
     */
    private int extractProblemNumber(String line) {
        Pattern pattern = Pattern.compile("^(\\d+)\\.");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    /**
     * 计算题目的答案
     * 注：完整实现需要一个表达式解析器
     * 这里简单返回null作为占位符
     */
    private Fraction calculateAnswer(String exercise) {
        // 需要实现表达式解析和求值
        // 完整实现需要表达式解析器
        return null;
    }

    /**
     * 解析答案行中的分数
     */
    private Fraction parseAnswer(String answerLine) {
        // 提取答案部分（题号之后的内容）
        Pattern pattern = Pattern.compile("^\\d+\\. (.+)$");
        Matcher matcher = pattern.matcher(answerLine);
        if (matcher.find()) {
            String answerStr = matcher.group(1).trim();
            return Fraction.parse(answerStr);
        }
        return null;
    }

    /**
     * 格式化数字列表为(1, 2, 3)格式
     */
    private String formatNumberList(List<Integer> numbers) {
        if (numbers.isEmpty()) {
            return "()";
        }

        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < numbers.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(numbers.get(i));
        }
        sb.append(")");
        return sb.toString();
    }
}
