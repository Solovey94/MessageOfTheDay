package com.solovey.messageoftheday.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="text")
    private String text;

    @Column(name="creation_date")
    private LocalDate creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Customer author;

    public boolean like(Message message) {
        if (!author.equals(message.author)) return false;
        if (!text.equals(message.text)) return false;
        return Objects.equals(creationDate, message.creationDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (!Objects.equals(id, message.id)) return false;
        return like(message);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + text.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + creationDate.hashCode();
        return result;
    }

}
