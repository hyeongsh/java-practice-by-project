package domain;

import java.util.Objects;

public final class User {

    private final String id;
    private final String name;

    public User(String id, String name) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id");
        } else if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name");
        }
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        User user = (User) obj;
        return this.getId().equals(user.getId()) && this.getName().equals(user.getName());
    }
}