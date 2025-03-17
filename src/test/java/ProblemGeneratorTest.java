

import com.zhang.generator.ProblemGenerator;
import com.zhang.model.Expression;
import com.zhang.model.Fraction;
import com.zhang.model.Problem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProblemGeneratorTest {
    private ProblemGenerator generator;

    @BeforeEach
    public void setUp() {
        // 在每个测试前初始化 ProblemGenerator，范围设为 10
        generator = new ProblemGenerator(10);
    }


    @Test
    public void testGenerateUniqueProblemNotNull() {
        // 测试生成单个唯一题目是否成功
        Problem problem = generator.generateUniqueProblem();
        assertNotNull(problem, "生成的题目不应为空");
        assertNotNull(problem.getExpression(), "题目表达式不应为空");
        assertNotNull(problem.getAnswer(), "题目答案不应为空");
    }

    @Test
    public void testGenerateProblemsNoDuplicates() throws IOException {
        // 生成 5 个题目并检查是否有重复
        generator.generateProblems(5);

        // 读取生成的 Exercises.txt 文件
        List<String> exercises = Files.readAllLines(Path.of("Exercises.txt"));
        assertEquals(exercises.size(), exercises.stream().distinct().count(), "不应有重复题目");
    }

    @Test
    public void testGenerateProblemsWithValidExpressions() throws IOException {
        // 生成 3 个题目并验证每个表达式都包含运算符
        generator.generateProblems(3);

        // 读取 Exercises.txt 文件
        List<String> exercises = Files.readAllLines(Path.of("Exercises.txt"));
        for (String line : exercises) {
            String expression = line.split("=")[0].trim().substring(3); // 提取表达式部分，去掉编号
            assertTrue(expression.contains("+") || expression.contains("-") ||
                            expression.contains("×") || expression.contains("÷"),
                    "表达式应至少包含一个运算符: " + expression);
        }
    }

    @Test
    public void testGenerateProblemsCountMatchesRequest() throws IOException {
        // 请求生成 5 个题目，验证生成数量是否正确
        int requestedCount = 5;
        generator.generateProblems(requestedCount);

        // 读取文件并检查题目数量
        List<String> exercises = Files.readAllLines(Path.of("Exercises.txt"));
        assertEquals(requestedCount, exercises.size(), "生成题目数量应与请求一致");
    }
}