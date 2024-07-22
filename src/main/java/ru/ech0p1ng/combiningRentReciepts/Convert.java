package ru.ech0p1ng.combiningRentReciepts;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Convert {
    public static List<Document> PdfToPng(String pdfSourceFilePath, String pngOutputDirectoryPath, int dpi) {
        List<Document> documents = new ArrayList<>();
        try {
            File sourceFile = new File(pdfSourceFilePath);
            File destinationFile = new File(pngOutputDirectoryPath);
            if (!destinationFile.exists()) {
                destinationFile.mkdir();
                System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
            }
            if (sourceFile.exists()) {
                System.out.println("Images copied to Folder Location: "+ destinationFile.getAbsolutePath());
                PDDocument document = PDDocument.load(sourceFile);
                PDFRenderer pdfRenderer = new PDFRenderer(document);

                int numberOfPages = document.getNumberOfPages();
                System.out.println("Total files to be converting -> "+ numberOfPages);

                String fileName = sourceFile.getName().replace(".pdf", "");
                String fileExtension= "png";
                /*
                 * 600 dpi give good image clarity but size of each image is 2x times of 300 dpi.
                 * Ex:  1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB
                 *      2. For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
                 * int dpi = 300;// use less dpi for to save more space in harddisk. For professional usage you can use more than 300dpi
                 */

                for (int i = 0; i < numberOfPages; ++i) {
                    File outPutFile = new File(pngOutputDirectoryPath + fileName +"_"+ (i+1) +"."+ fileExtension);
                    BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
                    ImageIO.write(bImage, fileExtension, outPutFile);
                    Document document1 = new Document(pdfSourceFilePath,outPutFile.getPath(),outPutFile, bImage);
                    documents.add(document1);
                }

                document.close();
                System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
            } else {
                System.err.println(sourceFile.getName() +" File not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documents;
    }
}
