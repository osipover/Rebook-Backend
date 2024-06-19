package ru.rebook.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "t_genre")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "genres")
    private List<Book> books;
}
