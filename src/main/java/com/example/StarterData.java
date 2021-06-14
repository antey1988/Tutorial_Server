package com.example;

import com.example.entities.Tutorial;
import com.example.repositories.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StarterData implements CommandLineRunner {

    private TutorialRepository tutorialRepository;
    @Autowired
    public void setTutorialRepository(TutorialRepository tutorialRepository) {
        this.tutorialRepository = tutorialRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Tutorial tutorial = new Tutorial("First", "Commit first's tutorial", true);
        tutorialRepository.save(tutorial);
        tutorial = new Tutorial("Second", "Commit second's tutorial", false);
        tutorialRepository.save(tutorial);
        tutorial = new Tutorial("Paging", "Commit page's tutorial", false);
        tutorialRepository.save(tutorial);
        tutorial = new Tutorial("Sort", "Commit sort's tutorial", true);
        tutorialRepository.save(tutorial);
        tutorial = new Tutorial("Five", "Commit five's tutorial", true);
        tutorialRepository.save(tutorial);
    }
}
