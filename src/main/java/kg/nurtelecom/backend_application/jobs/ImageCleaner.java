package kg.nurtelecom.backend_application.jobs;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ImageCleaner {
    private JdbcTemplate jdbcTemplate;
    public ImageCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private static final String SQL_QUERY_GET_ALL_IMAGES_FROM_PRODUCTS = "SELECT image_url FROM products";
    @Scheduled(cron = "0 0 * * * *")
    public void cleanImage() {
        jdbcTemplate.execute(SQL_QUERY_GET_ALL_IMAGES_FROM_PRODUCTS);

    }
}
