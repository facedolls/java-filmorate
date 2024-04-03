package ru.yandex.practicum.filmorate.service.review.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.review.ReviewStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.review.ReviewService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class ReviewServiceImpl implements ReviewService {
    private final ReviewStorage reviewStorage;
    private final FilmService filmService;
    private final UserService userService;

    @Override
    public Review getReviewById(Long id){
        isReviewExist(id);
        log.info("Received review id={}", id);
        return reviewStorage.getReviewById(id);
    }

    @Override
    public Collection<Review> getReviews(Long filmId, Integer count) {
        if (filmId == null) {
            log.info("Received all reviews");
            return reviewStorage.getAllReviews();
        }
        filmService.isExistsIdFilm(Long.valueOf(filmId).intValue());
        log.info("Received {} reviews for the film id={}", count, filmId);
        return reviewStorage.getReviews(filmId, count);
    }

    @Override
    public Review createReview(Review review) {
        userService.isExistsIdUser(review.getUserId());
        filmService.isExistsIdFilm(Long.valueOf(review.getFilmId()).intValue());
        Review createdReview = reviewStorage.createReview(review);
        log.info("Create review {}", createdReview);
        return createdReview;
    }

    @Override
    public Review updateReview(Review review) {
        isReviewExist(review.getReviewId());
        userService.isExistsIdUser(review.getUserId());
        filmService.isExistsIdFilm(Long.valueOf(review.getFilmId()).intValue());
        Review updatedReview = reviewStorage.updateReview(review);
        log.info("Update review {}", review);
        return updatedReview;
    }

    @Override
    public String addLike(Long id, Long userId) {
        userService.isExistsIdUser(userId);
        isLikeExist(id, userId);
        reviewStorage.addLike(id, userId);

        Review review = reviewStorage.getReviewById(id);
        review.setUseful(review.getUseful() + 1);

        log.info("Like from user id={} for the review id={} was added", userId, id);
        return String.format("Like from user id=%d for the review id=%d was added", userId, id);
    }

    @Override
    public String addDislike(Long id, Long userId) {
        userService.isExistsIdUser(userId);
        isDislikeExist(id, userId);
        reviewStorage.addDislike(id, userId);

        Review review = reviewStorage.getReviewById(id);
        review.setUseful(review.getUseful() - 1);
        reviewStorage.updateReview(review);

        log.info("Dislike from user id={} for the review id={} was added", userId, id);
        return String.format("Dislike from user id=%d for the review id=%d was added", userId, id);
    }

    @Override
    public String deleteLike(Long id, Long userId) {
        userService.isExistsIdUser(userId);
        isLikeExist(id, userId);
        reviewStorage.deleteLike(id, userId);

        Review review = reviewStorage.getReviewById(id);
        review.setUseful(review.getUseful() - 1);
        reviewStorage.updateReview(review);

        log.info("Like from user id={} for the review id={} was deleted", userId, id);
        return String.format("Like from user id=%d for the review id=%d was deleted", userId, id);
    }

    @Override
    public String deleteDislike(Long id, Long userId) {
        userService.isExistsIdUser(userId);
        isDislikeExist(id, userId);
        reviewStorage.deleteDislike(id, userId);

        Review review = reviewStorage.getReviewById(id);
        review.setUseful(review.getUseful() + 1);
        reviewStorage.updateReview(review);

        log.info("Dislike from user id={} for the review id={} was deleted", userId, id);
        return String.format("Dislike from user id=%d for the review id=%d was deleted", userId, id);
    }

    @Override
    public String deleteReview(Long id) {
        isReviewExist(id);
        reviewStorage.deleteReview(id);
        log.info("Review with id={} deleted", id);
        return String.format("Review with id=%d deleted", id);
    }

    private void isReviewExist(Long reviewId) {
        boolean isExist = reviewStorage.isReviewExist(reviewId);
        if (!isExist) {
            log.warn("Review with id={} not found", reviewId);
            throw new ReviewNotFoundException(String.format("Review with id=%d not found", reviewId));
        }
    }

    private void isLikeExist(Long reviewId, Long userId) {
        boolean isExist = reviewStorage.isLikeExist(reviewId, userId);
        if (!isExist) {
            log.warn("Like from user with id={} for the review with id={} not found", userId, reviewId);
            throw new NotFoundException(String.format("Like from user with id=%d for the review with id=%d not found",
                    userId, reviewId));
        }
    }

    private void isDislikeExist(Long reviewId, Long userId) {
        boolean isExist = reviewStorage.isDislikeExist(reviewId, userId);
        if (!isExist) {
            log.warn("Dislike from user with id={} for the review with id={} not found", userId, reviewId);
            throw new NotFoundException(String.format("Dislike from user with id=%d for review with id=%d not found",
                    userId, reviewId));
        }
    }
}
