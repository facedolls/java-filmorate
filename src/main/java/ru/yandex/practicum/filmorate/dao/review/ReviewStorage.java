package ru.yandex.practicum.filmorate.dao.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewStorage {
    Review getReviewById(Long id);

    Collection<Review> getAllReviews();

    Collection<Review> getReviews(Long filmId, Integer count);

    Review createReview(Review review);

    Review updateReview(Review review);

    void addLike(Long id, Long userId);

    void addDislike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    void deleteDislike(Long id, Long userId);

    void deleteReview(Long id);

    boolean isReviewExist(Long reviewId);

    boolean isLikeExist(Long reviewId, Long userId);

    boolean isDislikeExist(Long reviewId, Long userId);
}
