package com.menu.sheet.sample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.menu.sheet.MenuSheetLayout;
import com.menu.sheet.R;
import com.menu.sheet.commons.MenuSheetView;

/**
 * Activity demonstrating the use of {@link MenuSheetView}
 */
public class MenuActivity extends AppCompatActivity {

    protected MenuSheetLayout menuSheetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        menuSheetLayout = (MenuSheetLayout) findViewById(R.id.bottomsheet);
        menuSheetLayout.setPeekOnDismiss(true);
        findViewById(R.id.list_button).setOnClickListener(v -> showMenuSheet(MenuSheetView.MenuType.LIST));
        findViewById(R.id.grid_button).setOnClickListener(v -> showMenuSheet(MenuSheetView.MenuType.GRID));
    }

    private void showMenuSheet(final MenuSheetView.MenuType menuType) {
        MenuSheetView menuSheetView =
                new MenuSheetView(MenuActivity.this, menuType, "Create...", item -> {
                    Toast.makeText(MenuActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    if (menuSheetLayout.isSheetShowing()) {
                        menuSheetLayout.dismissSheet();
                    }
                    if (item.getItemId() == R.id.reopen) {
                        showMenuSheet(menuType == MenuSheetView.MenuType.LIST ? MenuSheetView.MenuType.GRID : MenuSheetView.MenuType.LIST);
                    }
                    return true;
                });
        menuSheetView.inflateMenu(R.menu.create);
        menuSheetLayout.showWithSheetView(menuSheetView);
    }
}
