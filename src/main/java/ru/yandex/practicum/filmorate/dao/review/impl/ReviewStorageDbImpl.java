package ru.yandex.practicum.filmorate.dao.review.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.review.ReviewStorage;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Primary
public class ReviewStorageDbImpl implements ReviewStorage {
    protected final String sqlSelectOneReview = "SELECT * FROM review WHERE review_id = :reviewId";
    protected final String sqlSelectAllReviews = "SELECT * FROM review ORDER BY useful DESC";
    protected final String sqlSelectReviewsByFilm = "SELECT * FROM review WHERE film_id = :filmId " +
            "ORDER BY useful DESC LIMIT :count";
    protected final String sqlUpdateReview = "UPDATE review SET is_positive = :isPositive, content = :content " +
            "WHERE review_id = :reviewId";
    protected final String sqlDeleteReview = "DELETE FROM review WHERE review_id = :reviewId";
    protected final String sqlSelectIdReview = "SELECT review_id FROM review WHERE review_id = :reviewId";
    protected final String sqlInsertReviewLike = "INSERT INTO review_likes VALUES (:reviewId, :userId)";
    protected final String sqlInsertReviewDislike = "INSERT INTO review_dislikes VALUES (:reviewId, :userId)";
    protected final String sqlDeleteReviewLike = "DELETE FROM review_likes WHERE review_id = :reviewId AND " +
            "user_id = :userId";
    protected final String sqlDeleteReviewDislike = "DELETE FROM review_dislikes WHERE review_id = :reviewId AND " +
            "user_id = :userId";
    protected final String sqlSelectLike = "SELECT * FROM review_likes WHERE review_id = :reviewId AND " +
            "user_id = :userId";
    protected final String sqlSelectDislike = "SELECT * FROM review_dislikes WHERE review_id = :reviewId AND " +
            "user_id = :userId";
    protected final String sqlUpdateUseful = "UPDATE review SET useful = :useful WHERE review_id = :reviewId";
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;

    @Override
    public Review getReviewById(Long id) {
        Map<String, Object> params = Map.of("reviewId", id);
        List<Review> review = parameter.query(sqlSelectOneReview, params, new ReviewMapper());

        if (!review.isEmpty()) {
            return review.get(0);
        }
        return null;
    }

    @Override
    public Collection<Review> getAllReviews() {
        return parameter.query(sqlSelectAllReviews, new ReviewMapper());
    }

    @Override
    public Collection<Review> getReviews(Long filmId, Integer count) {
        Map<String, Object> params = Map.of("filmId", filmId, "count", count);
        return parameter.query(sqlSelectReviewsByFilm, params, new ReviewMapper());
    }

    @Override
    public Review createReview(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("review")
                .usingGeneratedKeyColumns("review_id");

        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(review);
        review.setReviewId(simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue());
        return getReviewById(review.getReviewId());
    }

    @Override
    public Review updateReview(Review review) {
        parameter.update(sqlUpdateReview, getReviewParams(review));
        return getReviewById(review.getReviewId());
    }

    @Override
    public void addLike(Long id, Long userId) {
        parameter.update(sqlInsertReviewLike, Map.of("reviewId", id, "userId", userId));
        int updatedUseful = getReviewById(id).getUseful() + 1;
        parameter.update(sqlUpdateUseful, Map.of("useful", updatedUseful, "reviewId", id));
    }

    @Override
    public void addDislike(Long id, Long userId) {
        parameter.update(sqlInsertReviewDislike, Map.of("reviewId", id, "userId", userId));
        int updatedUseful = getReviewById(id).getUseful() - 1;
        parameter.update(sqlUpdateUseful, Map.of("useful", updatedUseful, "reviewId", id));
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        parameter.update(sqlDeleteReviewLike, Map.of("reviewId", id, "userId", userId));
        int updatedUseful = getReviewById(id).getUseful() - 1;
        parameter.update(sqlUpdateUseful, Map.of("useful", updatedUseful, "reviewId", id));
    }

    @Override
    public void deleteDislike(Long id, Long userId) {
        parameter.update(sqlDeleteReviewDislike, Map.of("reviewId", id, "userId", userId));
        int updatedUseful = getReviewById(id).getUseful() + 1;
        parameter.update(sqlUpdateUseful, Map.of("useful", updatedUseful, "reviewId", id));
    }

    @Override
    public void deleteReview(Long id) {
        parameter.update(sqlDeleteReview, Map.of("reviewId", id));
    }

    @Override
    public boolean isReviewExist(Long reviewId) {
        List<Integer> id = parameter.query(
                sqlSelectIdReview, Map.of("reviewId", reviewId),
                (rs, rowNum) -> rs.getInt("review_id"));

        return id.size() == 1;
    }

    @Override
    public boolean isLikeExist(Long reviewId, Long userId) {
        List<Map<String, Object>> record = parameter.queryForList(
                sqlSelectLike, Map.of("reviewId", reviewId, "userId", userId));

        return record.size() == 1;
    }

    @Override
    public boolean isDislikeExist(Long reviewId, Long userId) {
        List<Map<String, Object>> record = parameter.queryForList(
                sqlSelectDislike, Map.of("reviewId", reviewId, "userId", userId));

        return record.size() == 1;
    }

    private Map<String, Object> getReviewParams(Review review) {
        return Map.of("filmId", review.getFilmId(), "userId", review.getUserId(),
                "isPositive", review.getIsPositive(), "content", review.getContent(),
                "useful", review.getUseful(), "reviewId", review.getReviewId());
    }
}
