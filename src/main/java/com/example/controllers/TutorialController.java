package com.example.controllers;

import com.example.entities.Tutorial;
import com.example.repositories.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TutorialController {
    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping("/tutorials")
    public ResponseEntity<Object> getAllTutorials(@RequestParam(required = false) String title,
                                                          @RequestParam(required = false) String description,
                                                          @RequestParam(defaultValue = "0") int size,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(name = "sort", defaultValue = "id,desc") String[] sort) {
        try{

            List<Tutorial> tutorials = new ArrayList<>();

            Sort sort1;
            if (sort[0].contains(",")) {
                List<Sort.Order> listOrders = new ArrayList<>();
                Sort.Order order;
                for (String string : sort) {
                    String[] words = string.split(",");
                    if (words[1].equalsIgnoreCase("asc")) {
                        order = Sort.Order.asc(words[0]);
                    } else {
                        order = Sort.Order.desc(words[0]);
                    }
                    listOrders.add(order);
                }
                sort1 = Sort.by(listOrders);
            } else {
                sort1 = Sort.by(sort[0]);
                if (sort[1].equalsIgnoreCase("desc")) {
                    sort1 = sort1.descending();
                }
            }

            Pageable pageable = null;
            if (size > 0 && page >= 0 ) {
                pageable = PageRequest.of(page, size, sort1);
            }

            if (pageable == null) {
                if (title != null) {
                    tutorials.addAll(tutorialRepository.findByTitleContaining(title, sort1));
                } else if (description != null) {
                    tutorials.addAll(tutorialRepository.findByDescriptionContaining(description, sort1));
                } else {
                    tutorials.addAll(tutorialRepository.findAll(sort1));
                }
                if (tutorials.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return  new ResponseEntity<>(tutorials, HttpStatus.OK);
            } else {
                Page<Tutorial> currentPage;
                if (title != null) {
                    currentPage = tutorialRepository.findByTitleContaining(title, pageable);
                } else if (description != null) {
                    currentPage = tutorialRepository.findByDescriptionContaining(description, pageable);
                } else {
                    currentPage = tutorialRepository.findAll(pageable);
                }
                Map<String, Object> response = new HashMap<>();
                response.put("tutorials" , currentPage.getContent());
                response.put("currentPage", currentPage.getNumber());
                response.put("totalItems", currentPage.getTotalElements());
                response.put("totalPages", currentPage.getTotalPages());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") Long id) {
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

        if (tutorialData.isPresent()) {
            return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
        try {
            Tutorial _tutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
            return new ResponseEntity<>(_tutorial, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable(value = "id") Long id, @RequestBody Tutorial tutorial) {
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

        if (tutorialData.isPresent()) {
            Tutorial _tutorial = tutorialData.get();
            _tutorial.setTitle(tutorial.getTitle());
            _tutorial.setDescription(tutorial.getDescription());
            _tutorial.setPublished(tutorial.isPublished());
            return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") Long id) {
        try {
            tutorialRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<HttpStatus> deleteTutorials() {
        try {
            tutorialRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<List<Tutorial>> findByPublished() {
        try{
            List<Tutorial> tutorials = tutorialRepository.findByPublished(true);
            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return  new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
