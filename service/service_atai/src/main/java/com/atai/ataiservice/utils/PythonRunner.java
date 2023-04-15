package com.atai.ataiservice.utils;
/**
 * @author zhuweiming <1905470291@qq.com>
 * @since 18/3/2023 下午7:42
 */

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PythonRunner {

    public static ArrayList<String> run(String pythonPath) {
        Process process;
        String command = "python " + pythonPath;
        try {
            process = Runtime.getRuntime().exec(command);// 执行py文件
            // 等待Python进程结束
            int exitCode = process.waitFor();
            System.out.println("Python process exited with code " + exitCode);

            // 从Python进程的标准输出流中读取输出结果
            InputStream input = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            ArrayList<String> output = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
            return output;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void test() {
        run("2.py");
    }

}
