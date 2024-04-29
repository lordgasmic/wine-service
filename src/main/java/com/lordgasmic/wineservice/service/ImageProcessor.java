package com.lordgasmic.wineservice.service;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ImageProcessor {
    private static final int SCALE_TO_HEIGHT = 200;
    private static final int SCALE_TO_THUMBNAIL_HEIGHT = 50;

    public byte[] resizeImage(final byte[] bytes, final String mimeType) throws IOException {
        return resize(bytes, SCALE_TO_HEIGHT, mimeType);
    }

    public byte[] resizeImageToThumbnail(final byte[] bytes, final String mimeType) throws IOException {
        return resize(bytes, SCALE_TO_THUMBNAIL_HEIGHT, mimeType);
    }

    private static byte[] resize(final byte[] bytes, final int newHeight, final String mimeType) throws IOException {
        final Image originalSize = ImageIO.read(new ByteArrayInputStream(bytes));
        final int origHeight = originalSize.getHeight(null);
        if (origHeight > newHeight) {
            final double heightDiff = origHeight - newHeight;
            final double scaleRatio = origHeight / heightDiff;
            final int origWidth = originalSize.getWidth(null);
            final double widthDiff = origWidth / scaleRatio;
            final int newWidth = (int) (origWidth - widthDiff);

            final Image resizeImage = originalSize.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            final BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(resizeImage, 0, 0, null);
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, mimeType, bos);
            return bos.toByteArray();
        }
        return bytes;
    }
}
