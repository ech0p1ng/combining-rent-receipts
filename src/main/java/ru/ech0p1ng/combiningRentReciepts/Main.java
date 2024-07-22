package ru.ech0p1ng.combiningRentReciepts;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String pdfPath = "C:\\Users\\oselish\\Downloads\\Operation_Check_PJSC_Sberbank_22072024-3.pdf";
        String desktopPath = "C:\\Users\\oselish\\Desktop\\";
        int dpi = 300;


        List<Document> documents = new ArrayList<>();
        var result = Convert.PdfToPng(pdfPath, desktopPath, dpi);
        documents.addAll(result);


    }
}