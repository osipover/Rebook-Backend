package ru.rebook.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_role")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
