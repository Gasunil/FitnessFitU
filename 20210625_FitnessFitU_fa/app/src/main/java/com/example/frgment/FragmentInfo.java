package com.example.frgment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.example.fragmentInfo.FragmentInfoAdapter;
import com.example.fitnessfitu.R;
import com.google.android.material.tabs.TabLayout;

public class FragmentInfo extends Fragment {

//    TabLayout tabLayout;
//    ViewPager2 pager2;
//    FragmentInfoAdapter adapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        Context context = view.getContext();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);

        String[] videoIds = {"bSI7AEZUhvc", "3TyTGxBNwic", "qrt2fy_wmtU", "zcQ16cfJN9Q"};

        RecyclerView.Adapter recyclerViewAdapter = new RecyclerViewAdapter(videoIds, this.getLifecycle());
        recyclerView.setAdapter(recyclerViewAdapter);










//        tabLayout = view.findViewById(R.id.tab_layout);
//        pager2 = view.findViewById(R.id.view_pager2);
//
//        FragmentManager fm = getChildFragmentManager();
//        adapter = new FragmentInfoAdapter(fm, getLifecycle());
//        pager2.setAdapter(adapter);
//
//        tabLayout.addTab(tabLayout.newTab().setText("팔"));
//        tabLayout.addTab(tabLayout.newTab().setText("다리"));
//        tabLayout.addTab(tabLayout.newTab().setText("가슴"));
//        tabLayout.addTab(tabLayout.newTab().setText("복근"));
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                pager2.setCurrentItem(tab.getPosition());
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                tabLayout.selectTab(tabLayout.getTabAt(position));
//            }
//        });

        return view;
    }
}