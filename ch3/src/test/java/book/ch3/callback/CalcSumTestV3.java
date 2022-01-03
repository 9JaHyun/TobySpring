package book.ch3.callback;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// 이번에는 제네릭을 사용해서 더 강력한 콜백 인터페이스를 만들어 보자.
// 제네릭을 사용하면 여러 타입에 대한 대응력을 높일 수 있다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalcSumTestV3 {
    private CalculatorV3 calculator;
    private String filePath;
    private String stringPath;

    @BeforeAll
    void init() {
        this.calculator = new CalculatorV3();
        this.filePath = "C:\\Users\\rnwkg\\codding\\ch3\\src\\test\\java\\book\\callback\\numbers.txt";
        this.stringPath = "C:\\Users\\rnwkg\\codding\\ch3\\src\\test\\java\\book\\callback\\strings.txt";

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

    @DisplayName(value = "문자열 테스트")
    @Test
    void concatTest() throws IOException {
        final String result = calculator.concatenate(stringPath);
        Assertions.assertEquals(" hello world hello spring", result);
    }
}

class CalculatorV3 {
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

    // 이렇게 코드의 변경 없이 문자열도 처리가 가능하다.
    String concatenate(String filePath) {
        return lineReadTemplate(filePath, "", (line, result) -> {
            return result + line;
        });
    }

    // 기존 calc와 같이 file을 불러오는 수준뿐 아니라, line을 읽는 수준까지 중복이 발생하기 때문에 이에 대한 템플릿을 생성
    private <T>T lineReadTemplate(String filePath, T initVal, LineCallBackV1<T> lineCallBack) {
        T result = initVal;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                result = lineCallBack.doSomethingWithLine(line, result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

// 여러 타입에 대한 대응력을 키우기 위해서 제네릭을 사용하자.
interface LineCallBackV1<T> {
    T doSomethingWithLine(String line, T value) throws IOException;
}


