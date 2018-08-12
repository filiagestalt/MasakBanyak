package com.baskom.masakbanyak.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.baskom.masakbanyak.R;
import com.dm.emotionrating.library.EmotionView;
import com.dm.emotionrating.library.GradientBackgroundView;
import com.dm.emotionrating.library.RatingView;

import kotlin.Unit;

public class RatingActivity extends AppCompatActivity {
  
  private EmotionView mEmotionView;
  private RatingView mRatingView;
  private Button mButtonSubmit;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rating);
    
    mEmotionView = findViewById(R.id.emotionView);
    mRatingView = findViewById(R.id.ratingView);
    mButtonSubmit = findViewById(R.id.button_submit_rating);
    
    mRatingView.setRatingChangeListener((previousRating, newRating) -> {
      mEmotionView.setRating(previousRating, newRating);
      return Unit.INSTANCE;
    });
    
  }
}
