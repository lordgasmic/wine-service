package com.lordgasmic.wineservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class FTPUpload {

    private static final String IMAGES = "/images/wine_images/";

    @Autowired
    private ImageProcessor imageProcessor;

    public void doUpload(final MultipartFile file) throws IOException {
        final String shortMimeType = Objects.requireNonNull(file.getContentType()).substring(file.getContentType().indexOf('/') + 1);
        final String dir = UUID.randomUUID().toString();
        final String filename = UUID.randomUUID().toString();
        final String fullFileName = filename + "." + shortMimeType;
        final String tbFileName = filename + "_tb." + shortMimeType;
        final byte[] resizeBytes = imageProcessor.resizeImage(file.getBytes(), shortMimeType);

        final File directory = new File(IMAGES + dir);
        Files.createDirectory(directory.toPath());

        final File fullSize = new File(IMAGES + dir + '/' + fullFileName);
        final File thumbnail = new File(IMAGES + dir + '/' + tbFileName);

        Files.write(fullSize.toPath(), file.getBytes());
        Files.write(thumbnail.toPath(), resizeBytes);
    }
}
