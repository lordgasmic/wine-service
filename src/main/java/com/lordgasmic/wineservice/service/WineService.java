package com.lordgasmic.wineservice.service;

import com.lordgasmic.collections.Nucleus;
import com.lordgasmic.collections.repository.GSARepository;
import com.lordgasmic.collections.repository.MutableRepositoryItem;
import com.lordgasmic.collections.repository.RepositoryItem;
import com.lordgasmic.wineservice.config.WineConstants;
import com.lordgasmic.wineservice.models.WineRequest;
import com.lordgasmic.wineservice.models.WineResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import static com.lordgasmic.wineservice.config.WineConstants.PROPERTY_STYLE;
import static com.lordgasmic.wineservice.config.WineConstants.PROPERTY_WINERY_ID;
import static com.lordgasmic.wineservice.config.WineConstants.WINE_REPOSITORY_ITEM;
import static com.lordgasmic.wineservice.config.WineConstants.PROPERTY_NAME;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class WineService {
    private static final String REPO_NAME = "WineTastingRepository";

    private final GSARepository wineRepository;

    public WineService() {
        wineRepository = (GSARepository) Nucleus.getInstance().getGenericService(REPO_NAME);
    }

    public List<WineResponse> getAllWines() throws SQLException {
        final List<RepositoryItem> items = wineRepository.getAllRepositoryItems(WINE_REPOSITORY_ITEM);
        return items.stream().map(WineService::convertRepositoryItemToWineResponse).collect(toList());
    }

    public List<WineResponse> getWinesByWineryId(final String id) throws SQLException {
        final List<RepositoryItem> items = wineRepository.getAllRepositoryItems(WINE_REPOSITORY_ITEM);
        return items.stream()
                    .filter(ri -> ri.getPropertyValue(PROPERTY_WINERY_ID).equals(Integer.parseInt(id)))
                    .map(WineService::convertRepositoryItemToWineResponse)
                    .sorted(Comparator.comparing(WineResponse::getName, String.CASE_INSENSITIVE_ORDER))
                    .collect(toList());
    }

    public WineResponse getWine(final String id) throws SQLException {
        final RepositoryItem item = wineRepository.getRepositoryItem(id, WINE_REPOSITORY_ITEM);
        return convertRepositoryItemToWineResponse(item);
    }

    public WineResponse addWine(final WineRequest request) throws SQLException {
        final MutableRepositoryItem item = wineRepository.createItem(WINE_REPOSITORY_ITEM);
        item.setProperty(PROPERTY_WINERY_ID, request.getWineryId());
        item.setProperty(PROPERTY_NAME, request.getName());
        item.setProperty(PROPERTY_STYLE, request.getStyle());
        final RepositoryItem addedItem = wineRepository.addItem(item);

        return convertRepositoryItemToWineResponse(addedItem);
    }

    private static WineResponse convertRepositoryItemToWineResponse(final RepositoryItem repositoryItem) {
        final WineResponse response = new WineResponse();
        response.setId((Integer) repositoryItem.getPropertyValue(WineConstants.PROPERTY_ID));
        response.setWineryId((Integer) repositoryItem.getPropertyValue(WineConstants.PROPERTY_WINERY_ID));
        response.setName((String) repositoryItem.getPropertyValue(WineConstants.PROPERTY_NAME));
        response.setStyle((String) repositoryItem.getPropertyValue(WineConstants.PROPERTY_STYLE));
        return response;
    }
}
