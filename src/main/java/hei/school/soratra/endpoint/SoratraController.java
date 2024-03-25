package hei.school.soratra.endpoint;

import hei.school.soratra.file.BucketComponent;
import hei.school.soratra.repository.model.GetURL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.time.Duration;

@RestController
public class SoratraController {

    private final BucketComponent bucket;

    public SoratraController(BucketComponent bucket) {
        this.bucket = bucket;
    }

    @PutMapping("/soratra/{id}")
    public ResponseEntity<String> createFiles(@PathVariable String id, @RequestBody String text) {
        try {
            // Créer un nouvel objet File pour le fichier original
            File originalFile =  File.createTempFile(id + "-original", ".txt");
            FileWriter originalFileWriter = new FileWriter(originalFile);
            originalFileWriter.append(text.toLowerCase());
            originalFileWriter.close();
            bucket.upload(originalFile,id);

            File modifiedFile =  File.createTempFile(id + "-modified",".txt");
            FileWriter modifiedFileWriter = new FileWriter(modifiedFile);
            modifiedFileWriter.append(text.toUpperCase());
            modifiedFileWriter.close();
            bucket.upload(modifiedFile,id);

            return new ResponseEntity<>("Fichiers créés avec succès", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors de la création des fichiers", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/soratra/{id}")
    public GetURL getOriginalAndModifiedText(@PathVariable String id) {
        GetURL urls = new GetURL();
        urls.setOriginalURL(bucket.presign(id+"-original.txt", Duration.ofMinutes(10)).toString());
        urls.setModifiedURL(bucket.presign(id+"-modified.txt", Duration.ofMinutes(10)).toString());
        return urls;
    }
}

