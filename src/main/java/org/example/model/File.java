package org.example.model;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "files")
@Entity
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "file_name")
    private String fileName;

    @OneToOne(mappedBy = "file", fetch = FetchType.LAZY)
    private Event event;
}
