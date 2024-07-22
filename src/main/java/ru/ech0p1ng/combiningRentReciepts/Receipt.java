package ru.ech0p1ng.combiningRentReciepts;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.print.Doc;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Data
public class Receipt {
    List<String> pdfSourcesPaths;
    String pngOutputDirectoryPath;
    @Setter(AccessLevel.PRIVATE)
    private List<Document> documents;
    private int dpi;
    private boolean saveOnDiskTempImages = false;

    public Receipt(List<String> pdfSourcesPaths, String pngOutputDirectoryPath) {
        this(pdfSourcesPaths, pngOutputDirectoryPath, 300);
    }

    public Receipt(List<String> pdfSourcesPaths, String pngOutputDirectoryPath, int dpi) {
        documents = new ArrayList<>();
        setDpi(dpi);
        setPdfSourcesPaths(pdfSourcesPaths);
        setPngOutputDirectoryPath(pngOutputDirectoryPath);
    }

    public static List<Document> pdfToPng(String pdfSourceFilePath, String pngOutputDirectoryPath, int dpi) {
        List<Document> documents = new ArrayList<>();
        try {
            File sourceFile = new File(pdfSourceFilePath);
            File destinationFile = new File(pngOutputDirectoryPath);
            if (!destinationFile.exists()) {
                destinationFile.mkdir();
                System.out.println("Folder Created -> " + destinationFile.getAbsolutePath());
            }
            if (sourceFile.exists()) {
                System.out.println("Images copied to Folder Location: " + destinationFile.getAbsolutePath());
                PDDocument document = PDDocument.load(sourceFile);
                PDFRenderer pdfRenderer = new PDFRenderer(document);

                int numberOfPages = document.getNumberOfPages();
                System.out.println("Total files to be converting -> " + numberOfPages);

                String fileName = sourceFile.getName().replace(".pdf", "");
                String fileExtension = "png";
                /*
                 * 600 dpi give good image clarity but size of each image is 2x times of 300 dpi.
                 * Ex:  1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB
                 *      2. For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
                 * int dpi = 300;// use less dpi for to save more space in harddisk. For professional usage you can use more than 300dpi
                 */

                for (int i = 0; i < numberOfPages; i++) {
                    BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
                    bImage = bImage.getSubimage(0, 0, bImage.getWidth() / 2, bImage.getHeight()); //обрезка изображения
//                    File outPutFile = new File(pngOutputDirectoryPath + fileName + "_" + (i + 1) + "." + fileExtension);
//                    ImageIO.write(bImage, fileExtension, outPutFile);
                    Document document1 = new Document(pdfSourceFilePath, bImage);
                    documents.add(document1);
                }

                document.close();
                System.out.println("Converted Images are saved at -> " + destinationFile.getAbsolutePath());
            } else {
                System.err.println(sourceFile.getName() + " File not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documents;
    }

    public void render() {
        try {
            double bgWidth = 0;
            double bgHeight = 0;
            for (var pdfSourcePath : pdfSourcesPaths) {
                var document = pdfToPng(pdfSourcePath, pngOutputDirectoryPath, dpi);
                documents.addAll(document);
                for (var item : document) {
                    bgWidth += item.getPng().getWidth();
                }
            }

            bgHeight = bgWidth * Math.sqrt(2);

            BufferedImage bImage = new BufferedImage((int) bgWidth, (int) bgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, (int) bgWidth, (int) bgHeight);

            int horizontalOffset = 0;

            for (var document : documents) {
                g2d.drawImage(document.getPng(), horizontalOffset, 0, null);
                horizontalOffset += document.getPng().getWidth();
            }

            File outputFile = new File(pngOutputDirectoryPath + "output.png");
            ImageIO.write(bImage, "png", outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDpi(int dpi) throws IllegalArgumentException {
        if (dpi >= 300) {
            this.dpi = dpi;
        } else {
            throw new IllegalArgumentException("dpi must be >= 300");
        }
    }
}
