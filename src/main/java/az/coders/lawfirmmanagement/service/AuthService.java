package az.coders.lawfirmmanagement.service;

import az.coders.lawfirmmanagement.dto.ImageDto;
import az.coders.lawfirmmanagement.dto.request.GrantRequest;
import az.coders.lawfirmmanagement.dto.request.RefreshTokenRequest;
import az.coders.lawfirmmanagement.dto.request.SignInRequest;
import az.coders.lawfirmmanagement.dto.request.SignUpRequest;
import az.coders.lawfirmmanagement.dto.response.JwtAuthResponse;
import az.coders.lawfirmmanagement.enums.Role;
import az.coders.lawfirmmanagement.model.Image;
import az.coders.lawfirmmanagement.model.User;
import az.coders.lawfirmmanagement.repository.UserRepository;
import az.coders.lawfirmmanagement.service.Impl.JWTServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTServiceImpl jwtServiceImpl;
    private final ImageService imageService;


    public User signUpReq(SignUpRequest signUpRequest) throws IOException {
        User user = new User();

        if(userRepository.findByUsername(signUpRequest.getEmail())==null){

            ImageDto imageDto = ImageDto.builder()
                    .base64(signUpRequest.getPhotoBase64())
                    .name(signUpRequest.getFirstName()+"_user_"+ (int) (Math.random() * 9000) +1000+".jpg")
                    .build();   //for saving file system

Image image = Image.builder()
                .name(imageDto.getName())
                        .build();   //save name to db

            Image image1 = imageService.saveImage(image);

            if (image1!=null){
                imageService.uploadImage(imageDto);
            }


            user.setFirstName(signUpRequest.getFirstName());
            user.setSecondName(signUpRequest.getLastName());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            user.setUsername(signUpRequest.getEmail());
            user.setRole(Role.USER);
            user.setImage(image);


            userRepository.save(user);

        }

        return user;

    }

        public JwtAuthResponse signIn(SignInRequest signInRequest){
        System.out.println(signInRequest.getPassword());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
                signInRequest.getPassword()));

        User user = userRepository.findByUsername(signInRequest.getEmail());
        String jwt = jwtServiceImpl.generateToken(user);

        String refreshToken = jwtServiceImpl.generateRefreshToken(new HashMap<>(),user);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();


        jwtAuthResponse.setToken(jwt);
        jwtAuthResponse.setRefreshToken(refreshToken);

        return jwtAuthResponse;
    }

    public JwtAuthResponse refreshToken (RefreshTokenRequest refresh){

        String token = refresh.getToken();
        String username = jwtServiceImpl.extractUsername(token);
        User user = userRepository.findByUsername(username);


        if (jwtServiceImpl.isTokenValid(token,user)){
            String newToken = jwtServiceImpl.generateToken(user);

            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();

            jwtAuthResponse.setToken(newToken);
            jwtAuthResponse.setRefreshToken(refresh.getToken());


            return jwtAuthResponse;
        }else return null;

    }

    public JwtAuthResponse grant(GrantRequest grantRequest) {
        String token = grantRequest.getToken();
        String username = jwtServiceImpl.extractUsername(token);
        User user = userRepository.findByUsername(username);


        user.setRole(Role.ADMIN);

        userRepository.save(user);

      return JwtAuthResponse.builder()
               .token(jwtServiceImpl.generateToken(user))
               .build();
    }
}