package com.android.tutorial.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.tutorial.R;
import com.webianks.library.easyportfolio.EasyPortfolio;
import com.webianks.library.easyportfolio.Project;

import java.util.ArrayList;
import java.util.List;

public class PortfolioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Project> projectList = new ArrayList<>();

        Project pollstap = new Project();
        pollstap.setProjectName("PollsTap");
        pollstap.setProjectDesc("Polling based social networking app." +
                " You can start new polls and reach a conclusion based on the voting.");
        pollstap.setProjectLink("https://play.google.com/store/apps/details?id=com.webianks.pollstap");

        Project popupBubble = new Project();
        popupBubble.setProjectName("PopupBubble");
        popupBubble.setProjectDesc("Easily add and customise \"New Post\" popup button with the feeds (RecyclerView) of your app.");
        popupBubble.setProjectLink("https://github.com/webianks/PopupBubble");

        projectList.add(pollstap);
        projectList.add(popupBubble);

        new EasyPortfolio.Builder(this)
                .withGithubUrl("https://github.com/mannan541")
                .withPlayStoreUrl("https://play.google.com/store/apps/developer?id=Abdul+Mannan")
                .withLinkedInUrl("https://www.linkedin.com/in/abdul-mannan-17949550/")
                .withProjectList(projectList)
                .build()
                .start();
    }

}
