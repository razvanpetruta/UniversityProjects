package com.example.restapi.model.membership;

import com.example.restapi.model.library.Library;
import com.example.restapi.model.reader.Reader;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "memberships", indexes = {
        @Index(name = "fk_library_id_memberships_index", columnList = "library_id"),
        @Index(name = "fk_reader_id_memberships_index", columnList = "reader_id")
})
public class Membership {
    @EmbeddedId
    private MembershipKey ID;
    @ManyToOne
    @MapsId("libraryID")
    @JsonIgnore
    @JoinColumn(name = "library_id")
    private Library library;
    @ManyToOne
    @MapsId("readerID")
    @JsonIgnore
    @JoinColumn(name = "reader_id")
    private Reader reader;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Membership that = (Membership) o;
        return getID() != null && Objects.equals(getID(), that.getID());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(ID);
    }
}
