package com.vokal.hotdogdream;


import android.service.dreams.DreamService;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vokal.hotdogdream.view.Bouncer;

public class HotDogDreamService extends DreamService {
    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();

        // Our content view will take care of animating its children.
        final Bouncer bouncer = new Bouncer(this);
        bouncer.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        bouncer.setSpeed(200); // pixels/sec

        // Add some views that will be bounced around.
        // Here I'm using ImageViews but they could be any kind of
        // View or ViewGroup, constructed in Java or inflated from
        // resources.
        for (int i=0; i<5; i++) {
            final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
            final ImageView image = new ImageView(this);
            image.setImageResource(R.drawable.ic_launcher);
            image.setBackgroundColor(0xFF004000);
            bouncer.addView(image, lp);
        }

        setContentView(bouncer);
    }
}

