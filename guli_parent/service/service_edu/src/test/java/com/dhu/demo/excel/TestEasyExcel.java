package com.dhu.demo.excel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    public static void main(String[] args) {
        //实现excel的写操作
        //1 设置写入的文件夹地址和excel文件名称
        String filename = "C:\\Users\\yushu\\Desktop\\student.xlsx";
        //2 调用EasyExcel的方法实现写操作

        //两个参数： 文件的路径名称  实体类
        EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());

    }

    @Test
    public void ReadExcel(){
        String filename = "C:\\Users\\yushu\\Desktop\\student.xlsx";
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }

    //循环设置要添加的数据，最终封装到list集合中
    private static List<DemoData> getData() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("张三"+i);
            list.add(data);
        }
        return list;
    }
}
