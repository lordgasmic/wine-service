package com.lordgasmic.wineservice.service;

import com.lordgasmic.collections.Nucleus;
import com.lordgasmic.collections.repository.GSARepository;
import com.lordgasmic.collections.repository.MutableRepositoryItem;
import com.lordgasmic.collections.repository.RepositoryItem;
import com.lordgasmic.wineservice.config.WineRatingConstants;
import com.lordgasmic.wineservice.models.WineFriendsRequest;
import com.lordgasmic.wineservice.models.WineRatingEditRequest;
import com.lordgasmic.wineservice.models.WineRatingRequest;
import com.lordgasmic.wineservice.models.WineRatingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.lordgasmic.wineservice.config.WineRatingConstants.PROPERTY_DATE;
import static com.lordgasmic.wineservice.config.WineRatingConstants.PROPERTY_RATING;
import static com.lordgasmic.wineservice.config.WineRatingConstants.PROPERTY_USER;
import static com.lordgasmic.wineservice.config.WineRatingConstants.PROPERTY_WINE_ID;
import static com.lordgasmic.wineservice.config.WineRatingConstants.WINE_RATING_REPOSITORY_ITEM;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class WineRatingService {
    private static final String REPO_NAME = "WineTastingRepository";

    private final GSARepository wineRepository;

    public WineRatingService() {
        wineRepository = (GSARepository) Nucleus.getInstance().getGenericService(REPO_NAME);
    }

    public List<WineRatingResponse> getAllWineRatings() throws SQLException {
        final List<RepositoryItem> items = wineRepository.getAllRepositoryItems(WINE_RATING_REPOSITORY_ITEM);
        return items.stream().map(WineRatingService::convertRepositoryItemToWineRatingResponse).collect(toList());
    }

    public List<WineRatingResponse> getWineRatingByWineId(final int wineId) throws SQLException {
        final List<RepositoryItem> items = wineRepository.getAllRepositoryItems(WINE_RATING_REPOSITORY_ITEM);
        return items.stream()
                    .filter(ri -> ri.getPropertyValue(PROPERTY_WINE_ID).equals(wineId))
                    .map(WineRatingService::convertRepositoryItemToWineRatingResponse)
                    .collect(toList());
    }

    public List<WineRatingResponse> getWineRatingsByUser(final String user) throws SQLException {
        final List<RepositoryItem> items = wineRepository.getAllRepositoryItems(WINE_RATING_REPOSITORY_ITEM);
        return items.stream()
                    .filter(ri -> ri.getPropertyValue(PROPERTY_USER).equals(user))
                    .map(WineRatingService::convertRepositoryItemToWineRatingResponse)
                    .collect(toList());
    }

    public List<WineRatingResponse> getWineRatingsByWineIdByUser(final int wineId, final String user) throws SQLException {
        final List<RepositoryItem> items = wineRepository.getAllRepositoryItems(WINE_RATING_REPOSITORY_ITEM);
        return items.stream()
                    .filter(ri -> ri.getPropertyValue(PROPERTY_WINE_ID).equals(wineId))
                    .filter(ri -> ri.getPropertyValue(PROPERTY_USER).equals(user))
                    .map(WineRatingService::convertRepositoryItemToWineRatingResponse)
                    .collect(toList());
    }

    public List<WineRatingResponse> getWineRatingsByUsersByWineIds(final WineFriendsRequest request) throws SQLException {
        final List<Predicate<RepositoryItem>> filterList = new ArrayList<>();
        filterList.add(i -> request.getWineIds().contains((Integer) i.getPropertyValue(PROPERTY_WINE_ID)));

        if (!request.getUsers().get(0).equals("*")) {
            filterList.add(i -> request.getUsers().contains((String) i.getPropertyValue(PROPERTY_USER)));
        }

        final List<RepositoryItem> items = wineRepository.getAllRepositoryItems(WINE_RATING_REPOSITORY_ITEM);
        return items.stream()
                    .filter(i -> filterList.stream().anyMatch(f -> f.test(i)))
                    .map(WineRatingService::convertRepositoryItemToWineRatingResponse)
                    .collect(toList());
    }

    public WineRatingResponse addWineRating(final WineRatingRequest request) throws SQLException {
        final MutableRepositoryItem item = wineRepository.createItem(WINE_RATING_REPOSITORY_ITEM);

        item.setProperty(PROPERTY_WINE_ID, request.getWineId());
        item.setProperty(PROPERTY_USER, request.getUser());
        item.setProperty(PROPERTY_DATE, request.getDate());
        item.setProperty(PROPERTY_RATING, request.getRating());

        final RepositoryItem addedItem = wineRepository.addItem(item);
        return convertRepositoryItemToWineRatingResponse(addedItem);
    }

    public WineRatingResponse editWineRating(final WineRatingEditRequest request) throws SQLException {
        final MutableRepositoryItem item = (MutableRepositoryItem) wineRepository.getRepositoryItem(request.getId(), WINE_RATING_REPOSITORY_ITEM);
        item.setProperty(PROPERTY_RATING, request.getRating());

        final RepositoryItem updatedItem = wineRepository.updateItem(item, PROPERTY_RATING);
        return convertRepositoryItemToWineRatingResponse(updatedItem);
    }

    private static WineRatingResponse convertRepositoryItemToWineRatingResponse(final RepositoryItem repositoryItem) {
        final WineRatingResponse response = new WineRatingResponse();
        response.setId((Integer) repositoryItem.getPropertyValue(WineRatingConstants.PROPERTY_ID));
        response.setWineId((Integer) repositoryItem.getPropertyValue(PROPERTY_WINE_ID));
        response.setUser((String) repositoryItem.getPropertyValue(PROPERTY_USER));
        response.setDate((String) repositoryItem.getPropertyValue(WineRatingConstants.PROPERTY_DATE));
        response.setRating((String) repositoryItem.getPropertyValue(WineRatingConstants.PROPERTY_RATING));
        return response;
    }
}
