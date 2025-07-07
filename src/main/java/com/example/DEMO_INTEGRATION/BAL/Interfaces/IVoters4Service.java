package com.example.DEMO_INTEGRATION.BAL.Interfaces;

import com.example.DEMO_INTEGRATION.DAL.Entities.NRLM.Voters4;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IVoters4Service {
    CompletableFuture<List<Voters4>> getAllVoters();

    CompletableFuture<Boolean> addVoters(List<Voters4> votersList);

    CompletableFuture<Boolean> updateVoter(Voters4 voter);

    CompletableFuture<Boolean> deleteVoter(Integer voterId);
}
