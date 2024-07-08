package com.example.demo.Service;

import com.example.demo.Model.BlogPost;
import com.example.demo.Model.Comment;
import com.example.demo.Model.User;
import com.example.demo.Repository.BlogPostRepository;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    public Comment addComment(Long id, Comment comment, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username);

        BlogPost blogPost = blogPostRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Blog post not found with this id: " + comment.getBlogPost()));

        comment.setAuthor(currentUser);
        comment.setBlogPost(blogPost);

        return commentRepository.save(comment);
    }

    public void deleteComment(Long postId, Long commentId, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName());
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found with id: " + commentId));
        if (!comment.getBlogPost().getId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment does not belong to the specified blog");
        }
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this comment");
        }
        commentRepository.delete(comment);
    }

    public Comment editComment(Long postId, Long commentId, Comment updatedComment, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName());
        Comment existingComment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found with id: " + commentId));
        if (!existingComment.getBlogPost().getId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment does not belong to the specified blog");
        }
        if (!existingComment.getAuthor().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to edit this comment");
        }
        existingComment.setContent(updatedComment.getContent());
        return commentRepository.save(existingComment);
    }
}
