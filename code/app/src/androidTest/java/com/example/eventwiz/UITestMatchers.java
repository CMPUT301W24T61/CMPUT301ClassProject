package com.example.eventwiz;

public class UITestMatchers {
    public static DrawableMatcher withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static DrawableMatcher noDrawable() {
        return new DrawableMatcher(-1);
    }
}