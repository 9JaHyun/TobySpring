package book.ch3.callback;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalcSumTestV2 {
    private CalculatorV2 calculator;
    private String filePath;

    @BeforeAll
    void init() {
        this.calculator = new CalculatorV2();
        this.filePath = "C:\\Users\\rnwkg\\codding\\ch3\\src\\test\\java\\book\\callback\\numbers.txt";

    }
    @DisplayName(value = "덧셈 테스트")
    @Test
    void sumOfNumbers() throws IOException {
        final int result = calculator.calcSum(filePath);
        Assertions.assertSame(15, result);
    }

    @DisplayName(value = "곱셈 테스트")
    @Test
    void multiOfNumbers() throws IOException {
        final int result = calculator.calcMulti(filePath);
        Assertions.assertSame(120, result);
    }
}

class CalculatorV2 {

    int calcSum(String filePath) {
        return lineReadTemplate(filePath, 0, (line, result) -> {
            return result + Integer.parseInt(line);
        });
    }


    // lineTemplate를 통해 더 코드 재사용성이 좋아진다.
   int calcMulti(String filePath) {
       return lineReadTemplate(filePath, 1, (line, result) -> {
           return result * Integer.parseInt(line);
       });
    }

    // 기존 calc와 같이 file을 불러오는 수준뿐 아니라, line을 읽는 수준까지 중복이 발생하기 때문에 이에 대한 템플릿을 생성
    private int lineReadTemplate(String filePath, int initVal, LineCallBack lineCallBack) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int result = initVal;
            String line = null;
            while ((line = br.readLine()) != null) {
                result = lineCallBack.doSomethingWithLine(line, result);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

// CalculatorV1 내부에서도 중복되는 코드가 있기 때문에 이것에 대한 콜백도 만들자.

interface LineCallBack {
    int doSomethingWithLine(String line, int value) throws IOException;
}


