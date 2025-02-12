package dmu.dasom.api.domain.google.controller;

import dmu.dasom.api.domain.google.service.GoogleApiService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/google")
@RequiredArgsConstructor
public class GoogleController {

    private final GoogleApiService googleApiService;

    private static final String SPREADSHEET_ID = "1Vpu6_2raNvJN_GGg7aGBmzV4cXG1rCoizHl9v7kbG2o";
    private static final String RANGE = "A1";

    @PostMapping("/write")
    public ResponseEntity<String> writeToSheet(@RequestParam String word){
        try{
            List<List<Object>> values = List.of(Collections.singletonList(word));

            googleApiService.writeToSheet(SPREADSHEET_ID, RANGE, values);
            return ResponseEntity.ok("Data written successfully to the spreadsheet" + word);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to write data to the spreadsheet" + e.getMessage());
        }
    }
}
