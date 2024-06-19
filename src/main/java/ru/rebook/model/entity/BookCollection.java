package ru.rebook.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "t_collection")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "collections")
    private List<Book> books;
}
