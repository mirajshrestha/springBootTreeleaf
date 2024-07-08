package com.example.demo.Controller;

import com.example.demo.Model.BlogPost;
import com.example.demo.Service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class BlogPostController {
    @Autowired
    private BlogPostService blogPostService;

    @GetMapping("/")
    public ResponseEntity<?> getAllPosts() {
        try {
            List<BlogPost> posts = blogPostService.getAllPosts();
            return ResponseEntity.ok(posts);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving posts");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<BlogPost>> getBlogPost(@PathVariable Long id) {
        try {
            Optional<BlogPost> post = blogPostService.getBlogPost(id);
            return ResponseEntity.ok(post);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.empty());
        }

    }

    @PostMapping("/create")
    public ResponseEntity<?> createBlogPost(@RequestPart("image") MultipartFile image, @RequestPart("blog") BlogPost blogPost, Authentication authentication) throws IOException {
        try {
            String imagePath = blogPostService.saveImage(image);
            blogPost.setThumbnailUrl(imagePath);
            BlogPost createdPost = blogPostService.createBlogPost(blogPost, authentication);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred: " + e.getMessage());
        }
    }
}
