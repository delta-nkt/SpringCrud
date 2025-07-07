package com.example.DEMO_INTEGRATION.DAL.Repository;

import com.example.DEMO_INTEGRATION.DAL.Entities.NRLM.Voters4;
import com.example.DEMO_INTEGRATION.DAL.Interfaces.IVoters4Repository;
import com.example.DEMO_INTEGRATION.Utils.Logger.LoggerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Repository
public class Voters4Repository extends BaseRepository<Voters4, Integer> implements IVoters4Repository {

    @PersistenceContext
    private EntityManager em;

    private final LoggerUtil logger;

    public Voters4Repository(JpaEntityInformation<Voters4, ?> entityInformation,
                             EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.em = entityManager;
        this.logger = LoggerUtil.getLogger(getClass());
    }
    @Override
    @Transactional // Mark as read-only as it's a retrieval operation
    public List<Voters4> getAllVoters() {
        LocalDateTime startTime = LocalDateTime.now();
        String entityTypeName = Voters4.class.getSimpleName();
        Map<String, String> context = Map.of("entityType", entityTypeName);

        try {
            logger.log(LoggerUtil.LogLevel.DEBUG, "Starting fetch of all " + entityTypeName + " entities", null, context);

            // Directly call the findAll method from the BaseRepository
            List<Voters4> result = super.fetchAll(); // This calls the BaseRepository's implementation

            logger.log(LoggerUtil.LogLevel.INFO, "Successfully fetched " + result.size() + " " + entityTypeName + " entities", null,
                    Map.of(
                            "entityType", entityTypeName,
                            "entityCount", String.valueOf(result.size()),
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))
                    ));
            return result;
        } catch (PersistenceException e) {
            // Log the persistence exception and rethrow as a DataAccessException
            logger.log(LoggerUtil.LogLevel.ERROR, "Failed to fetch all " + entityTypeName + " entities", e, null, context);
            throw new DataAccessException("Unable to fetch all " + entityTypeName + " entities", e) {};
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            logger.log(LoggerUtil.LogLevel.ERROR, "An unexpected error occurred while fetching all " + entityTypeName + " entities", e, null, context);
            throw new DataAccessException("An unexpected error occurred while fetching all " + entityTypeName + " entities", e) {};
        }
    }

    @Override
    @Transactional // This is a write operation
    public boolean addVoters(List<Voters4> votersList) {
        LocalDateTime startTime = LocalDateTime.now();
        String entityTypeName = Voters4.class.getSimpleName();
        Map<String, String> context = Map.of("entityType", entityTypeName);

        if (votersList == null || votersList.isEmpty()) {
            logger.log(LoggerUtil.LogLevel.ERROR, "Invalid parameter: votersList is null/empty", null, context);
            throw new IllegalArgumentException("Voters list cannot be null or empty");
        }

        try {
            logger.log(LoggerUtil.LogLevel.DEBUG, "Starting save of " + votersList.size() + " " + entityTypeName + " entities", null, context);

            // Call BaseRepository saveAll (assuming your BaseRepository has addAll() like your pattern)
            super.addAll(votersList);

            logger.log(LoggerUtil.LogLevel.INFO, "Successfully saved " + votersList.size() + " " + entityTypeName + " entities", null,
                    Map.of(
                            "entityType", entityTypeName,
                            "entityCount", String.valueOf(votersList.size()),
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))
                    ));

            return true;
        } catch (PersistenceException e) {
            logger.log(LoggerUtil.LogLevel.ERROR, "Failed to save " + entityTypeName + " entities", e, null, context);
            throw new DataAccessException("Unable to save " + entityTypeName + " entities", e) {};
        } catch (Exception e) {
            logger.log(LoggerUtil.LogLevel.ERROR, "An unexpected error occurred while saving " + entityTypeName + " entities", e, null, context);
            throw new DataAccessException("An unexpected error occurred while saving " + entityTypeName + " entities", e) {};
        }
    }
    // UpDate
    @Override
    @Transactional // This is a write operation
    public boolean updateVoter(Voters4 voter) {
        LocalDateTime startTime = LocalDateTime.now();
        String entityTypeName = Voters4.class.getSimpleName();
        Map<String, String> context = Map.of(
                "entityType", entityTypeName,
                "entityId", voter.getId() != null ? voter.getId().toString() : "null"
        );

        if (voter == null || voter.getId() == null) {
            logger.log(LoggerUtil.LogLevel.ERROR, "Invalid parameter: voter is null or ID is null", null, context);
            throw new IllegalArgumentException("Voter object or ID cannot be null");
        }

        try {
            logger.log(LoggerUtil.LogLevel.DEBUG, "Starting update for " + entityTypeName + " with ID: " + voter.getId(), null, context);

            super.update(voter); // Assuming BaseRepository has an update() method

            logger.log(LoggerUtil.LogLevel.INFO, "Successfully updated " + entityTypeName + " with ID: " + voter.getId(), null,
                    Map.of(
                            "entityType", entityTypeName,
                            "entityId", voter.getId().toString(),
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))
                    ));

            return true;
        } catch (PersistenceException e) {
            logger.log(LoggerUtil.LogLevel.ERROR, "Failed to update " + entityTypeName + " with ID: " + voter.getId(), e, null, context);
            throw new DataAccessException("Unable to update " + entityTypeName + " with ID: " + voter.getId(), e) {};
        } catch (Exception e) {
            logger.log(LoggerUtil.LogLevel.ERROR, "An unexpected error occurred while updating " + entityTypeName + " with ID: " + voter.getId(), e, null, context);
            throw new DataAccessException("An unexpected error occurred while updating " + entityTypeName + " with ID: " + voter.getId(), e) {};
        }
    }

    // delete
    @Override
    @Transactional // This is a write operation
    public boolean deleteVoter(Integer voterId) {
        LocalDateTime startTime = LocalDateTime.now();
        String entityTypeName = Voters4.class.getSimpleName();
        Map<String, String> context = Map.of(
                "entityType", entityTypeName,
                "entityId", voterId != null ? voterId.toString() : "null"
        );

        if (voterId == null) {
            logger.log(LoggerUtil.LogLevel.ERROR, "Invalid parameter: voterId is null", null, context);
            throw new IllegalArgumentException("Voter ID cannot be null");
        }

        try {
            logger.log(LoggerUtil.LogLevel.DEBUG, "Starting delete for " + entityTypeName + " with ID: " + voterId, null, context);

            super.deleteById(voterId); // Assuming BaseRepository has deleteById()

            logger.log(LoggerUtil.LogLevel.INFO, "Successfully deleted " + entityTypeName + " with ID: " + voterId, null,
                    Map.of(
                            "entityType", entityTypeName,
                            "entityId", voterId.toString(),
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))
                    ));

            return true;
        } catch (PersistenceException e) {
            logger.log(LoggerUtil.LogLevel.ERROR, "Failed to delete " + entityTypeName + " with ID: " + voterId, e, null, context);
            throw new DataAccessException("Unable to delete " + entityTypeName + " with ID: " + voterId, e) {};
        } catch (Exception e) {
            logger.log(LoggerUtil.LogLevel.ERROR, "An unexpected error occurred while deleting " + entityTypeName + " with ID: " + voterId, e, null, context);
            throw new DataAccessException("An unexpected error occurred while deleting " + entityTypeName + " with ID: " + voterId, e) {};
        }
    }

}
