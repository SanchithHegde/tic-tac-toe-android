package me.sanchithhegde.tictactoe.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import me.sanchithhegde.tictactoe.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String IMAGE_URL = "https://www.gravatar.com/avatar/345c7da5daa5e728a11f7d89fa70170c?s=1024";
        ImageView imageView = findViewById(R.id.imageViewDeveloper);
        Picasso.get().load(IMAGE_URL).fit().centerCrop()
                .placeholder(R.drawable.ic_account_circle)
                .into(imageView);
    }

    public void launchNewGame(View view) {
        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);
    }
}
