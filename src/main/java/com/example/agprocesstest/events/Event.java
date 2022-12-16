package com.example.agprocesstest.events;

import com.example.agprocesstest.sources.Source;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events", indexes = {
        @Index(name = "ts_idx", columnList = "ts"),
        @Index(name = "value_idx", columnList = "val")
})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Instant ts;
    private Long val;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "source_id",
            foreignKey = @ForeignKey(name = "SOURCE_ID_FK")
    )
    private Source source;
}
