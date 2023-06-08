package com.example.Sweater.domain.util;
import com.example.Sweater.domain.User;

// делаем класс абстрактным чтобы его случайно не "фанцеировать"
public abstract class MessageHelper {
    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "<none>";
    }
}