package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface FriendshipStorage {

    void createFriendRequest(long requesterId, long approverId);

    void confirmFriendRequest (long requesterId, long approverId);

    List<Long> findFriendsByUser (long userId);

    void removeFriendship (long userId, long friendId);
    void removeFriendsForUser (long userId);

}
