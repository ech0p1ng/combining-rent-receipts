package ru.ech0p1ng.combiningrentreceipts;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;

@Data
@AllArgsConstructor
public class Document {
    private String pdfPath;
    private BufferedImage png;
}
