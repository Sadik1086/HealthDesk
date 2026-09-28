package com.healthdesk.util;

import java.util.List;

public interface Searchable<T> {
    List<T> searchByName(String keyword);
}