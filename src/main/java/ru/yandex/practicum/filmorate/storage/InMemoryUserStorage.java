package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Set<Integer>> friends = new HashMap<>();
    private int id = 1;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User getById(int id) {
        return users.get(id);
    }

    @Override
    public User add(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        int userId = generateId();
        user.setId(userId);
        users.put(user.getId(), user);
        friends.put(userId, new HashSet<>());
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(int id, int friendId) {
        getFriendsIds(id).add(friendId);
        getFriendsIds(friendId).add(id);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        getFriendsIds(id).remove(friendId);
        getFriendsIds(friendId).remove(id);
    }

    @Override
    public List<User> getAllFriends(int id) {
        return getFriends(getFriendsIds(id));
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        Set<Integer> commonFriendIds = getFriendsIds(id).stream()
                .filter(i -> getFriendsIds(otherId).contains(i))
                .collect(Collectors.toSet());
        return getFriends(commonFriendIds);
    }

    private int generateId() {
        return id++;
    }

    private Set<Integer> getFriendsIds(int id) {
        return friends.get(id);
    }

    private List<User> getFriends(Set<Integer> ids) {
        List<User> friends = new ArrayList<>();
        for (Integer i : ids) {
            friends.add(users.get(i));
        }
        return friends;
    }
}
