package com.backend.c4s.Utility;

import com.backend.c4s.Exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileUploadUtil {

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("jpg", "jpeg", "png", "webp");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB in bytes

    private FileUploadUtil() {}

    public static void validateImageFile(MultipartFile file) {
        // 1. Check if the file is null or empty
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Uploaded File Cannot be Empty.");
        }

        // 2. Check if the file size exceeds the 5MB limit

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum allowed limit of 5MB.");
        }

        // 3. Extract the original file name
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new BadRequestException("Invalid file format.");
        }

        // 4. Extract extension correctly after the dot
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

        // 5. Reject file if the extension is NOT in allowed extensions list
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("Only JPEG, JPG, PNG, and WEBP files are permitted.");
        }
    }
}