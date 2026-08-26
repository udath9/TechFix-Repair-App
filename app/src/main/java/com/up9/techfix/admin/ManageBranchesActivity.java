package com.up9.techfix.admin;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;

import java.util.ArrayList;
import java.util.List;

public class ManageBranchesActivity extends AppCompatActivity {

    private RecyclerView recyclerBranches;

    private BranchAdapter branchAdapter;

    private List<Branch> branchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_branches);

        recyclerBranches =
                findViewById(R.id.recyclerBranches);

        recyclerBranches.setLayoutManager(
                new LinearLayoutManager(this)
        );

        branchList = new ArrayList<>();

        loadSampleBranches();

        branchAdapter =
                new BranchAdapter(branchList);

        recyclerBranches.setAdapter(branchAdapter);
    }

    private void loadSampleBranches() {

        branchList.add(
                new Branch(
                        1,
                        "Colombo Branch",
                        "Colombo, Sri Lanka",
                        "011-2345678",
                        6.9271,
                        79.8612
                )
        );

        branchList.add(
                new Branch(
                        2,
                        "Galle Branch",
                        "Galle, Sri Lanka",
                        "091-2345678",
                        6.0329,
                        80.2168
                )
        );
    }
}