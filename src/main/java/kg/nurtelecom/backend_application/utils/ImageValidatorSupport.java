package kg.nurtelecom.backend_application.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class ImageValidatorSupport implements ConstraintValidator<ImageValidator, MultipartFile> {

    private long maxSize;
    private String[] allowedTypes;

    @Override
    public void initialize(ImageValidator constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
        this.allowedTypes = constraintAnnotation.allowedTypes();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return fail(context, "Image file is required");
        }

        if (file.getSize() > maxSize) {
            return fail(context, "Max file size exceeded. Allowed: " + readableSize(maxSize));
        }

        String contentType = file.getContentType();
        if (contentType == null || !isValidContentType(contentType)) {
            return fail(context, "Allowed types: " + Arrays.toString(allowedTypes));
        }

        return true;
    }

    private boolean isValidContentType(String contentType) {
        for (String type : allowedTypes) {
            if (type.equalsIgnoreCase(contentType)) {
                return true;
            }
        }
        return false;
    }

    private boolean fail(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }

    private String readableSize(long bytes) {
        return String.format("%.2fMB", bytes / (1024.0 * 1024.0));
    }
}