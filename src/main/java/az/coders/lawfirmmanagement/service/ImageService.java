package az.coders.lawfirmmanagement.service;

import az.coders.lawfirmmanagement.model.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ImageService {
    ResponseEntity<String> uploadImage(MultipartFile multipartFile,Long id) throws IOException;
    ResponseEntity<byte[]> downloadImage(Long id) ;

    Image saveImage(Image image);

    List<Image> getAllImages();

    Image getById(Long id);
}
