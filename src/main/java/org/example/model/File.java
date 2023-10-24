package org.example.model;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "File")
@Entity
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Expose
    private Long id;

    @Column(name = "text")
    @Expose
    private String text;

    @OneToOne(mappedBy = "file", fetch = FetchType.EAGER)
    private Event event;
}
