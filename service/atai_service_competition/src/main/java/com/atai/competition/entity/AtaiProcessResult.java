package com.atai.competition.entity;

/**
 * 运行代码结果
 *
 * @author zhuweiming <1905470291@qq.com>
 * @since 2021/8/17 8:52
 */
public class AtaiProcessResult {
    private int exitCode;

    private String output;

    public AtaiProcessResult(int exitCode, String output) {
        this.exitCode = exitCode;
        this.output = output;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getOutput() {
        return output;
    }
}
