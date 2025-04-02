package com.group.commitapp.domain;

import com.group.commitapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.attoparser.dom.Text;


@Entity
@Table(name = "badge")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Badge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000, nullable = false)
    private String description;

    @Builder
    private Badge(String name, String description){
        this.name = name;
        this.description = description;
    }

    public static Badge create(String name, String description){

        return Badge.builder()
                .name(name)
                .description(description)
                .build();
    }

}
