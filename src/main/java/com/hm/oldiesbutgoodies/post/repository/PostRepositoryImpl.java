package com.hm.oldiesbutgoodies.post.repository;

import com.hm.oldiesbutgoodies.comment.domain.QComment;
import com.hm.oldiesbutgoodies.common.domain.OwnerType;
import com.hm.oldiesbutgoodies.post.domain.Post;
import com.hm.oldiesbutgoodies.post.domain.PostStatus;
import com.hm.oldiesbutgoodies.post.domain.QPost;
import com.hm.oldiesbutgoodies.post.dto.request.SearchRequest;
import com.hm.oldiesbutgoodies.user.domain.QUser;
import com.hm.oldiesbutgoodies.user.domain.QUserProfile;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Post> searchByCondition(SearchRequest request, Pageable pageable) {
        QPost post = QPost.post;
        QUser user = QUser.user;
        QComment comment = QComment.comment;
        QUserProfile userProfile = QUserProfile.userProfile;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.deleted.eq(false));
        builder.and(post.postStatus.in(PostStatus.PUBLIC, PostStatus.MEMBER_ONLY));

        log.info(request.getKeyword());

        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            String keyword = request.getKeyword();
            switch (request.getFiled()) {
                case AUTHOR -> builder.and(
                        JPAExpressions.selectOne()
                                .from(userProfile)
                                .where(userProfile.eq(post.user.userProfile)
                                        .and(userProfile.nickname.containsIgnoreCase(keyword)))
                                .exists()
                );
                case TITLE -> builder.and(post.title.containsIgnoreCase(keyword));
                case CONTENT -> builder.and(post.content.containsIgnoreCase(keyword));
                case TITLE_CONTENT -> builder.and(post.title.containsIgnoreCase(keyword)
                        .or(post.content.containsIgnoreCase(keyword)));
                case COMMENT -> builder.and(
                        JPAExpressions
                                .selectOne()
                                .from(comment)
                                .where(comment.ownerType.eq(OwnerType.POST)
                                        .and(comment.ownerId.eq(post.id))
                                        .and(comment.deleted.eq(false))
                                        .and(comment.content.containsIgnoreCase(keyword)))
                                .exists()
                );
            }
        }

        // 정렬
        OrderSpecifier<?> orderSpecifier = switch (request.getSearchOrder()) {
            case LIKE_POPULAR -> post.likeCount.desc();
            case BOOKMARK_POPULAR -> post.bookmarkCount.desc();
            case VIEW -> post.viewCount.desc();
            case COMMENT_COUNT -> post.commentCount.desc();
            default -> post.createdAt.desc(); // RECENT
        };

        // 결과 조회
        List<Post> posts = queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .join(user.userProfile, userProfile).fetchJoin()
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count 쿼리
        long total = queryFactory
                .select(post.count())
                .from(post)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }
}
