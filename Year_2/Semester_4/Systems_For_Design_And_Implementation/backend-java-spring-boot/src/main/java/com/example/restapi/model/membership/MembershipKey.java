package com.example.restapi.model.membership;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MembershipKey implements Serializable {
    @Column(name = "library_id")
    private Long libraryID;

    @Column(name = "reader_id")
    private Long readerID;
}
