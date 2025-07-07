package com.example.DEMO_INTEGRATION.Controller;

import com.example.DEMO_INTEGRATION.BAL.Interfaces.IVoters4Service;
import com.example.DEMO_INTEGRATION.DAL.Entities.NRLM.Voters4;
import com.example.DEMO_INTEGRATION.Utils.ErrorResponse.ErrorResponseUtil;
import com.example.DEMO_INTEGRATION.Utils.Logger.LoggerUtil;
import com.example.DEMO_INTEGRATION.Utils.Logger.LoggerUtil.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/voters4")
public class Voters4Controller {

    private final IVoters4Service voters4Service;
    private final LoggerUtil logger;

    @Autowired
    public Voters4Controller(IVoters4Service voters4Service) {
        this.voters4Service = voters4Service;
        this.logger = LoggerUtil.getLogger(getClass());
    }

    // Endpoint: /api/v1/voters4/get-all-voters
    @GetMapping("/get-all-voters")
    public CompletableFuture<ResponseEntity<?>> getAllVoters() {
        Map<String, String> context = Map.of("operation", "getAllVoters");

        try {
            return voters4Service.getAllVoters()
                    .<ResponseEntity<?>>thenApply(result -> {
                        logger.log(LogLevel.INFO, "Controller: getAllVoters successful", null,
                                Map.of("operation", "getAllVoters", "resultCount", String.valueOf(result.size())));
                        return ResponseEntity.ok(result);
                    })
                    .exceptionally(ex -> {
                        logger.log(LogLevel.ERROR, "Controller: getAllVoters failed", ex, null, context);
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                        return ErrorResponseUtil.handleException((Exception) cause, logger);
                    });

        } catch (Exception ex) {
            logger.log(LogLevel.ERROR, "Controller: getAllVoters failed (sync)", ex, null, context);
            return CompletableFuture.completedFuture(ErrorResponseUtil.handleException(ex, logger));
        }
    }

    // Endpoint: /api/v1/voters4/add-voters
    @PostMapping("/add-voters")
    public CompletableFuture<ResponseEntity<?>> addVoters(@RequestBody List<Voters4> votersList) {
        Map<String, String> context = Map.of(
                "operation", "addVoters",
                "inputCount", String.valueOf(votersList.size())
        );

        try {
            return voters4Service.addVoters(votersList)
                    .<ResponseEntity<?>>thenApply(result -> {
                        logger.log(LogLevel.INFO, "Controller: addVoters successful", null, context);
                        return ResponseEntity.ok(result);
                    })
                    .exceptionally(ex -> {
                        logger.log(LogLevel.ERROR, "Controller: addVoters failed", ex, null, context);
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                        return ErrorResponseUtil.handleException((Exception) cause, logger);
                    });

        } catch (Exception ex) {
            logger.log(LogLevel.ERROR, "Controller: addVoters failed (sync)", ex, null, context);
            return CompletableFuture.completedFuture(ErrorResponseUtil.handleException(ex, logger));
        }
    }

    // Endpoint: /api/v1/voters4/update-voter
    @PutMapping("/update-voter")
    public CompletableFuture<ResponseEntity<?>> updateVoter(@RequestBody Voters4 voter) {
        Map<String, String> context = Map.of(
                "operation", "updateVoter",
                "voterId", voter.getId() != null ? voter.getId().toString() : "null"
        );

        try {
            return voters4Service.updateVoter(voter)
                    .<ResponseEntity<?>>thenApply(result -> {
                        logger.log(LogLevel.INFO, "Controller: updateVoter successful", null, context);
                        return ResponseEntity.ok(result);
                    })
                    .exceptionally(ex -> {
                        logger.log(LogLevel.ERROR, "Controller: updateVoter failed", ex, null, context);
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                        return ErrorResponseUtil.handleException((Exception) cause, logger);
                    });

        } catch (Exception ex) {
            logger.log(LogLevel.ERROR, "Controller: updateVoter failed (sync)", ex, null, context);
            return CompletableFuture.completedFuture(ErrorResponseUtil.handleException(ex, logger));
        }
    }

    // Endpoint: /api/v1/voters4/delete-voter/{voterId}
    @DeleteMapping("/delete-voter/{voterId}")
    public CompletableFuture<ResponseEntity<?>> deleteVoter(@PathVariable Integer voterId) {
        Map<String, String> context = Map.of(
                "operation", "deleteVoter",
                "voterId", voterId != null ? voterId.toString() : "null"
        );

        try {
            return voters4Service.deleteVoter(voterId)
                    .<ResponseEntity<?>>thenApply(result -> {
                        logger.log(LogLevel.INFO, "Controller: deleteVoter successful", null, context);
                        return ResponseEntity.ok(result);
                    })
                    .exceptionally(ex -> {
                        logger.log(LogLevel.ERROR, "Controller: deleteVoter failed", ex, null, context);
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                        return ErrorResponseUtil.handleException((Exception) cause, logger);
                    });

        } catch (Exception ex) {
            logger.log(LogLevel.ERROR, "Controller: deleteVoter failed (sync)", ex, null, context);
            return CompletableFuture.completedFuture(ErrorResponseUtil.handleException(ex, logger));
        }
    }
}