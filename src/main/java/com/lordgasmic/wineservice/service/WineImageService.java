package com.lordgasmic.wineservice.service;

import com.lordgasmic.collections.Nucleus;
import com.lordgasmic.collections.repository.GSARepository;
import com.lordgasmic.collections.repository.MutableRepositoryItem;
import com.lordgasmic.collections.repository.RepositoryItem;
import com.lordgasmic.wineservice.config.WineImageConstants;
import com.lordgasmic.wineservice.models.WineImage;
import com.lordgasmic.wineservice.models.WineImageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class WineImageService {
    private static final String REPO_NAME = "WineTastingRepository";

    private final GSARepository wineRepository;
    private final ImageProcessor imageProcessor;

    public WineImageService() {
        wineRepository = (GSARepository) Nucleus.getInstance().getGenericService(REPO_NAME);
        imageProcessor = new ImageProcessor();
    }

    public WineImageResponse addWineImage(final int wineId, final String label, final MultipartFile file) throws SQLException, IOException {
        final String shortMimeType = Objects.requireNonNull(file.getContentType()).substring(file.getContentType().indexOf('/') + 1);
        log.info("resizing...");
        final byte[] resizeBytes = imageProcessor.resizeImage(file.getBytes(), shortMimeType);
        final MutableRepositoryItem item = wineRepository.createItem(WineImageConstants.WINE_IMAGE_ITEM_DESCRIPTOR_NAME);
        item.setProperty(WineImageConstants.PROPERTY_WINE_ID, wineId);
        item.setProperty(WineImageConstants.PROPERTY_LABEL, label);
        item.setProperty(WineImageConstants.PROPERTY_IMAGE, resizeBytes);
        item.setProperty(WineImageConstants.PROPERTY_MIME_TYPE, file.getContentType());
        log.info("adding image to repository");
        final RepositoryItem repositoryItem = wineRepository.addItem(item);

        log.info("resizing thumbnail...");
        final byte[] thumbnailBytes = imageProcessor.resizeImageToThumbnail(file.getBytes(), shortMimeType);
        final MutableRepositoryItem thumbnailItem = wineRepository.createItem(WineImageConstants.WINE_IMAGE_THUMBNAIL_ITEM_DESCRIPTOR_NAME);
        thumbnailItem.setProperty(WineImageConstants.PROPERTY_WINE_ID, wineId);
        thumbnailItem.setProperty(WineImageConstants.PROPERTY_LABEL, label);
        thumbnailItem.setProperty(WineImageConstants.PROPERTY_THUMBNAIL, thumbnailBytes);
        thumbnailItem.setProperty(WineImageConstants.PROPERTY_MIME_TYPE, file.getContentType());
        log.info("adding thumbnail to repository");
        wineRepository.addItem(thumbnailItem);

        final WineImageResponse response = new WineImageResponse();
        response.setWineImages(List.of(convertRepositoryItemToWineImage(repositoryItem)));
        return response;
    }

    public WineImageResponse getWineImages(final int wineId) throws SQLException {
        final List<RepositoryItem> items = wineRepository.getRepositoryItems(Integer.toString(wineId),
                                                                             WineImageConstants.PROPERTY_WINE_ID,
                                                                             WineImageConstants.WINE_IMAGE_ITEM_DESCRIPTOR_NAME);
        final List<WineImage> images = items.stream().map(WineImageService::convertRepositoryItemToWineImage).collect(toList());
        final WineImageResponse response = new WineImageResponse();
        response.setWineImages(images);
        return response;
    }

    private static WineImage convertRepositoryItemToWineImage(final RepositoryItem repositoryItem) {
        final WineImage image = new WineImage();
        image.setId((Integer) repositoryItem.getPropertyValue(WineImageConstants.PROPERTY_ID));
        image.setWineId((Integer) repositoryItem.getPropertyValue(WineImageConstants.PROPERTY_WINE_ID));
        image.setLabel((String) repositoryItem.getPropertyValue(WineImageConstants.PROPERTY_LABEL));
        image.setImage((byte[]) repositoryItem.getPropertyValue(WineImageConstants.PROPERTY_IMAGE));
        image.setMimeType((String) repositoryItem.getPropertyValue(WineImageConstants.PROPERTY_MIME_TYPE));
        return image;
    }
}
