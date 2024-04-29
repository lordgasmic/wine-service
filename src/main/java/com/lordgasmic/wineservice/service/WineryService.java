package com.lordgasmic.wineservice.service;

import com.lordgasmic.collections.Nucleus;
import com.lordgasmic.collections.repository.GSARepository;
import com.lordgasmic.collections.repository.MutableRepositoryItem;
import com.lordgasmic.collections.repository.RepositoryItem;
import com.lordgasmic.wineservice.config.WineryConstants;
import com.lordgasmic.wineservice.models.WineryRequest;
import com.lordgasmic.wineservice.models.WineryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

import static com.lordgasmic.wineservice.config.WineryConstants.PROPERTY_LOCATION;
import static com.lordgasmic.wineservice.config.WineryConstants.PROPERTY_NAME;
import static com.lordgasmic.wineservice.config.WineryConstants.WINERY_REPOSITORY_ITEM;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class WineryService {
    private static final String REPO_NAME = "WineTastingRepository";

    private final GSARepository wineRepository;

    public WineryService() {
        wineRepository = (GSARepository) Nucleus.getInstance().getGenericService(REPO_NAME);
    }

    public List<WineryResponse> getWineries() throws SQLException {
        final List<RepositoryItem> items = wineRepository.getAllRepositoryItems(WINERY_REPOSITORY_ITEM);
        return items.stream().map(WineryService::convertRepositoryItemToWineryResponse).collect(toList());
    }

    public WineryResponse getWineryById(final String id) throws SQLException {
        final RepositoryItem winery = wineRepository.getRepositoryItem(id, WINERY_REPOSITORY_ITEM);
        return convertRepositoryItemToWineryResponse(winery);
    }

    public WineryResponse addWinery(final WineryRequest request) throws SQLException {
        final MutableRepositoryItem item = wineRepository.createItem(WINERY_REPOSITORY_ITEM);
        item.setProperty(PROPERTY_NAME, request.getName());
        item.setProperty(PROPERTY_LOCATION, request.getLocation());
        final RepositoryItem addedItem = wineRepository.addItem(item);

        return convertRepositoryItemToWineryResponse(addedItem);
    }

    private static WineryResponse convertRepositoryItemToWineryResponse(final RepositoryItem repositoryItem) {
        final WineryResponse response = new WineryResponse();
        response.setId((Integer) repositoryItem.getPropertyValue(WineryConstants.PROPERTY_ID));
        response.setName((String) repositoryItem.getPropertyValue(WineryConstants.PROPERTY_NAME));
        response.setLocation((String) repositoryItem.getPropertyValue(WineryConstants.PROPERTY_LOCATION));

        return response;
    }
}
