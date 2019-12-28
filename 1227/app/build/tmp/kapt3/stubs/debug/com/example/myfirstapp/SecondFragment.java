package com.example.myfirstapp;

import java.lang.System;

/**
 * A simple [Fragment] subclass.
 */
@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\u0010\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0005H\u0016J&\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/example/myfirstapp/SecondFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/example/myfirstapp/adapter/GalleryImageClickListener;", "()V", "SPAN_COUNT", "", "galleryAdapter", "Lcom/example/myfirstapp/adapter/GalleryImageAdapter;", "getGalleryAdapter", "()Lcom/example/myfirstapp/adapter/GalleryImageAdapter;", "setGalleryAdapter", "(Lcom/example/myfirstapp/adapter/GalleryImageAdapter;)V", "imageList", "Ljava/util/ArrayList;", "Lcom/example/myfirstapp/adapter/Image;", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "loadImages", "", "onClick", "position", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class SecondFragment extends androidx.fragment.app.Fragment implements com.example.myfirstapp.adapter.GalleryImageClickListener {
    private final int SPAN_COUNT = 3;
    private final java.util.ArrayList<com.example.myfirstapp.adapter.Image> imageList = null;
    @org.jetbrains.annotations.NotNull()
    public com.example.myfirstapp.adapter.GalleryImageAdapter galleryAdapter;
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.myfirstapp.adapter.GalleryImageAdapter getGalleryAdapter() {
        return null;
    }
    
    public final void setGalleryAdapter(@org.jetbrains.annotations.NotNull()
    com.example.myfirstapp.adapter.GalleryImageAdapter p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    private final void loadImages() {
    }
    
    @java.lang.Override()
    public void onClick(int position) {
    }
    
    public SecondFragment() {
        super();
    }
}