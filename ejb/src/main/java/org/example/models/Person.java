package org.example.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Person {

    @NotNull
    @Min(value = 1)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Valid
    private Coordinates coordinates;

//    @Generated
//    @JsonbDateFormat(value = "yyyy-MM-dd")
//    private Date creationDate;

    @NotNull
    @Min(value = 1)
    private Long height;

//    @NotNull
//    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
//    private ZonedDateTime birthday;

    @NotNull
    @Min(value = 1)
    private Double weight;

    @NotNull
    @Valid
    private Country nationality;

    @NotNull
    @Valid
    private Location location;

    @NotBlank
    private String hairColor;
}