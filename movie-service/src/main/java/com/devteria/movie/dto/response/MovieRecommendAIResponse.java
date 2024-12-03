package com.devteria.movie.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieRecommendAIResponse {
    String movieId;
    String estimated_sentiment_score;
}
