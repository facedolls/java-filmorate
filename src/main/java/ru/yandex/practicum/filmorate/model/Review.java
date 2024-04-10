package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.*;
import java.util.Objects;

@RequiredArgsConstructor
@AllArgsConstructor
public class Review {
    private Long reviewId;
    @NotNull
    private Long filmId;
    @NotNull
    private Long userId;
    @NotNull
    private Boolean isPositive;
    @NotBlank
    @Size(max = 5000)
    private String content;
    private Integer useful = 0;

    public Review(Long filmId, Long userId, Boolean isPositive, String content) {
        this.filmId = filmId;
        this.userId = userId;
        this.isPositive = isPositive;
        this.content = content;
    }

    public Long getReviewId() {
        return this.reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public @NotNull Long getFilmId() {
        return this.filmId;
    }

    public void setFilmId(@NotNull Long filmId) {
        this.filmId = filmId;
    }

    public @NotNull Long getUserId() {
        return this.userId;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public Boolean getIsPositive() {
        return this.isPositive;
    }

    public void setIsPositive(Boolean isPositive) {
        this.isPositive = isPositive;
    }

    public @NotBlank @Size(max = 5000) String getContent() {
        return this.content;
    }

    public void setContent(@NotBlank @Size(max = 5000) String content) {
        this.content = content;
    }

    public Integer getUseful() {
        return this.useful;
    }

    public void setUseful(Integer useful) {
        this.useful = useful;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Review)) return false;
        final Review other = (Review) o;
        if (!other.canEqual(this)) return false;
        if (!this.getReviewId().equals(other.getReviewId())) return false;
        if (!this.getFilmId().equals(other.getFilmId())) return false;
        if (!this.getUserId().equals(other.getUserId())) return false;
        if (this.getIsPositive() != other.getIsPositive()) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (!Objects.equals(this$content, other$content)) return false;
        return this.getUseful().equals(other.getUseful());
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
        return "Review(reviewId=" + this.getReviewId() + ", filmId=" + this.getFilmId() + ", userId=" +
                this.getUserId() + ", isPositive=" + this.getIsPositive() + ", content=" + this.getContent() +
                ", useful=" + this.getUseful() + ")";
    }
}