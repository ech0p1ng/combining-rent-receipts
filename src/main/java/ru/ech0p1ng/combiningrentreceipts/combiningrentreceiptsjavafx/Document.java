package ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx;

import java.awt.image.BufferedImage;

class Document {
    private String pdfPath;
    private BufferedImage png;

    public Document(String pdfPath, BufferedImage png) {
        this.pdfPath = pdfPath;
        this.png = png;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public BufferedImage getPng() {
        return png;
    }

    public void setPng(BufferedImage png) {
        this.png = png;
    }
}
