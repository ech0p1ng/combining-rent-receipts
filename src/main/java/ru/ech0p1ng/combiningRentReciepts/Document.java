package ru.ech0p1ng.combiningRentReciepts;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.File;

@Data
@AllArgsConstructor
public class Document {
    private String pdfPath;
    private String pngPath;
    private File pdf;
    private BufferedImage png;
}
