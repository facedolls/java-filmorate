package ru.yandex.practicum.filmorate.service.review.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.review.ReviewStorage;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.feedEvent.EventOperation;
import ru.yandex.practicum.filmorate.model.feedEvent.EventType;
import ru.yandex.practicum.filmorate.service.feedEvent.FeedEventService;
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
    private final FeedEventService feedEventService;

    @Override
    public Review getReviewById(Long id) {
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
        feedEventService.addFeedEvent(createdReview.getUserId(), EventType.REVIEW, EventOperation.ADD,
                createdReview.getReviewId());
        log.info("Create review {}", createdReview);
        return createdReview;
    }

    @Override
    public Review updateReview(Review review) {
        userService.isExistsIdUser(review.getUserId());
        filmService.isExistsIdFilm(Long.valueOf(review.getFilmId()).intValue());
        Review updatedReview = reviewStorage.updateReview(review);
        feedEventService.addFeedEvent(updatedReview.getUserId(), EventType.REVIEW, EventOperation.UPDATE,
                updatedReview.getReviewId());
        log.info("Update review {}", review);
        return updatedReview;
    }

    @Override
    public String addLike(Long id, Long userId) {
        userService.isExistsIdUser(userId);
        if (reviewStorage.isLikeExist(id, userId)) {
            log.warn("Like from user with id={} for the review with id={} already exists", userId, id);
            throw new AlreadyExistsException(String.format("Like from user with id=%d for the review with id=%d " +
                            "already exists",
                    userId, id));
        }

        reviewStorage.addLike(id, userId);

        log.info("Like from user id={} for the review id={} was added", userId, id);
        return String.format("Like from user id=%d for the review id=%d was added", userId, id);
    }

    @Override
    public String addDislike(Long id, Long userId) {
        userService.isExistsIdUser(userId);
        if (reviewStorage.isDislikeExist(id, userId)) {
            log.warn("Dislike from user with id={} for the review with id={} already exists", userId, id);
            throw new AlreadyExistsException(String.format("Dislike from user with id=%d for the review with id=%d " +
                            "already exists",
                    userId, id));
        }

        reviewStorage.addDislike(id, userId);

        log.info("Dislike from user id={} for the review id={} was added", userId, id);
        return String.format("Dislike from user id=%d for the review id=%d was added", userId, id);
    }

    @Override
    public String deleteLike(Long id, Long userId) {
        userService.isExistsIdUser(userId);
        if (!reviewStorage.isLikeExist(id, userId)) {
            log.warn("Like from user with id={} for the review with id={} not found", userId, id);
            throw new NotFoundException(String.format("Like from user with id=%d for the review with id=%d not found",
                    userId, id));
        }

        reviewStorage.deleteLike(id, userId);

        log.info("Like from user id={} for the review id={} was deleted", userId, id);
        return String.format("Like from user id=%d for the review id=%d was deleted", userId, id);
    }

    @Override
    public String deleteDislike(Long id, Long userId) {
        userService.isExistsIdUser(userId);
        if (!reviewStorage.isDislikeExist(id, userId)) {
            log.warn("Dislike from user with id={} for the review with id={} not found", userId, id);
            throw new NotFoundException(String.format("Dislike from user with id=%d for review with id=%d not found",
                    userId, id));
        }

        reviewStorage.deleteDislike(id, userId);

        log.info("Dislike from user id={} for the review id={} was deleted", userId, id);
        return String.format("Dislike from user id=%d for the review id=%d was deleted", userId, id);
    }

    @Override
    public String deleteReview(Long id) {
        isReviewExist(id);
        Review review = getReviewById(id);
        reviewStorage.deleteReview(id);
        feedEventService.addFeedEvent(review.getUserId(), EventType.REVIEW, EventOperation.REMOVE,
                review.getReviewId());
        log.info("Review with id={} deleted", id);
        return String.format("Review with id=%d deleted", id);
    }

    @Override
    public void isReviewValid(Review review) {
        if (review.getUserId() == 0) {
            log.warn("Incorrect userId={} was passed when creating the review: ", review.getUserId());
            throw new ValidationException("review must have userId");
        }

        if (review.getFilmId() == 0) {
            log.warn("Incorrect filmId={} was passed when creating the review: ", review.getFilmId());
            throw new ValidationException("review must have filmId");
        }

        if (review.getIsPositive() == null) {
            log.warn("Incorrect isPositive={} was passed when creating the review: ", review.getIsPositive());
            throw new ValidationException("review must have isPositive parameter");
        }
    }

    public void isReviewExist(Long reviewId) {
        boolean isExist = reviewStorage.isReviewExist(reviewId);
        if (!isExist) {
            log.warn("Review with id={} not found", reviewId);
            throw new ReviewNotFoundException(String.format("Review with id=%d not found", reviewId));
        }
    }
}
