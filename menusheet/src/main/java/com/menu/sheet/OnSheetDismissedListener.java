package com.menu.sheet;

public interface OnSheetDismissedListener {

    /**
     * Called when the presented sheet has been dismissed.
     *
     * @param menuSheetLayout The bottom sheet which contained the presented sheet.
     */
    void onDismissed(MenuSheetLayout menuSheetLayout);

}
