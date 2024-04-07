package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@RequiredArgsConstructor
@AllArgsConstructor
public class Review {
    @NotNull
    private long reviewId;
    @NotNull
    private long filmId;
    @NotNull
    private long userId;
    private Boolean isPositive;
    @NotBlank
    @Size(max = 5000)
    private String content;
    @NotNull
    private int useful;

    public Review(long reviewId, long filmId, long userId, boolean isPositive, String content) {
        this.reviewId = reviewId;
        this.filmId = filmId;
        this.userId = userId;
        this.isPositive = isPositive;
        this.content = content;
        this.useful = 0;
    }

    public long getReviewId() {
        return this.reviewId;
    }

    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }

    public @NotNull long getFilmId() {
        return this.filmId;
    }

    public void setFilmId(@NotNull long filmId) {
        this.filmId = filmId;
    }

    public @NotNull long getUserId() {
        return this.userId;
    }

    public void setUserId(@NotNull long userId) {
        this.userId = userId;
    }

    public Boolean getIsPositive() {
        return this.isPositive;
    }

    public void setIsPositive(boolean isPositive) {
        this.isPositive = isPositive;
    }

    public @NotBlank @Size(max = 5000) String getContent() {
        return this.content;
    }

    public void setContent(@NotBlank @Size(max = 5000) String content) {
        this.content = content;
    }

    public int getUseful() {
        return this.useful;
    }

    public void setUseful(int useful) {
        this.useful = useful;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Review)) return false;
        final Review other = (Review) o;
        if (!other.canEqual(this)) return false;
        if (this.getReviewId() != other.getReviewId()) return false;
        if (this.getFilmId() != other.getFilmId()) return false;
        if (this.getUserId() != other.getUserId()) return false;
        if (this.getIsPositive() != other.getIsPositive()) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (!Objects.equals(this$content, other$content)) return false;
        return this.getUseful() == other.getUseful();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Review;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $reviewId = this.getReviewId();
        result = result * PRIME + (int) ($reviewId >>> 32 ^ $reviewId);
        final long $filmId = this.getFilmId();
        result = result * PRIME + (int) ($filmId >>> 32 ^ $filmId);
        final long $userId = this.getUserId();
        result = result * PRIME + (int) ($userId >>> 32 ^ $userId);
        result = result * PRIME + (this.getIsPositive() ? 79 : 97);
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
        result = result * PRIME + this.getUseful();
        return result;
    }

    public String toString() {
        return "Review(reviewId=" + this.getReviewId() + ", filmId=" + this.getFilmId() + ", userId=" + this.getUserId() + ", isPositive=" + this.getIsPositive() + ", content=" + this.getContent() + ", useful=" + this.getUseful() + ")";
    }
}
