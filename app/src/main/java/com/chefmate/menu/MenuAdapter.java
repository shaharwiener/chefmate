//package com.chefmate.menu;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import java.util.List;
//import com.chefmate.R;
//
//public class MenuAdapter extends ArrayAdapter<MenuItemModel> {
//
//    public MenuAdapter(@NonNull Context context, List<MenuItemModel> items) {
//        super(context, 0, items);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_menu_item, parent, false);
//        }
//
//        MenuItemModel item = getItem(position);
//
//        ImageView icon = convertView.findViewById(R.id.menu_item_icon);
//        TextView title = convertView.findViewById(R.id.menu_item_text);
//
//        if (item != null) {
//            icon.setImageResource(item.getIconResId());
//            title.setText(item.getTitle());
//        }
//
//        return convertView;
//    }
//}
//
