package az.coders.lawfirmmanagement.service.Impl;

import az.coders.lawfirmmanagement.dto.ImageDto;
import az.coders.lawfirmmanagement.model.Image;
import az.coders.lawfirmmanagement.model.User;
import az.coders.lawfirmmanagement.repository.ImageRepository;
import az.coders.lawfirmmanagement.repository.UserRepository;
import az.coders.lawfirmmanagement.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {


    public static String DIRECTORY="/home/nicat/Desktop/law-firm-management/src/main/resources/profile photos/";

private final ImageRepository imageRepository;
private final UserRepository userRepository;

    @Override
    public ResponseEntity<String> uploadImage(MultipartFile file,Long id) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException();
        }

        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("user not found"));

        Image image = new Image();
        String fileName = saveToFileSys(file);

        if (user.getImage() == null) {
            image.setName(fileName);
            imageRepository.save(image);
            user.setImage(image);
            userRepository.save(user);
        }else {
            String imageName = user.getImage().getName();
            Image byName = imageRepository.findByName(imageName);
            byName.setName(fileName);
            imageRepository.save(byName);
        }
        return new ResponseEntity<>("good",HttpStatusCode.valueOf(200));
    }

    private String saveToFileSys(MultipartFile file) throws IOException {
        // Generate a unique file name (you may use your own logic)
        String fileName =  file.getOriginalFilename() + "_" + System.currentTimeMillis();

        // Build the file path
        Path filePath = Paths.get(DIRECTORY).resolve(fileName);

        // Save the file to the specified directory
        file.transferTo(filePath.toFile());

//            // Build the download URL for the client
//            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path("/downloadFile/")
//                    .path(fileName)
//                    .toUriString();
        return fileName;

    }

    @Override
    public ResponseEntity<byte[]> downloadImage(Long id) throws IOException {

        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("user not found"));

        Image userImage = user.getImage();

//        String imageDir = DIRECTORY + userImage.getName();
//
//        byte[] imageData = Files.readAllBytes(Paths.get(imageDir));
//
//        return Base64.getEncoder().encodeToString(imageData);//image base64 formatted

            // Resolve the image file path
            Path imagePath = Paths.get(DIRECTORY).resolve(userImage.getName());

            // Read the image bytes
            byte[] imageBytes = Files.readAllBytes(imagePath);

            // Set the content type and headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust the media type based on your image format
            headers.setContentDispositionFormData("inline", userImage.getName());

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }

    @Override
    public Image saveImage(Image image) {
        Image image1 = imageRepository.save(image);
        return image1;
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public Image getById(Long id) {
       return imageRepository.findById(id).orElseThrow(()->new RuntimeException("Image not found"));
    }


}