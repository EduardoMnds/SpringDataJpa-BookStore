package com.bookstore.jpa.service;

import com.bookstore.jpa.dtos.BookRecordDTO;
import com.bookstore.jpa.models.BookModel;
import com.bookstore.jpa.models.ReviewModel;
import com.bookstore.jpa.repositories.AuthorRepository;
import com.bookstore.jpa.repositories.BookRepository;
import com.bookstore.jpa.repositories.PublisherRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<BookModel> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public BookModel saveBook(BookRecordDTO bookRecordDTO) {
        BookModel book = new BookModel();
        book.setTitle(bookRecordDTO.title());
        book.setPublisher(publisherRepository.findById(bookRecordDTO.publisherId()).get());
        book.setAuthors(authorRepository.findAllById(bookRecordDTO.authorIds()).stream().collect(Collectors.toSet()));

        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setComment(bookRecordDTO.reviewComment());
        reviewModel.setBook(book);

        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }
}
