package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable @NotNull @Min(1) Long id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping
    public Collection<Review> getReviews(@RequestParam(required = false) Long filmId,
                                         @RequestParam(defaultValue = "10") Integer count) {
        return reviewService.getReviews(filmId, count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review createReview(@Valid @RequestBody Review review) {
        if (review.getReviewId() != 0) {
            log.warn("Incorrect id={} was passed when creating the review: ", review.getReviewId());
            throw new ValidationException("id for the review must not be specified");
        }
        reviewService.isReviewValid(review);
        return reviewService.createReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        reviewService.isReviewExist(review.getReviewId());
        reviewService.isReviewValid(review);
        return reviewService.updateReview(review);
    }

    @PutMapping("/{id}/like/{userId}")
    public String addLike(@PathVariable @NotNull @Min(1) Long id,
                          @PathVariable @NotNull @Min(1) Long userId) {
        return reviewService.addLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public String addDislike(@PathVariable @NotNull @Min(1) Long id,
                             @PathVariable @NotNull @Min(1) Long userId) {
        return reviewService.addDislike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String deleteLike(@PathVariable @NotNull @Min(1) Long id,
                             @PathVariable @NotNull @Min(1) Long userId) {
        return reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public String deleteDislike(@PathVariable @NotNull @Min(1) Long id,
                                @PathVariable @NotNull @Min(1) Long userId) {
        return reviewService.deleteDislike(id, userId);
    }

    @DeleteMapping("/{id}")
    public String deleteReview(@PathVariable @NotNull @Min(1) Long id) {
        return reviewService.deleteReview(id);
    }
}
