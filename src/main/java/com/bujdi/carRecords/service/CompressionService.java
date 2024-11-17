package com.bujdi.carRecords.service;

import com.bujdi.carRecords.utils.InMemoryMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CompressionService {
    public MultipartFile compressImage(MultipartFile file) throws IOException {
        if (file.getSize() < 1024 * 1024) { // If file is smaller than 1MB
            return file; // Skip resizing/compression
        }
        // Read the image
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        // Determine the target size and compress
        int targetWidth = Math.min(originalImage.getWidth(), 1280);
        int targetHeight = (int) ((double) originalImage.getHeight() * targetWidth / originalImage.getWidth());
        BufferedImage resizedImage = resizeImage(originalImage, targetWidth, targetHeight);

        // Convert to compressed format
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String format = "image/png".equals(file.getContentType()) ? "png" : "jpg";
        ImageIO.write(resizedImage, format, outputStream);

        // Wrap the compressed data into a new MultipartFile
        return new InMemoryMultipartFile(
                file.getName(),
                file.getOriginalFilename(),
                file.getContentType(),
                outputStream.toByteArray()
        );
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        // Ensure the new image has an appropriate color model
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = resizedImage.createGraphics();

        // Apply high-quality rendering hints
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Perform the scaling
        g.drawImage(originalImage, 0, 0, width, height, null);

        g.dispose();
        return resizedImage;
    }
}
