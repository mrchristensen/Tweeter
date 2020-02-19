package edu.byu.cs.tweeter.model.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a follow relationship.
 */
public class Status {
//todo: clean up this code (comments)
    private final User user;
    private final String messageBody;

    public Status(@NotNull User user, @NotNull String messageBody) {
//    public Status(@NotNull String messageBody) {
        this.user = user;
        this.messageBody = messageBody;
    }

    public User getUser() {
        return user;
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
                messageBody.equals(status.messageBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, messageBody);
    }

    @Override
    public String toString() {
        return "Status{" +
                "user=" + user +
                ", messageBody='" + messageBody + '\'' +
                '}';
    }
//
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Status status = (Status) o;
//        return messageBody.equals(status.messageBody);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(messageBody);
//    }
//
//    @Override
//    public String toString() {
//        return "Status{" +
//                "messageBody='" + messageBody + '\'' +
//                '}';
//    }
}
