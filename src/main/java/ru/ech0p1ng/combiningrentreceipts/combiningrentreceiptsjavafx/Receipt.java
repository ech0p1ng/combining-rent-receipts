package ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx;

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
import java.util.ArrayList;
import java.util.List;


@Data
public class Receipt {
    @Setter(AccessLevel.PRIVATE)
    private List<Document> documents;
    private List<String> pdfSourcesPaths;
    private String pngOutputFilePath;
    private int dpi;

    /**
     * @param pdfSourcesPaths   list of the paths to pdf documents
     * @param pngOutputFilePath file path for the output png of merged pdf files
     */
    public Receipt(List<String> pdfSourcesPaths, String pngOutputFilePath) {
        this(pdfSourcesPaths, pngOutputFilePath, 300);
    }

    /**
     * @param pdfSourcesPaths   list of the paths to pdf documents
     * @param pngOutputFilePath file path for the output png of merged pdf files
     * @param dpi               wanted dpi of pdf (300 is default value)
     */
    public Receipt(List<String> pdfSourcesPaths, String pngOutputFilePath, int dpi) {
        documents = new ArrayList<>();
        setDpi(dpi);
        setPdfSourcesPaths(pdfSourcesPaths);
        setPngOutputFilePath(pngOutputFilePath);
    }

    /**
     * Converting each page of the pdf file to png image
     *
     * @param pdfSourceFilePath list of the paths to pdf documents
     * @param pngOutputFilePath file path for the output png of merged pdf files
     * @param dpi               wanted dpi of pdf (300 is default value)
     * @return list of documents
     */
    private static List<Document> pdfToPng(String pdfSourceFilePath, String pngOutputFilePath, int dpi) {
        List<Document> documents = new ArrayList<>();
        try {
            File sourceFile = new File(pdfSourceFilePath);
            File destinationFile = new File(pngOutputFilePath).getParentFile();
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
                    bImage = autoCropWhiteBackground(bImage);
//                    bImage = bImage.getSubimage(0, 0, bImage.getWidth(), bImage.getHeight()); //обрезка изображения
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
     * Crop white background
     *
     * @param img BufferedImage
     * @return
     */
    public static BufferedImage autoCropWhiteBackground(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        int top = height, bottom = 0, left = width, right = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);

                if (!isWhite(rgb)) {
                    if (x < left)   left = x;
                    if (x > right)  right = x;
                    if (y < top)    top = y;
                    if (y > bottom) bottom = y;
                }
            }
        }

        if (left >= right || top >= bottom) return img;

        return img.getSubimage(left, top, right - left + 1, bottom - top + 1);
    }

    /**
     * Is pixel white
     *
     * @param rgb pixel
     * @return
     */
    private static boolean isWhite(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        // допуск, если фон слегка сероват
        return (r > 240 && g > 240 && b > 240);
    }


    /**
     * Merging pdf files into png image
     *
     * @return path to the output png image
     */
    public String render(ImageOrientation orientation) {
        try {
            //set the aspect ratio for a4 letter format
            double bgWidth = 0;
            double bgHeight = 0;

            for (var pdfSourcePath : pdfSourcesPaths) {
                var document = pdfToPng(pdfSourcePath, pngOutputFilePath, dpi);
                documents.addAll(document);
                for (var item : document) {
                    bgWidth += item.getPng().getWidth();
                    bgHeight = Math.max(bgHeight, item.getPng().getHeight());
                }
            }
            double ratio = bgWidth / bgHeight;
            double a4letterRatio = Math.sqrt(2);
            if (ratio < a4letterRatio) {
                bgWidth = bgHeight * a4letterRatio;
            }
            else {
                bgHeight = bgWidth / a4letterRatio;
            }

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


            //export file to
            String outputFilePath = pngOutputFilePath;
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

    public void setPdfSourcesPaths(List<String> pdfSourcesPaths) {
        this.pdfSourcesPaths = pdfSourcesPaths;
    }

    public void setPngOutputFilePath(String pngOutputFilePath) {
        this.pngOutputFilePath = pngOutputFilePath;
    }
}
