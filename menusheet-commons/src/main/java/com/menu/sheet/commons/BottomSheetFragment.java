package com.menu.sheet.commons;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.menu.sheet.ViewTransformer;

public class BottomSheetFragment extends Fragment implements BottomSheetFragmentInterface {

    private BottomSheetFragmentDelegate delegate;

    public BottomSheetFragment() { }

    @Override
    public void show(FragmentManager manager, @IdRes int bottomSheetLayoutId) {
        getDelegate().show(manager, bottomSheetLayoutId);
    }

    @Override
    public int show(FragmentTransaction transaction, @IdRes int bottomSheetLayoutId) {
        return getDelegate().show(transaction, bottomSheetLayoutId);
    }

    @Override
    public void dismiss() {
        getDelegate().dismiss();
    }

    @Override
    public void dismissAllowingStateLoss() {
        getDelegate().dismissAllowingStateLoss();
    }

    @Override
    public ViewTransformer getViewTransformer() {
        return null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getDelegate().onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getDelegate().onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().onCreate(savedInstanceState);
    }
/*
    @Override
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return getDelegate().getLayoutInflater(savedInstanceState, super.getLayoutInflater(savedInstanceState));
    }
*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDelegate().onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDelegate().onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getDelegate().onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        getDelegate().onDestroyView();
        super.onDestroyView();
    }

    private BottomSheetFragmentDelegate getDelegate() {
        if (delegate == null) {
            delegate = BottomSheetFragmentDelegate.create(this);
        }
        return delegate;
    }
}
