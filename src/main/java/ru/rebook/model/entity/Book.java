package ru.rebook.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "t_book")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(name = "publication_date")
    private Integer dateOfPublication;

    private Double rating;

    @ManyToMany(
            cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "t_book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @ManyToMany(
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "t_book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @ManyToMany(
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "t_book_collection",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "collection_id")
    )
    private List<BookCollection> collections;

    @OneToMany(
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "book_id")
    private List<Review> reviews;

}
