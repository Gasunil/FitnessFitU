//package com.example.fragmentInfo;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.fitnessfitu.R;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
//
//public class FragmentInfoArm extends Fragment {
//
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private String mParam1;
//    private String mParam2;
//
//    public String layoutId;
//    public String youtubeVideoId;
//
//    public FragmentInfoArm() {
//        // Required empty public constructor
//    }
//
//    public static FragmentInfoArm newInstance(String param1, String param2) {
//        FragmentInfoArm fragment = new FragmentInfoArm();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_info_arm, container, false);
//
//
//
////        YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube1);
////        getLifecycle().addObserver(youTubePlayerView);
////
////        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
////            @Override
////            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
////
////                String videoId = "S0Q4gqBUs7c";
////                youTubePlayer.loadVideo(videoId, 0);
////                youTubePlayer.pause();
////
////            }
////        });
//
//
//
//
//        // Inflate the layout for this fragment
//        return view;
//    }
//
//}
//
