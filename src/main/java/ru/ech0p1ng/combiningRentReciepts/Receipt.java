package ru.ech0p1ng.combiningRentReciepts;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
public class Receipt {
    @Setter(AccessLevel.PRIVATE)
    private List<Document> documents;
    private List<String> pdfSourcesPaths;
    private String pngOutputDirectoryPath;
    private int dpi;

    /**
     * @param pdfSourcesPaths list of the paths to pdf documents
     * @param pngOutputDirectoryPath directory path for the output png of merged pdf files
     */
    public Receipt(List<String> pdfSourcesPaths, String pngOutputDirectoryPath) {
        this(pdfSourcesPaths, pngOutputDirectoryPath, 300);
    }

    /**
     * @param pdfSourcesPaths list of the paths to pdf documents
     * @param pngOutputDirectoryPath directory path for the output png of merged pdf files
     * @param dpi wanted dpi of pdf (300 is default value)
     */
    public Receipt(List<String> pdfSourcesPaths, String pngOutputDirectoryPath, int dpi) {
        documents = new ArrayList<>();
        setDpi(dpi);
        setPdfSourcesPaths(pdfSourcesPaths);
        setPngOutputDirectoryPath(pngOutputDirectoryPath);
    }

    /**
     * Converting each page of the pdf file to png image
     * @param pdfSourceFilePath list of the paths to pdf documents
     * @param pngOutputDirectoryPath directory path for the output png of merged pdf files
     * @param dpi wanted dpi of pdf (300 is default value)
     * @return list of documents
     */
    private static List<Document> pdfToPng(String pdfSourceFilePath, String pngOutputDirectoryPath, int dpi) {
        List<Document> documents = new ArrayList<>();
        try {
            File sourceFile = new File(pdfSourceFilePath);
            File destinationFile = new File(pngOutputDirectoryPath);
            if (!destinationFile.exists()) {
                destinationFile.mkdir();
            }
            if (sourceFile.exists()) {
                //loading pdf
                PDDocument document = PDDocument.load(sourceFile);
                PDFRenderer pdfRenderer = new PDFRenderer(document);

                int numberOfPages = document.getNumberOfPages();

                //converting each page of the file to png image
                for (int i = 0; i < numberOfPages; i++) {
                    BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
                    bImage = bImage.getSubimage(0, 0, bImage.getWidth() / 2, bImage.getHeight()); //обрезка изображения
                    Document document1 = new Document(pdfSourceFilePath, bImage);
                    documents.add(document1);
                }

                document.close();
            } else {
                System.err.println(sourceFile.getName() + " File not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documents;
    }

    /**
     * Merging pdf files into png image
     * @return path to the output png image
     */
    public String render() {
        try {
            //set the aspect ratio for a4 letter format
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

            //fill the background with white color
            BufferedImage bImage = new BufferedImage((int) bgWidth, (int) bgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, (int) bgWidth, (int) bgHeight);

            //set the offset for each document
            int horizontalOffset = 0;
            for (var document : documents) {
                g2d.drawImage(document.getPng(), horizontalOffset, 0, null);
                horizontalOffset += document.getPng().getWidth();
            }

            //formating dateTime for the file name
            LocalDateTime dateTime = LocalDateTime.now();
            String year = String.valueOf(dateTime.getYear());
            String month = dateTimeValueToString(dateTime.getMonthValue());
            String day = dateTimeValueToString(dateTime.getDayOfMonth());
            String hour = dateTimeValueToString(dateTime.getHour());
            String minute = dateTimeValueToString(dateTime.getMinute());
            String second = dateTimeValueToString(dateTime.getSecond());
            String milliseconds = String.valueOf(dateTime.getNano());
            String fileName = "output %s-%s-%s %s-%s-%s-%s.png"
                    .formatted(
                            year, month, day,
                            hour, minute, second, milliseconds
                    );

            //export file to
            String outputFilePath = pngOutputDirectoryPath + fileName;
            File outputFile = new File(outputFilePath);
            ImageIO.write(bImage, "png", outputFile);
            return outputFilePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDpi(int dpi) throws IllegalArgumentException {
        if (dpi < 300) {
            throw new IllegalArgumentException("dpi must be >= 300");
        }
        this.dpi = dpi;
    }

    /**
     * Adds zeroes at the beginning of the value to set the length of the value to 2
     * @param value
     * @return formatted value with zeroes at the beginning
     */
    private String dateTimeValueToString(int value) {
        String result = String.valueOf(value);
        while (result.length() < 2) {
            result = "0" + result;
        }
        return result;
    }
}
