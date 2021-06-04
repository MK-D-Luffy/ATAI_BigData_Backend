package com.atai.compentition.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class CompeletionResult {
    //id
    @ExcelProperty(index = 0)
    private String id;

    //result
    @ExcelProperty(index = 1)
    private String result;
}
