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
import java.util.ArrayList;
import java.util.List;

import static com.gruppe_f.sep.businesslogic.CSV_Reader.csv_read;

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

    private File convertMultiPartToFile(MultipartFile file ) throws IOException {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }

    @PostMapping("/liga/upload")
    public ResponseEntity<?> uploadLiga( @RequestParam("file") MultipartFile file,
                                         @RequestParam("name") String name,
                                         @RequestParam("picture") MultipartFile multiParfile) {     //muss multiParfile bleiben, sonst kommt nen error :(

            try {
                File newfile = convertMultiPartToFile(file);
                ArrayList<String[]> list = csv_read(newfile);
                list.remove(0);
                List<LeagueData> data = new ArrayList<>();

                Liga liga = new Liga(name, StringUtils.cleanPath(multiParfile.getOriginalFilename()));

                for(String[] stringarr: list) {
                    LeagueData league = new LeagueData();
                    league.setMatchDay(Integer.parseInt(stringarr[0]));
                    league.setPlayer1(stringarr[2]);
                    league.setPlayer2(stringarr[4]);
                    league.setResult(stringarr[3]);
                    league.setDate(stringarr[1]);
                    league.setLiga(liga);
                    data.add(league);

                }
                liga.setLeagueData(data);
                ligaRepo.save(liga);


                String uploadDir = "Pictures/liga-photos/" + liga.getId();

                FileUploadUtil.saveFile(uploadDir, StringUtils.cleanPath(multiParfile.getOriginalFilename()), multiParfile);


            } catch (IOException e) {e.printStackTrace();}


        return new ResponseEntity<>("Hat geklappt", HttpStatus.OK);
    }
}
