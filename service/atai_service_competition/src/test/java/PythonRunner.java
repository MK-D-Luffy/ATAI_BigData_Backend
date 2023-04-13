/**
 * @author zhuweiming <1905470291@qq.com>
 * @since 18/3/2023 下午7:42
 */

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PythonRunner {
    public static void main(String[] args) {

    }

    @Test
    public void test() throws IOException, InterruptedException {
        Process process;
        try {
            process = Runtime.getRuntime().exec("python D:\\Projects\\JavaProjects\\ATAI_BigData_Backend\\service\\atai_service_competition\\src\\test\\java\\1.py");// 执行py文件
            // 等待Python进程结束
            int exitCode = process.waitFor();
            System.out.println("Python process exited with code " + exitCode);

            // 从Python进程的标准输出流中读取输出结果
            InputStream input = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
