

import com.zhang.generator.ExpressionGenerator;
import com.zhang.model.Expression;
import com.zhang.model.Fraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GeneratorTest {
    private ExpressionGenerator generator;

    @BeforeEach
    public void setUp() {
        // 在每个测试前初始化一个 ExpressionGenerator，范围设为 10
        generator = new ExpressionGenerator(10);
    }

    @Test
    public void testGenerateExpressionNotNull() {
        // 测试生成表达式是否非空
        Expression expr = generator.generateExpression(2);
        assertNotNull(expr, "生成的表达式不应为空");
    }

    @Test
    public void testExpressionEvaluation() {
        // 测试表达式是否可以正确求值
        Expression expr = generator.generateExpression(2);
        Fraction result = expr.evaluate();
        assertNotNull(result, "表达式求值结果不应为空");
        assertFalse(result.isNegative(), "表达式结果不应为负数");
    }

    @Test
    public void testMaxOperatorsLimit() {
        // 测试最大运算符数量限制
        Expression expr = generator.generateExpression(1);
        int operatorCount = countOperators(expr);
        assertTrue(operatorCount <= 1, "运算符数量应不超过指定最大值");
    }

    @Test
    public void testNoNegativeIntermediates() {
        // 测试表达式中间结果没有负数
        Expression expr = generator.generateExpression(2);
        assertFalse(hasNegativeIntermediates(expr), "表达式中间结果不应包含负数");
    }

    @Test
    public void testDivisionProperResult() {
        // 测试除法结果是真分数或整数
        Expression expr = generator.generateExpression(2);
        assertFalse(hasImproperDivision(expr), "除法结果应为真分数或整数");
    }

    // 辅助方法：计算表达式中的运算符数量
    private int countOperators(Expression expr) {
        if (expr.isLeaf()) {
            return 0;
        }
        return 1 + countOperators(expr.getLeft()) + countOperators(expr.getRight());
    }

    // 辅助方法：检查是否有负数中间结果
    private boolean hasNegativeIntermediates(Expression expr) {
        return generator.hasNegativeIntermediates(expr); // 复用原类方法
    }

    // 辅助方法：检查是否有非真分数的除法结果
    private boolean hasImproperDivision(Expression expr) {
        return generator.hasDivisionWithImproperResult(expr); // 复用原类方法
    }
}
