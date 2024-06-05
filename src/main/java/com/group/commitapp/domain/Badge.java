package com.group.commitapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.attoparser.dom.Text;


@Entity
@Table(name = "badge")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeId;
    private String name;
    @Lob
    private String description;


}
