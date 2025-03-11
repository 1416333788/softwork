package com.zhang.generator;

import com.zhang.model.Expression;
import com.zhang.model.Fraction;
import com.zhang.model.Problem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProblemGenerator {
    private final ExpressionGenerator expressionGenerator; // 表达式生成器
    private final Set<String> problemSignatures = new HashSet<>(); // 用于存储已生成题目的签名，防止重复

    public ProblemGenerator(int range) {
        this.expressionGenerator = new ExpressionGenerator(range);
    }

    /**
     * 生成指定数量的题目并写入文件
     * @param count 题目数量
     */
    public void generateProblems(int count) throws IOException {
        List<Problem> problems = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Problem problem = generateUniqueProblem();
            problems.add(problem);

            // 如果经过多次尝试后仍难以生成唯一的题目，则退出循环
            if (problem == null) {
                break;
            }
        }

        // 将题目写入文件
        try (PrintWriter exerciseWriter = new PrintWriter(new FileWriter("Exercises.txt"));
             PrintWriter answerWriter = new PrintWriter(new FileWriter("Answers.txt"))) {

            for (int i = 0; i < problems.size(); i++) {
                Problem problem = problems.get(i);
                exerciseWriter.println((i + 1) + ". " + problem.getExpression() + " = ");
                answerWriter.println((i + 1) + ". " + problem.getAnswer());
            }
        }

        System.out.println("已生成 " + problems.size() + " 道题目。");
        System.out.println("题目已保存到 Exercises.txt");
        System.out.println("答案已保存到 Answers.txt");
    }

    /**
     * 生成一个唯一的题目（不与之前生成的重复）
     */
    private Problem generateUniqueProblem() {
        // 生成唯一题目的最大尝试次数
        final int MAX_ATTEMPTS = 1000;

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            // 生成一个最多包含3个运算符的表达式
            Expression expression = expressionGenerator.generateExpression(3);
            String signature = getCanonicalForm(expression);

            // 检查题目是否重复
            if (!problemSignatures.contains(signature)) {
                problemSignatures.add(signature);
                Fraction answer = expression.evaluate();
                return new Problem(expression, answer);
            }
        }

        System.err.println("经过多次尝试后，无法生成更多的唯一题目。");
        return null;
    }

    /**
     * 获取表达式的规范形式，用于检测重复题目
     * 注意：这是一个简化的实现，完整的解决方案应使用更复杂的算法来检测代数等价表达式
     */
    private String getCanonicalForm(Expression expression) {
        return expression.toString();
    }
}
