package ru.ech0p1ng.combiningRentReciepts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String outputPath = "C:\\Users\\oselish\\Desktop\\чеки\\";

        List<String> files = Arrays.asList(
                "C:\\Users\\oselish\\Downloads\\Operation_Check_PJSC_Sberbank_22072024.pdf",
                "C:\\Users\\oselish\\Downloads\\Operation_Check_PJSC_Sberbank_22072024-1.pdf",
                "C:\\Users\\oselish\\Downloads\\Operation_Check_PJSC_Sberbank_22072024-2.pdf",
                "C:\\Users\\oselish\\Downloads\\Operation_Check_PJSC_Sberbank_22072024-3.pdf"
        );

        Receipt receipt = new Receipt(files, outputPath);
        receipt.render();
    }
}