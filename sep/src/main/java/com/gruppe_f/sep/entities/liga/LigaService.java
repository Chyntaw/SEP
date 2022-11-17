package com.gruppe_f.sep.entities.liga;

import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.businesslogic.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static com.gruppe_f.sep.businesslogic.CSV_Reader.csv_read;
import static com.gruppe_f.sep.businesslogic.FileUploadUtil.convertMultiPartToFile;
import static java.util.Map.entry;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

public class LigaService {


    private final LigaRepository ligaRepo;
    @Autowired
    public LigaService(LigaRepository ligaRepo) {
        this.ligaRepo = ligaRepo;
    }

    public Liga addLiga(Liga liga){
        return ligaRepo.save(liga);
    }


    @GetMapping("/liga/findAll")
    public ResponseEntity<List<Liga>> findAll(){
        List<Liga> leagueList = ligaRepo.findAll();
        for(Liga liga:leagueList) {
            liga.setLeagueData(null);
        }
        return new ResponseEntity<>(leagueList, HttpStatus.OK);
    }

    public Liga updateLiga(Liga liga){
        return ligaRepo.save(liga);
    }

    public void deleteLiga(Long ligaId){
        ligaRepo.deleteById(ligaId);
    }


    @PostMapping("/liga/upload")
    public ResponseEntity<?> uploadLiga( @RequestParam("file") MultipartFile file,
                                         @RequestParam("name") String name,
                                         @RequestParam(value = "picture", required = false) MultipartFile leaguePicture) {     //muss multiParfile bleiben, sonst kommt nen error :(

        Map<String, String> monthMap = Map.ofEntries(entry("Jan", "01"),entry("Feb", "02"),entry("Mar", "03"),entry("Apr", "04"),entry("May", "05"),entry("Jun", "06"),
                                                        entry("Jul", "07"),entry("Aug", "08"),entry("Sep", "09"),entry("Oct", "10"),entry("Nov", "11"),entry("Dec", "12"));

            try {
                File newfile = convertMultiPartToFile(file);
                ArrayList<String[]> csvData = csv_read(newfile);

                //Remove headers
                csvData.remove(0);
                List<LeagueData> data = new ArrayList<>();
                String[] dateConv;
                Liga liga = new Liga(name);

                for(String[] stringarr: csvData) {
                    LeagueData league = new LeagueData();
                    league.setMatchDay(Integer.parseInt(stringarr[0]));
                    league.setPlayer1(stringarr[2]);
                    league.setPlayer2(stringarr[4]);
                    league.setResult(stringarr[3]);
                    dateConv = stringarr[1].split(" ");
                    //If days of Date "d" -> convert to "dd"
                    if(dateConv[2].length() <2) dateConv[2] = "0"+dateConv[2];
                    league.setDate(dateConv[3]+"-"+monthMap.get(dateConv[1])+"-"+dateConv[2]);
                    league.setLiga(liga);
                    data.add(league);
                }

                if(leaguePicture != null){
                    liga.setLigaPicture(StringUtils.cleanPath(leaguePicture.getOriginalFilename()));
                    String uploadDir = "Pictures/liga-photos/" + liga.getId();
                    FileUploadUtil.saveFile(uploadDir, StringUtils.cleanPath(leaguePicture.getOriginalFilename()), leaguePicture);
                }

                //Adding new data to Liga object and saving to repo
                liga.setLeagueData(data);
                ligaRepo.save(liga);

            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error while reading CSV-Data", HttpStatus.REQUEST_TIMEOUT);
            }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
