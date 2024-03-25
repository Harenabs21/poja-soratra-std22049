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
            File originalFile = new File(id + "-original.txt");

            // Créer un FileWriter et un BufferedWriter pour le fichier original
            FileWriter originalFileWriter = new FileWriter(originalFile);
            BufferedWriter originalBufferedWriter = new BufferedWriter(originalFileWriter);

            // Écrire le texte poétique dans le fichier original
            originalBufferedWriter.write(text.toLowerCase());

            // Fermer le BufferedWriter du fichier original
            originalBufferedWriter.close();

            // Lire le fichier original
            String originalText = text.toLowerCase();

            // Créer un nouvel objet File pour le fichier modifié
            File modifiedFile = new File(id + "-modified.txt");

            // Créer un FileWriter et un BufferedWriter pour le fichier modifié
            FileWriter modifiedFileWriter = new FileWriter(modifiedFile);
            BufferedWriter modifiedBufferedWriter = new BufferedWriter(modifiedFileWriter);

            // Écrire le texte en majuscules dans le fichier modifié
            modifiedBufferedWriter.write(originalText.toUpperCase());

            // Fermer le BufferedWriter du fichier modifié
            modifiedBufferedWriter.close();
            bucket.upload(originalFile,id);
            bucket.upload(modifiedFile,id);

            return new ResponseEntity<>("Fichiers créés avec succès", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors de la création des fichiers", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

