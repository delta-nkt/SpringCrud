package com.example.DEMO_INTEGRATION.BAL.Services;

import com.example.DEMO_INTEGRATION.BAL.Interfaces.IVoters4Service;
import com.example.DEMO_INTEGRATION.DAL.Entities.NRLM.Voters4;
import com.example.DEMO_INTEGRATION.DAL.Interfaces.IVoters4Repository;
import com.example.DEMO_INTEGRATION.Utils.Logger.LoggerUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class Voters4Service implements IVoters4Service {

    private final IVoters4Repository voters4Repository;
    private final LoggerUtil logger;

    public Voters4Service(IVoters4Repository voters4Repository) {
        this.voters4Repository = voters4Repository;
        this.logger = LoggerUtil.getLogger(getClass());
    }


    @Override
    @Async
    public CompletableFuture<List<Voters4>> getAllVoters() {
        LocalDateTime startTime = LocalDateTime.now();

        try {
            logger.log(LoggerUtil.LogLevel.DEBUG, "Fetching all voters4 records", null,
                    Map.of("operation", "getAllVoters"));

            List<Voters4> result = voters4Repository.getAllVoters();

            logger.log(LoggerUtil.LogLevel.INFO, "Successfully fetched voters4 records", null,
                    Map.of(
                            "operation", "getAllVoters",
                            "resultCount", String.valueOf(result.size()),
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))
                    ));

            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            logger.log(LoggerUtil.LogLevel.ERROR, "Failed to fetch voters4 records", e, null,
                    Map.of("operation", "getAllVoters"));
            throw e;
        }
    }

    @Override
    @Async
    public CompletableFuture<Boolean> addVoters(List<Voters4> votersList) {
        LocalDateTime startTime = LocalDateTime.now();

        try {
            logger.log(LoggerUtil.LogLevel.DEBUG, "Adding voters4 records", null,
                    Map.of("operation", "addVoters"));

            boolean result = voters4Repository.addVoters(votersList);

            logger.log(LoggerUtil.LogLevel.INFO, "Successfully added voters4 records", null,
                    Map.of(
                            "operation", "addVoters",
                            "addedCount", String.valueOf(votersList.size()),
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))
                    ));

            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            logger.log(LoggerUtil.LogLevel.ERROR, "Failed to add voters4 records", e, null,
                    Map.of("operation", "addVoters"));
            throw new DataAccessException("Unexpected error adding voters", e) {};
        }
    }

    @Override
    @Async
    public CompletableFuture<Boolean> updateVoter(Voters4 voter) {
        LocalDateTime startTime = LocalDateTime.now();

        try {
            logger.log(LoggerUtil.LogLevel.DEBUG, "Updating voter4 record", null,
                    Map.of("operation", "updateVoter", "voterId", voter.getId() != null ? voter.getId().toString() : "null"));

            boolean result = voters4Repository.updateVoter(voter);

            logger.log(LoggerUtil.LogLevel.INFO, "Successfully updated voter4 record", null,
                    Map.of(
                            "operation", "updateVoter",
                            "voterId", voter.getId() != null ? voter.getId().toString() : "null",
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))
                    ));

            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            logger.log(LoggerUtil.LogLevel.ERROR, "Failed to update voter4 record", e, null,
                    Map.of("operation", "updateVoter", "voterId", voter.getId() != null ? voter.getId().toString() : "null"));
            throw new DataAccessException("Unexpected error updating voter", e) {};
        }
    }

    @Override
    @Async
    public CompletableFuture<Boolean> deleteVoter(Integer voterId) {
        LocalDateTime startTime = LocalDateTime.now();

        try {
            logger.log(LoggerUtil.LogLevel.DEBUG, "Deleting voter4 record", null,
                    Map.of("operation", "deleteVoter", "voterId", voterId != null ? voterId.toString() : "null"));

            boolean result = voters4Repository.deleteVoter(voterId);

            logger.log(LoggerUtil.LogLevel.INFO, "Successfully deleted voter4 record", null,
                    Map.of(
                            "operation", "deleteVoter",
                            "voterId", voterId != null ? voterId.toString() : "null",
                            "durationMs", String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS))
                    ));

            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            logger.log(LoggerUtil.LogLevel.ERROR, "Failed to delete voter4 record", e, null,
                    Map.of("operation", "deleteVoter", "voterId", voterId != null ? voterId.toString() : "null"));
            throw new DataAccessException("Unexpected error deleting voter", e) {};
        }
    }
}