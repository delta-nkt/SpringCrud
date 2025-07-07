package com.example.DEMO_INTEGRATION.DAL.Interfaces;

import com.example.DEMO_INTEGRATION.DAL.Entities.NRLM.Voters4;

import java.util.List;

public interface IVoters4Repository {
    List<Voters4> getAllVoters();
    boolean addVoters(List<Voters4> votersList);
    boolean updateVoter(Voters4 voter);
    boolean deleteVoter(Integer voterId);
}
