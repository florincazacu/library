package com.example.library.model;

import com.example.library.model.security.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "loans")
public class Loan extends BaseEntity {

    private String loanDate;
    private String returnDate;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "book_id")
    @JsonIgnoreProperties("loan")
    private Book book;

    public Loan() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loanDate='" + loanDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", user=" + user.getUsername() +
                ", book=" + book.getTitle() +
                "} " + super.toString();
    }
}
