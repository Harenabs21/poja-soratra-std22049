package hei.school.soratra.endpoint;

import hei.school.soratra.file.BucketComponent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

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
}

