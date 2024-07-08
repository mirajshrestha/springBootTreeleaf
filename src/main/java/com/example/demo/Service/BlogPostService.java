package com.example.demo.Service;

import com.example.demo.Model.BlogPost;
import com.example.demo.Model.User;
import com.example.demo.Repository.BlogPostRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {
    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    public String saveImage(MultipartFile image) throws IOException {
        String uploadDir = "uploads/";
        Path uploadPath = Path.of(uploadDir);

        System.out.println(uploadPath.toString());

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    public BlogPost createBlogPost(BlogPost blogPost, Authentication authentication) {
        if (blogPost == null) {
            throw new IllegalArgumentException("Blog post cannot be null");
        }
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication cannot be null");
        }

        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username);

        if (currentUser == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        blogPost.setAuthor(currentUser);
        try {
            return blogPostRepository.save(blogPost);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to save blog post due to data integrity violation", e);
        }
    }


    public List<BlogPost> getAllPosts() {
        try {
            return new ArrayList<>(blogPostRepository.findAll());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve all posts", e);
        }
    }

    public Optional<BlogPost> getBlogPost(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Blog post ID cannot be null");
        }
        try {
            return blogPostRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve blog post with id: " + id, e);
        }
    }
}
