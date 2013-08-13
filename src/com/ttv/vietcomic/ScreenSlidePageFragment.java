package com.ttv.vietcomic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ttv.vietcomic.imageloader.ImageLoader;

@SuppressLint("ValidFragment")
public class ScreenSlidePageFragment extends Fragment {
	int imageResourceId;
	String url;
	ReadingViewActivity context;
	TouchImageView image;

	public ScreenSlidePageFragment(String url) {
		this.url = url;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ImageLoader loader = new ImageLoader(context);
		View V = inflater.inflate(R.layout.fragment_screen_slide_page,
				container, false);
		image = (TouchImageView) V.findViewById(R.id.imageView1);
		loader.DisplayImage(url, image);
		image.setMaxZoom(3f);
		return V;
	}
}
