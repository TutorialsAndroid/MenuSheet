package com.menu.sheet.commons;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import com.menu.sheet.MenuSheetLayout;
import com.menu.sheet.OnSheetDismissedListener;

@SuppressWarnings({"unused", "RedundantSuppression"})
public final class BottomSheetFragmentDelegate implements OnSheetDismissedListener {

    private static final String SAVED_SHOWS_BOTTOM_SHEET = "bottomsheet:savedBottomSheet";
    private static final String SAVED_BACK_STACK_ID = "bottomsheet:backStackId";
    private static final String SAVED_BOTTOM_SHEET_LAYOUT_ID = "bottomsheet:bottomSheetLayoutId";

    @IdRes
    private int bottomSheetLayoutId = View.NO_ID;
    private MenuSheetLayout menuSheetLayout;
    private boolean dismissed;
    private boolean shownByMe;
    private boolean viewDestroyed;
    private boolean showsBottomSheet = true;
    private int backStackId = -1;

    private final BottomSheetFragmentInterface sheetFragmentInterface;
    private final Fragment fragment;

    public static BottomSheetFragmentDelegate create(BottomSheetFragmentInterface sheetFragmentInterface) {
        return new BottomSheetFragmentDelegate(sheetFragmentInterface);
    }

    private BottomSheetFragmentDelegate(BottomSheetFragmentInterface sheetFragmentInterface) {

        if (!(sheetFragmentInterface instanceof Fragment)) {
            throw new IllegalArgumentException("sheetFragmentInterface must be an instance of a Fragment too!");
        }

        this.sheetFragmentInterface = sheetFragmentInterface;
        this.fragment = (Fragment) sheetFragmentInterface;
    }

    /**
     * DialogFragment-like show() method for displaying this the associated sheet fragment
     *
     * @param manager FragmentManager instance
     * @param bottomSheetLayoutId Resource ID of the {@link MenuSheetLayout}
     */
    public void show(FragmentManager manager, @IdRes int bottomSheetLayoutId) {
        dismissed = false;
        shownByMe = true;
        this.bottomSheetLayoutId = bottomSheetLayoutId;
        manager.beginTransaction()
                .add(fragment, String.valueOf(bottomSheetLayoutId))
                .commit();
    }

    /**
     * DialogFragment-like show() method for displaying this the associated sheet fragment
     *
     * @param transaction FragmentTransaction instance
     * @param bottomSheetLayoutId Resource ID of the {@link MenuSheetLayout}
     * @return the back stack ID of the fragment after the transaction is committed.
     */
    public int show(FragmentTransaction transaction, @IdRes int bottomSheetLayoutId) {
        dismissed = false;
        shownByMe = true;
        this.bottomSheetLayoutId = bottomSheetLayoutId;
        transaction.add(fragment, String.valueOf(bottomSheetLayoutId));
        viewDestroyed = false;
        backStackId = transaction.commit();
        return backStackId;
    }

    /**
     * Dismiss the fragment and it's bottom sheet. If the fragment was added to the back stack, all
     * back stack state up to and including this entry will be popped. Otherwise, a new transaction
     * will be committed to remove this fragment.
     */
    public void dismiss() {
        dismissInternal(/*allowStateLoss=*/false);
    }

    /**
     * Version of {@link #dismiss()} that uses {@link FragmentTransaction#commitAllowingStateLoss()}.
     * See linked documentation for further details.
     */
    public void dismissAllowingStateLoss() {
        dismissInternal(/*allowStateLoss=*/true);
    }

    private void dismissInternal(boolean allowStateLoss) {
        if (dismissed) {
            return;
        }
        dismissed = true;
        shownByMe = false;
        if (menuSheetLayout != null) {
            menuSheetLayout.dismissSheet();
            menuSheetLayout = null;
        }
        viewDestroyed = true;
        if (backStackId >= 0) {
            fragment.getFragmentManager().popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            backStackId = -1;
        } else {
            FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
            ft.remove(fragment);
            if (allowStateLoss) {
                ft.commitAllowingStateLoss();
            } else {
                ft.commit();
            }
        }
    }

