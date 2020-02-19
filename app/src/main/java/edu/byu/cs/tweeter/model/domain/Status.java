package edu.byu.cs.tweeter.model.domain;

import android.annotation.SuppressLint;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a follow relationship.
 */
public class Status implements Comparable<Status> {
    private final User user;
    private final LocalDateTime date;
    private final String messageBody;

    public Status(@NotNull User user, LocalDateTime date,@NotNull String messageBody) {
        this.user = user;
        this.date = date;
        this.messageBody = messageBody;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getMessageBody() {
        return messageBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return user.equals(status.user) &&
                date.equals(status.date) &&
                messageBody.equals(status.messageBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, date, messageBody);
    }

    @Override
    public String toString() {
        return "Status{" +
                "user=" + user +
                ", date=" + date +
                ", messageBody='" + messageBody + '\'' +
                '}';
    }

    @SuppressLint("NewApi")
    @Override
    public int compareTo(Status o) {
        return o.getDate().compareTo(getDate());
    }
}
