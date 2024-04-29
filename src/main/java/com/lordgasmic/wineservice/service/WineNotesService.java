package com.lordgasmic.wineservice.service;

import com.lordgasmic.collections.Nucleus;
import com.lordgasmic.collections.repository.GSARepository;
import com.lordgasmic.collections.repository.MutableRepositoryItem;
import com.lordgasmic.collections.repository.RepositoryItem;
import com.lordgasmic.wineservice.config.WineNotesConstants;
import com.lordgasmic.wineservice.models.WineNoteOutput;
import com.lordgasmic.wineservice.models.WineNoteRequest;
import com.lordgasmic.wineservice.models.WineNoteResponse;
import com.lordgasmic.wineservice.models.WineNoteUpsert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.lordgasmic.wineservice.config.WineNotesConstants.PROPERTY_DATE;
import static com.lordgasmic.wineservice.config.WineNotesConstants.PROPERTY_NOTE;
import static com.lordgasmic.wineservice.config.WineNotesConstants.PROPERTY_ORDINAL;
import static com.lordgasmic.wineservice.config.WineNotesConstants.PROPERTY_USER;
import static com.lordgasmic.wineservice.config.WineNotesConstants.PROPERTY_WINE_ID;
import static com.lordgasmic.wineservice.config.WineNotesConstants.WINE_NOTES_REPOSITORY_ITEM;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class WineNotesService {
    private static final String REPO_NAME = "WineTastingRepository";

    private final GSARepository wineRepository;

    public WineNotesService() {
        wineRepository = (GSARepository) Nucleus.getInstance().getGenericService(REPO_NAME);
    }

    public WineNoteResponse getAllWineNotes() throws SQLException {
        final List<RepositoryItem> items = wineRepository.getAllRepositoryItems(WINE_NOTES_REPOSITORY_ITEM);
        final List<WineNoteOutput> wineNotes = items.stream().map(WineNotesService::convertRepositoryItemToWineNoteResponse).collect(toList());

        return WineNoteResponse.builder().wineNotes(wineNotes).build();
    }

    public WineNoteResponse getWineNotesByUser(final String user) throws SQLException {
        final List<RepositoryItem> items = wineRepository.getRepositoryItems(user, PROPERTY_USER, WINE_NOTES_REPOSITORY_ITEM);
        final List<WineNoteOutput> wineNotes = items.stream().map(WineNotesService::convertRepositoryItemToWineNoteResponse).collect(toList());

        return WineNoteResponse.builder().wineNotes(wineNotes).build();
    }

    public WineNoteResponse getWineNotesByWineId(final int wineId) throws SQLException {
        final List<RepositoryItem> items = wineRepository.getRepositoryItems(Integer.toString(wineId), PROPERTY_WINE_ID, WINE_NOTES_REPOSITORY_ITEM);
        final List<WineNoteOutput> wineNotes = items.stream().map(WineNotesService::convertRepositoryItemToWineNoteResponse).collect(toList());

        return WineNoteResponse.builder().wineNotes(wineNotes).build();
    }

    public WineNoteResponse getWineNotesByWineIdByUser(final int wineId, final String user) throws SQLException {
        final List<RepositoryItem> items = wineRepository.getRepositoryItems(Integer.toString(wineId), PROPERTY_WINE_ID, WINE_NOTES_REPOSITORY_ITEM);
        final List<WineNoteOutput> wineNotes = items.stream()
                                                    .filter(ri -> ri.getPropertyValue(PROPERTY_USER).equals(user))
                                                    .map(WineNotesService::convertRepositoryItemToWineNoteResponse)
                                                    .collect(toList());

        return WineNoteResponse.builder().wineNotes(wineNotes).build();
    }

    public WineNoteResponse addWineNotes(final WineNoteRequest request) throws SQLException {
        final WineNoteResponse response = new WineNoteResponse();
        List<RepositoryItem> items = wineRepository.getRepositoryItems(Integer.toString(request.getWineId()),
                                                                       PROPERTY_WINE_ID,
                                                                       WINE_NOTES_REPOSITORY_ITEM);
        int maxOrdinal = items.stream().mapToInt(item -> (Integer) item.getPropertyValue(PROPERTY_ORDINAL)).max().orElseGet(() -> -1);

        for (final String note : request.getWineNotes()) {
            final MutableRepositoryItem item = wineRepository.createItem(WINE_NOTES_REPOSITORY_ITEM);
            item.setProperty(PROPERTY_WINE_ID, request.getWineId());
            item.setProperty(PROPERTY_USER, request.getUser());
            item.setProperty(PROPERTY_DATE, request.getDate());
            item.setProperty(PROPERTY_NOTE, note);
            item.setProperty(PROPERTY_ORDINAL, ++maxOrdinal);

            addItem(item);
        }

        for (final RepositoryItem item : items) {
            final MutableRepositoryItem mItem = (MutableRepositoryItem) item;
            final Optional<WineNoteUpsert> optional = request.getUpsert()
                                                             .stream()
                                                             .filter(i -> mItem.getPropertyValue(WineNotesConstants.PROPERTY_ID)
                                                                               .equals(Integer.parseInt(i.getId())))
                                                             .findFirst();
            if (optional.isPresent()) {
                log.info("found optional");
                mItem.setProperty(PROPERTY_NOTE, optional.get().getNote());
                wineRepository.updateItem(mItem, PROPERTY_NOTE);
            }
        }

        items = wineRepository.getRepositoryItems(Integer.toString(request.getWineId()), PROPERTY_WINE_ID, WINE_NOTES_REPOSITORY_ITEM);
        for (final RepositoryItem item : items) {
            response.getWineNotes().add(convertRepositoryItemToWineNoteResponse(item));
        }

        return response;
    }

    private void addItem(final MutableRepositoryItem item) {
        try {
            wineRepository.addItem(item);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static WineNoteOutput convertRepositoryItemToWineNoteResponse(final RepositoryItem repositoryItem) {
        final WineNoteOutput output = new WineNoteOutput();
        output.setId((Integer) repositoryItem.getPropertyValue(WineNotesConstants.PROPERTY_ID));
        output.setWineId((Integer) repositoryItem.getPropertyValue(WineNotesConstants.PROPERTY_WINE_ID));
        output.setUser((String) repositoryItem.getPropertyValue(WineNotesConstants.PROPERTY_USER));
        output.setNote((String) repositoryItem.getPropertyValue(WineNotesConstants.PROPERTY_NOTE));
        output.setOrdinal((Integer) repositoryItem.getPropertyValue(WineNotesConstants.PROPERTY_ORDINAL));
        output.setDate((String) repositoryItem.getPropertyValue(WineNotesConstants.PROPERTY_DATE));
        return output;
    }
}
