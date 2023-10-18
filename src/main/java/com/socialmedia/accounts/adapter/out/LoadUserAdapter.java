package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
public class LoadUserAdapter implements LoadUserPort {

    private static final String LOAD_USER_BY_EMAIL_STATEMENT = "SELECT * FROM users WHERE email = '%s';";
    private static final String LOAD_USER_BY_ID_STATEMENT = "SELECT * FROM users WHERE id = '%s';";
    private static final String LOAD_ROLE_BY_ID_STATEMENT = "SELECT * FROM roles WHERE id = '%s';";
    private static final String LOAD_FOLLOWS_BY_FOLLOWING_ID_STATEMENT = "SELECT * FROM follows WHERE following_id = '%s';";
    private static final String LOAD_FOLLOWS_BY_FOLLOWER_ID_STATEMENT = "SELECT * FROM follows WHERE follower_id = '%s';";

    @Override
    public Optional<User> loadUserByEmail(String email) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement userStatement = conn.createStatement();
            String userQuery = String.format(LOAD_USER_BY_EMAIL_STATEMENT,email);
            ResultSet userResultSet = userStatement.executeQuery(userQuery);

            if (userResultSet.next()) {
                UUID userId = (UUID) userResultSet.getObject("id");
                String userEmail = userResultSet.getString("email");
                String hashedPassword = userResultSet.getString("hashed_password");
                boolean verified = userResultSet.getBoolean("verified");
                Long roleId = userResultSet.getLong("role_id");
                Instant userCreationTime = userResultSet.getTimestamp("creation_time").toInstant();

                Statement roleStatement = conn.createStatement();
                String roleQuery = String.format(LOAD_ROLE_BY_ID_STATEMENT, roleId);
                ResultSet roleResultSet = roleStatement.executeQuery(roleQuery);

                if (roleResultSet.next()) {
                    String roleName = roleResultSet.getString("role_name");
                    boolean hasPostCharsLimit = roleResultSet.getBoolean("has_post_chars_limit");
                    Long postCharsLimit = roleResultSet.getLong("post_chars_limit");
                    boolean hasCommentsLimit = roleResultSet.getBoolean("has_comments_limit");
                    Long commentsLimit = roleResultSet.getLong("comments_limit");
                    Instant roleCreationTime = roleResultSet.getTimestamp("creation_time").toInstant();

                    Role role = new Role(
                            roleId,
                            roleName,
                            hasPostCharsLimit,
                            postCharsLimit,
                            hasCommentsLimit,
                            commentsLimit,
                            roleCreationTime);

                    List<Follow> followers = new ArrayList<>();
                    List<Follow> following = new ArrayList<>();

                    Statement followerStatement = conn.createStatement();
                    String followerQuery = String.format(LOAD_FOLLOWS_BY_FOLLOWING_ID_STATEMENT, userId);
                    ResultSet followerResultSet = followerStatement.executeQuery(followerQuery);


                    while (followerResultSet.next()) {
                        UUID followersFollowerId = (UUID) followerResultSet.getObject("follower_id");
                        UUID followersFollowingId = (UUID) followerResultSet.getObject("following_id");
                        Instant followersFollowCreationTime = followerResultSet.getTimestamp("creation_time").toInstant();

                        followers.add(new Follow(
                                followersFollowerId,
                                followersFollowingId,
                                followersFollowCreationTime
                        ));
                    }

                    Statement followingStatement = conn.createStatement();
                    String followingQuery = String.format(LOAD_FOLLOWS_BY_FOLLOWER_ID_STATEMENT, userId);
                    ResultSet followingResultSet = followingStatement.executeQuery(followingQuery);

                    while (followingResultSet.next()) {
                        UUID followingsFollowerId = (UUID) followerResultSet.getObject("follower_id");
                        UUID followingsFollowingId = (UUID) followerResultSet.getObject("following_id");
                        Instant followingsFollowCreationTime = followerResultSet.getTimestamp("creation_time").toInstant();

                        following.add(new Follow(
                                followingsFollowerId,
                                followingsFollowingId,
                                followingsFollowCreationTime
                        ));
                    }

                    return Optional.of(new User(
                            userId,
                            userEmail,
                            hashedPassword,
                            verified,
                            role,
                            userCreationTime,
                            followers,
                            following
                    ));
                }
                else {return Optional.empty();}
            }
            else {return Optional.empty();}
        });
    }

    @Override
    public Optional<User> loadUserById(UUID id) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement userStatement = conn.createStatement();
            String userQuery = String.format(LOAD_USER_BY_ID_STATEMENT,id);
            ResultSet userResultSet = userStatement.executeQuery(userQuery);

            if (userResultSet.next()) {
                UUID userId = (UUID) userResultSet.getObject("id");
                String userEmail = userResultSet.getString("email");
                String hashedPassword = userResultSet.getString("hashed_password");
                boolean verified = userResultSet.getBoolean("verified");
                Long roleId = userResultSet.getLong("role_id");
                Instant userCreationTime = userResultSet.getTimestamp("creation_time").toInstant();

                Statement roleStatement = conn.createStatement();
                String roleQuery = String.format(LOAD_ROLE_BY_ID_STATEMENT, roleId);
                ResultSet roleResultSet = roleStatement.executeQuery(roleQuery);

                if (roleResultSet.next()) {
                    String roleName = roleResultSet.getString("role_name");
                    boolean hasPostCharsLimit = roleResultSet.getBoolean("has_post_chars_limit");
                    Long postCharsLimit = roleResultSet.getLong("post_chars_limit");
                    boolean hasCommentsLimit = roleResultSet.getBoolean("has_comments_limit");
                    Long commentsLimit = roleResultSet.getLong("comments_limit");
                    Instant roleCreationTime = roleResultSet.getTimestamp("creation_time").toInstant();

                    Role role = new Role(
                            roleId,
                            roleName,
                            hasPostCharsLimit,
                            postCharsLimit,
                            hasCommentsLimit,
                            commentsLimit,
                            roleCreationTime);

                    List<Follow> followers = new ArrayList<>();
                    List<Follow> following = new ArrayList<>();

                    Statement followerStatement = conn.createStatement();
                    String followerQuery = String.format(LOAD_FOLLOWS_BY_FOLLOWER_ID_STATEMENT, userId);
                    ResultSet followerResultSet = followerStatement.executeQuery(followerQuery);

                    while (followerResultSet.next()) {
                        UUID followersFollowerId = (UUID) followerResultSet.getObject("follower_id");
                        UUID followersFollowingId = (UUID) followerResultSet.getObject("following_id");
                        Instant followersFollowCreationTime = followerResultSet.getTimestamp("creation_time").toInstant();

                        followers.add(new Follow(
                                followersFollowerId,
                                followersFollowingId,
                                followersFollowCreationTime
                        ));
                    }

                    Statement followingStatement = conn.createStatement();
                    String followingQuery = String.format(LOAD_FOLLOWS_BY_FOLLOWING_ID_STATEMENT, userId);
                    ResultSet followingResultSet = followingStatement.executeQuery(followingQuery);

                    while (followingResultSet.next()) {
                        UUID followingsFollowerId = (UUID) followerResultSet.getObject("follower_id");
                        UUID followingsFollowingId = (UUID) followerResultSet.getObject("following_id");
                        Instant followingsFollowCreationTime = followerResultSet.getTimestamp("creation_time").toInstant();

                        followers.add(new Follow(
                                followingsFollowerId,
                                followingsFollowingId,
                                followingsFollowCreationTime
                        ));
                    }

                    return Optional.of(new User(
                            userId,
                            userEmail,
                            hashedPassword,
                            verified,
                            role,
                            userCreationTime,
                            followers,
                            following
                    ));
                }
                else {return Optional.empty();}
            }
            else {return Optional.empty();}
        });
    }
}
