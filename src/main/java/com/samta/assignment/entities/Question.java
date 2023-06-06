package com.samta.assignment.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "question")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {

    @Id
    private Long id;

    private String answer;


    private String question;


    private int value;

    @JsonProperty("airdate")
    @Column(name = "air_date")
    private String airDate;

    @JsonProperty("created_at")
    @Column(name = "created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    @Column(name = "updated_at")
    private String updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @JsonProperty("game_id")
    @Column(name="game_id")
    private String gameId;

    @JsonProperty("invalid_count")
    @Column(name = "invalid_count")
    private Integer invalidCount;

}

