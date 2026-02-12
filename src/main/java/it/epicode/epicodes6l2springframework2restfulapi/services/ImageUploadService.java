package it.epicode.epicodes6l2springframework2restfulapi.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.epicode.epicodes6l2springframework2restfulapi.exceptions.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {
    private static final long MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024;

    private final Cloudinary cloudinary;

    public ImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) {
        validateImage(file);
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) result.get("secure_url");
            if (url == null || url.isBlank()) {
                url = (String) result.get("url");
            }
            if (url == null || url.isBlank()) {
                throw new BadRequestException("Upload riuscito ma URL mancante");
            }
            return url;
        } catch (IOException e) {
            throw new BadRequestException("Errore nella lettura del file");
        } catch (Exception e) {
            throw new BadRequestException("Errore durante l'upload dell'immagine");
        }
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File mancante o vuoto");
        }
        if (file.getSize() > MAX_IMAGE_SIZE_BYTES) {
            throw new BadRequestException("File troppo grande (max 5MB)");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Tipo di file non valido");
        }
    }
}
