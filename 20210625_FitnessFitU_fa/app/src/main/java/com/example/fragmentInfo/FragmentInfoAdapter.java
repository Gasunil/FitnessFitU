//package com.example.fragmentInfo;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.lifecycle.Lifecycle;
//import androidx.viewpager2.adapter.FragmentStateAdapter;
//
//import org.jetbrains.annotations.NotNull;
//
//public class FragmentInfoAdapter extends FragmentStateAdapter {
//    public FragmentInfoAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
//        super(fragmentManager, lifecycle);
//    }
//
//    @NonNull
//    @NotNull
//    @Override
//    public Fragment createFragment(int position) {
//
//        switch (position){
//            case 1:
//                return new FragmentInfoLeg();
//            case 2:
//                return new FragmentInfoChest();
//            case 3:
//                return new FragmentInfoAbs();
//        }
//
//        return new FragmentInfoArm();
//    }
//
//    @Override
//    public int getItemCount() {
//        return 4;
//    }
//}
