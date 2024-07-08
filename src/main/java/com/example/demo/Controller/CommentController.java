package com.example.demo.Controller;

import com.example.demo.Model.Comment;
import com.example.demo.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/{id}")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody Comment comment, Authentication authentication){
        try {
            Comment addedComment = commentService.addComment(id, comment, authentication);
            return ResponseEntity.ok(addedComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the comment: " + e.getMessage());
        }
    }

    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Authentication authentication){
        try {
            commentService.deleteComment(postId, commentId, authentication);
            return ResponseEntity.ok("Comment Deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the comment: " + e.getMessage());
        }
    }

    @PutMapping("/{postId}/{commentId}")
    public ResponseEntity<?> editComment(
            @PathVariable Long postId, @PathVariable Long commentId, @RequestBody Comment updatedComment, Authentication authentication){
        try {
            Comment edited = commentService.editComment(postId, commentId, updatedComment, authentication);
            return ResponseEntity.ok(edited);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while editing the comment: " + e.getMessage());
        }
    }
}
