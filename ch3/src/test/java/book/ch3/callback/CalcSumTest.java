package book.ch3.callback;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CalcSumTest {
    @DisplayName(value = "덧셈 테스트")
    @Test
    void sumOfNumbers() throws IOException {
        final Calculator calculator = new Calculator();
        final int sum = calculator.calcSum("C:\\Users\\rnwkg\\codding\\ch3\\src\\test\\java\\book\\callback\\numbers.txt");
        Assertions.assertSame(15, sum);
    }


}
class Calculator {
    int calcSum(String filePath) {
        try(BufferedReader br = new BufferedReader(new FileReader(filePath));) {
            int sum = 0;
            String line = null;
            while ((line = br.readLine()) != null) {
                sum += Integer.parseInt(line);
            }
            br.close();
            return sum;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 문제 발생 점점 많은 기능이 요구 (곱셈)
    // 중복 코드 발생
    int calcMulti(String filePath) {
        try(BufferedReader br = new BufferedReader(new FileReader(filePath));) {
            int multi = 1;
            String line = null;
            while ((line = br.readLine()) != null) {
                multi *= Integer.parseInt(line);
            }
            br.close();
            return multi;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
