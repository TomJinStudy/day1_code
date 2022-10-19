package com.jin.common;

import com.jin.domain.User;

public class UserHolder {
        private static final ThreadLocal<User> t = new ThreadLocal<User>();

        public static void setUser(User user) {
                t.set(user);
        }

        public static User getUser() {
                return t.get();
        }
        public static void remove() {
                t.remove();
        }

}