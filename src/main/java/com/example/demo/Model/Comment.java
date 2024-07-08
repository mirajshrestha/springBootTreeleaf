package com.example.demo.Model;

import jakarta.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne
    private User author;

    @ManyToOne
    private BlogPost blogPost;

    @Transient
    private Long blogPostId;

    public Comment() {
    }

    public Comment(Long id, String content, User author, BlogPost blogPost, Long blogPostId) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.blogPost = blogPost;
        this.blogPostId = blogPostId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public BlogPost getBlogPost() {
        return blogPost;
    }

    public void setBlogPost(BlogPost blogPost) {
        this.blogPost = blogPost;
    }

    public Long getBlogPostId() {
        return blogPostId;
    }

    public void setBlogPostId(Long blogPostId) {
        this.blogPostId = blogPostId;
    }
}
