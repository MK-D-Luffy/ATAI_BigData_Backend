package com.atai.competition.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


@ConfigurationProperties (prefix = "run.script")
@Component
public class AtaiRunCode {

    private static String javaExec = null;
    private static AtomicLong nextLong = new AtomicLong(System.currentTimeMillis());
    private String cpp;
    private String c;
    private String python;

    public void setCpp(String cpp) {
        this.cpp = cpp;
    }

    public void setC(String c) {
        this.c = c;
    }

    public void setPython(String python) {
        this.python = python;
    }

    public AtaiProcessResult runCode(String type, String code) throws IOException, InterruptedException {
        // 获取系统缓存文件的位置
        String tmpDir = System.getProperty("java.io.tmpdir");
        // 随机文件夹的名字
        File pwd = Paths.get(tmpDir, String.format("%016x", nextLong.incrementAndGet())).toFile();
        // 新建文件夹
        pwd.mkdirs();
        ProcessBuilder pb = null;
        switch (type) {
            case "c":
                try (Writer writer = new BufferedWriter(new FileWriter(new File(pwd, "Main.c")))) {
                    writer.write(code);
                }
                pb = new ProcessBuilder().command(c).directory(pwd);
                break;
            case "cpp":
                try (Writer writer = new BufferedWriter(new FileWriter(new File(pwd, "Main.cpp")))) {
                    writer.write(code);
                }
                pb = new ProcessBuilder().command(cpp).directory(pwd);
                break;
            case "java":
                try (Writer writer = new BufferedWriter(new FileWriter(new File(pwd, "Main.java")))) {
                    writer.write(code);
                }
                //Java需先编译生成字节码文件 waitFor:等待执行完毕
                new ProcessBuilder("javac", "Main.java").directory(pwd).start().waitFor();
                pb = new ProcessBuilder().command("java", "Main").directory(pwd);
                break;
            case "python":
                try (Writer writer = new BufferedWriter(new FileWriter(new File(pwd, "Main.py")))) {
                    writer.write(code);
                }
                pb = new ProcessBuilder().command(python, "Main.py").directory(pwd);
                break;
            default:
                break;
        }

        pb.redirectErrorStream(true);
        Process p = pb.start();
        if (p.waitFor(5, TimeUnit.SECONDS)) {
            String result;
            try (InputStream input = p.getInputStream()) {
                result = readAsString(input, Charset.defaultCharset());
            }
            return new AtaiProcessResult(p.exitValue(), result);
        } else {
            System.err.println("Error: process timeout. destroy forcibly.");
            p.destroyForcibly();
            return new AtaiProcessResult(p.exitValue(), "运行超时");
        }
    }


    private String getJavaExecutePath() {
        if (javaExec == null) {
            String javaHome = System.getProperty("java.home");
            String os = System.getProperty("os.name");
            boolean isWindows = os.toLowerCase().startsWith("windows");
            Path javaPath = Paths.get(javaHome, "bin", isWindows ? "java.exe" : "java");
            javaExec = javaPath.toString();
        }
        return javaExec;
    }

    public String readAsString(InputStream input, Charset charset) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[102400];
        for (; ; ) {
            int n = input.read(buffer);
            if (n == (-1)) {
                break;
            }
            output.write(buffer, 0, n);
        }
        return output.toString(String.valueOf(charset));
    }
}