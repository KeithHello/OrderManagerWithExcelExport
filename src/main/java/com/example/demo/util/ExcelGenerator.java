package com.example.demo.util;

import com.example.demo.model.Order;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExcelGenerator {
    public static ByteArrayResource createExcel(Iterable<Order> orders)  {
        // ワークブックを作成
        try (Workbook workbook = new XSSFWorkbook()) {

            // ワークブックにシートを作成
            Sheet sheet = workbook.createSheet("情報");

            // ヘッダー行を作成
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("お客様名");
            headerRow.createCell(2).setCellValue("収入名");
            headerRow.createCell(3).setCellValue("値段");

            // データを行に追加
            int i = 1;
            for (Order order : orders) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getCustomerName());
                row.createCell(2).setCellValue(order.getProductName());
                row.createCell(3).setCellValue(order.getPrice());
                i++;
            }

            // ファイルを出力
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());

            return byteArrayResource;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