    /**
     * Corresponding onAttach() method
     *
     * @param context Context to match the Fragment API, unused.
     */
    public void onAttach(Context context) {
        if (!shownByMe) {
            dismissed = false;
        }
    }

    /**
     * Corresponding onDetach() method
     */
    public void onDetach() {
        if (!shownByMe && !dismissed) {
            dismissed = true;
        }
    }

    /**
     * Corresponding onCreate() method
     *
     * @param savedInstanceState Instance state, can be null.
     */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            showsBottomSheet = savedInstanceState.getBoolean(SAVED_SHOWS_BOTTOM_SHEET, showsBottomSheet);
            backStackId = savedInstanceState.getInt(SAVED_BACK_STACK_ID, -1);
            bottomSheetLayoutId = savedInstanceState.getInt(SAVED_BOTTOM_SHEET_LAYOUT_ID, View.NO_ID);
        }
    }

    /*
     * Retrieves the appropriate layout inflater, either the sheet's or the view's super container. Note that you should
     * handle the result of this in your getLayoutInflater method.
     *
     * @param savedInstanceState Instance state, here to match Fragment API but unused.
     * @param superInflater The result of the view's inflater, usually the result of super.getLayoutInflater()
     * @return the layout inflater to use

    @CheckResult
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState, LayoutInflater superInflater) {
        if (!showsBottomSheet) {
            return superInflater;
        }
        bottomSheetLayout = getBottomSheetLayout();
        if (bottomSheetLayout != null) {
            return LayoutInflater.from(bottomSheetLayout.getContext());
        }
        return LayoutInflater.from(fragment.getContext());
    }
    */

    /**
     * @return this fragment sheet's {@link MenuSheetLayout}.
     */
    public MenuSheetLayout getMenuSheetLayout() {
        if (menuSheetLayout == null) {
            menuSheetLayout = findBottomSheetLayout();
        }

        return menuSheetLayout;
    }

    @Nullable
    private MenuSheetLayout findBottomSheetLayout() {
        Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment != null) {
            View view = parentFragment.getView();
            if (view != null) {
                return (MenuSheetLayout) view.findViewById(bottomSheetLayoutId);
            } else {
                return null;
            }
        }
        Activity parentActivity = fragment.getActivity();
        if (parentActivity != null) {
            return (MenuSheetLayout) parentActivity.findViewById(bottomSheetLayoutId);
        }
        return null;
    }

    /**
     * Corresponding onActivityCreated() method
     *
     * @param savedInstanceState Instance state, here to match the Fragment API but unused
     */
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (!showsBottomSheet) {
            return;
        }

        View view = fragment.getView();
        if (view != null) {
            if (view.getParent() != null) {
                throw new IllegalStateException("BottomSheetFragment can not be attached to a container view");
            }
        }
    }

    /**
     * Corresponding onStart() method
     */
    public void onStart() {
        if (menuSheetLayout != null) {
            viewDestroyed = false;
            menuSheetLayout.showWithSheetView(fragment.getView(), sheetFragmentInterface.getViewTransformer());
            menuSheetLayout.addOnSheetDismissedListener(this);
        }
    }

    /**
     * Corresponding onSaveInstanceState() method
     *
     * @param outState The output state, here to match the Fragment API but unused
     */
    public void onSaveInstanceState(Bundle outState) {
        if (!showsBottomSheet) {
            outState.putBoolean(SAVED_SHOWS_BOTTOM_SHEET, false);
        }
        if (backStackId != -1) {
            outState.putInt(SAVED_BACK_STACK_ID, backStackId);
        }
        if (bottomSheetLayoutId != View.NO_ID) {
            outState.putInt(SAVED_BOTTOM_SHEET_LAYOUT_ID, bottomSheetLayoutId);
        }
    }

    /**
     * Corresponding onDestroyView() method
     */
    public void onDestroyView() {
        if (menuSheetLayout != null) {
            viewDestroyed = true;
            menuSheetLayout.dismissSheet();
            menuSheetLayout = null;
        }
    }

    @Override
    @CallSuper
    public void onDismissed(MenuSheetLayout menuSheetLayout) {
        if (!viewDestroyed) {
            dismissInternal(true);
        }
    }
}
