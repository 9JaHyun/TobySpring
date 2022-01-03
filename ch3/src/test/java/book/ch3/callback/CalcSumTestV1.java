package book.ch3.callback;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalcSumTestV1 {
    private CalculatorV1 calculator;
    private String filePath;

    @BeforeAll
    void init() {
        this.calculator = new CalculatorV1();
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
        final CalculatorV1 calculator = new CalculatorV1();
        final int result = calculator.calcMulti(filePath);
        Assertions.assertSame(120, result);
    }
}

class CalculatorV1 {
    // 익명 클래스
    int calcSum(String filePath) {
        final BufferedReaderCallBack sumCallBack = new BufferedReaderCallBack() {
            @Override
            public int doSomethingWithReader(BufferedReader br) throws IOException {
                int sum = 0;
                String line = null;
                while ((line = br.readLine()) != null) {
                    sum += Integer.parseInt(line);
                }
                return sum;
            }
        };
        return calc(filePath, sumCallBack);
    }

    // 람다
   int calcMulti(String filePath) {
        return calc(filePath, (bufferedReader) -> {
            int result = 1;
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                result *= Integer.parseInt(line);
            }
            return result;
        });
    }

    // 콜백 인터페이스를 받는 메서드 생성
    // 이것도 충분하지만... 더욱 개선할 점이 있다. (V2)
    private int calc(String filePath, BufferedReaderCallBack callBack) {
        BufferedReader br = null;
        try {
             br = new BufferedReader(new FileReader(filePath));
            return callBack.doSomethingWithReader(br);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}

// 콜백 인터페이스
interface BufferedReaderCallBack {
    int doSomethingWithReader(BufferedReader br) throws IOException;
}
