package com.example.myfirstapp.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001,B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J&\u0010$\u001a\u0004\u0018\u00010%2\u0006\u0010&\u001a\u00020\'2\b\u0010(\u001a\u0004\u0018\u00010)2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\u0010\u0010*\u001a\u00020!2\u0006\u0010+\u001a\u00020\rH\u0002R\u001e\u0010\u0003\u001a\u00060\u0004R\u00020\u0000X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u00020\u000fX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u0015X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\u00020\u001bX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001f\u00a8\u0006-"}, d2 = {"Lcom/example/myfirstapp/fragment/GalleryFullscreenFragment;", "Landroidx/fragment/app/DialogFragment;", "()V", "galleryPagerAdapter", "Lcom/example/myfirstapp/fragment/GalleryFullscreenFragment$GalleryPagerAdapter;", "getGalleryPagerAdapter", "()Lcom/example/myfirstapp/fragment/GalleryFullscreenFragment$GalleryPagerAdapter;", "setGalleryPagerAdapter", "(Lcom/example/myfirstapp/fragment/GalleryFullscreenFragment$GalleryPagerAdapter;)V", "imageList", "Ljava/util/ArrayList;", "Lcom/example/myfirstapp/adapter/Image;", "selectedPosition", "", "tvGalleryTitle", "Landroid/widget/TextView;", "getTvGalleryTitle", "()Landroid/widget/TextView;", "setTvGalleryTitle", "(Landroid/widget/TextView;)V", "viewPager", "Landroidx/viewpager/widget/ViewPager;", "getViewPager", "()Landroidx/viewpager/widget/ViewPager;", "setViewPager", "(Landroidx/viewpager/widget/ViewPager;)V", "viewPagerPageChangeListener", "Landroidx/viewpager/widget/ViewPager$OnPageChangeListener;", "getViewPagerPageChangeListener$app_debug", "()Landroidx/viewpager/widget/ViewPager$OnPageChangeListener;", "setViewPagerPageChangeListener$app_debug", "(Landroidx/viewpager/widget/ViewPager$OnPageChangeListener;)V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "setCurrentItem", "position", "GalleryPagerAdapter", "app_debug"})
public final class GalleryFullscreenFragment extends androidx.fragment.app.DialogFragment {
    private java.util.ArrayList<com.example.myfirstapp.adapter.Image> imageList;
    private int selectedPosition;
    @org.jetbrains.annotations.NotNull()
    public android.widget.TextView tvGalleryTitle;
    @org.jetbrains.annotations.NotNull()
    public androidx.viewpager.widget.ViewPager viewPager;
    @org.jetbrains.annotations.NotNull()
    public com.example.myfirstapp.fragment.GalleryFullscreenFragment.GalleryPagerAdapter galleryPagerAdapter;
    @org.jetbrains.annotations.NotNull()
    private androidx.viewpager.widget.ViewPager.OnPageChangeListener viewPagerPageChangeListener;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final android.widget.TextView getTvGalleryTitle() {
        return null;
    }
    
    public final void setTvGalleryTitle(@org.jetbrains.annotations.NotNull()
    android.widget.TextView p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.viewpager.widget.ViewPager getViewPager() {
        return null;
    }
    
    public final void setViewPager(@org.jetbrains.annotations.NotNull()
    androidx.viewpager.widget.ViewPager p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.myfirstapp.fragment.GalleryFullscreenFragment.GalleryPagerAdapter getGalleryPagerAdapter() {
        return null;
    }
    
    public final void setGalleryPagerAdapter(@org.jetbrains.annotations.NotNull()
    com.example.myfirstapp.fragment.GalleryFullscreenFragment.GalleryPagerAdapter p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setCurrentItem(int position) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.viewpager.widget.ViewPager.OnPageChangeListener getViewPagerPageChangeListener$app_debug() {
        return null;
    }
    
    public final void setViewPagerPageChangeListener$app_debug(@org.jetbrains.annotations.NotNull()
    androidx.viewpager.widget.ViewPager.OnPageChangeListener p0) {
    }
    
    public GalleryFullscreenFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\bH\u0016J\u0018\u0010\f\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\nH\u0016\u00a8\u0006\u0011"}, d2 = {"Lcom/example/myfirstapp/fragment/GalleryFullscreenFragment$GalleryPagerAdapter;", "Landroidx/viewpager/widget/PagerAdapter;", "(Lcom/example/myfirstapp/fragment/GalleryFullscreenFragment;)V", "destroyItem", "", "container", "Landroid/view/ViewGroup;", "position", "", "obj", "", "getCount", "instantiateItem", "isViewFromObject", "", "view", "Landroid/view/View;", "app_debug"})
    public final class GalleryPagerAdapter extends androidx.viewpager.widget.PagerAdapter {
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public java.lang.Object instantiateItem(@org.jetbrains.annotations.NotNull()
        android.view.ViewGroup container, int position) {
            return null;
        }
        
        @java.lang.Override()
        public int getCount() {
            return 0;
        }
        
        @java.lang.Override()
        public boolean isViewFromObject(@org.jetbrains.annotations.NotNull()
        android.view.View view, @org.jetbrains.annotations.NotNull()
        java.lang.Object obj) {
            return false;
        }
        
        @java.lang.Override()
        public void destroyItem(@org.jetbrains.annotations.NotNull()
        android.view.ViewGroup container, int position, @org.jetbrains.annotations.NotNull()
        java.lang.Object obj) {
        }
        
        public GalleryPagerAdapter() {
            super();
        }
    }
}