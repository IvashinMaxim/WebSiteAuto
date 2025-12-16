package com.example.websiteauto.util;

public class PaginationUtils {
    public static Pagination calculate(int currentPage, int totalPages, int window) {

        if (totalPages < 1) {
            totalPages = 1;
        }

        int firstIndex = 0;
        int lastIndex = Math.max(0, totalPages - 1);

        int startPage;
        int endPage;

        if (totalPages <= (2 + 2 * window + 1)) {
            startPage = firstIndex;
            endPage = lastIndex;
        } else {
            startPage = Math.max(firstIndex + 1, currentPage - window);
            endPage = Math.min(lastIndex - 1, currentPage + window);

            int desiredSize = 2 * window + 1;
            int actualSize = endPage - startPage + 1;

            if (actualSize < desiredSize) {
                int need = desiredSize - actualSize;

                int canLeft = startPage - (firstIndex + 1);
                int addLeft = Math.min(canLeft, need);
                startPage -= addLeft;
                need -= addLeft;

                int canRight = (lastIndex - 1) - endPage;
                int addRight = Math.min(canRight, need);
                endPage += addRight;
            }
        }

        return new Pagination(startPage, endPage, totalPages, currentPage);
    }

    public static class Pagination {
        public final int startPage;
        public final int endPage;
        public final int totalPages;
        public final int currentPage;

        public Pagination(int startPage, int endPage, int totalPages, int currentPage) {
            this.startPage = startPage;
            this.endPage = endPage;
            this.totalPages = totalPages;
            this.currentPage = currentPage;
        }
    }
}
