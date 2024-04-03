package ru.yandex.practicum.filmorate.service.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewService {
    Review getReviewById(Long id);

    Collection<Review> getReviews(Long filmId, Integer count);

    Review createReview(Review review);

    Review updateReview(Review review);

    String addLike(Long id, Long userId);

    String addDislike(Long id, Long userId);

    String deleteLike(Long id, Long userId);

    String deleteDislike(Long id, Long userId);

    String deleteReview(Long id);

    void isReviewExist(Long reviewId);

    void isReviewValid(Review review);
}
