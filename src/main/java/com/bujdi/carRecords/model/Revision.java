package com.bujdi.carRecords.model;

import jakarta.persistence.*;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@Table(name = "revinfo")
@RevisionEntity
public class Revision {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "revinfo_seq"
    )
    @SequenceGenerator(
            name = "revinfo_seq",
            sequenceName = "revinfo_rev_seq",
            allocationSize = 1
    )
    @RevisionNumber
    private int rev;

    @RevisionTimestamp
    private long revtstmp;

    // getters only (no setters needed)
}