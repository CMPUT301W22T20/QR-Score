package com.example.qrscore;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Purpose: RecyclerAdapter for HomeFragment Players QR Codes.
 *
 * Outstanding Issues:
 *
 * @author: William Liu
 */
public class HomeFragmentQRCodeRecyclerAdapter extends RecyclerView.Adapter<HomeFragmentQRCodeRecyclerAdapter.MyViewHolder>{

    private Account account;
    private QRDataList qrDataList;
    private String hash;
    private ArrayList<QRCode> qrCodes;

    public HomeFragmentQRCodeRecyclerAdapter(Account account) {
        this.account = account;
        this.qrDataList = account.getQrDataList();
        this.qrCodes = qrDataList.getQRCodes();
    }

    /**
     * Purpose: ViewHolder of items inside the Adapter.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView rank;
        private TextView score;
        private TextView name;
        private ImageButton menuButton;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            rank = itemView.findViewById(R.id.list_item_rank);
            score = itemView.findViewById(R.id.list_item_score);
            name = itemView.findViewById(R.id.list_item_name);
            menuButton = itemView.findViewById(R.id.list_item_menu_button);
//            hash =
//
//            itemView.setOnClickListener(view -> {
//
//            });
        }
    }

    /**
     * Purpose: Set items on ViewHolder.
     * @param holder
     *      Represents a ViewHolder.
     *
     * @param position
     *      Represents the position on the ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull HomeFragmentQRCodeRecyclerAdapter.MyViewHolder holder, int position) {

        holder.rank.setText("NIL");
//        holder.score.setText(account.getQR().get(position).getQRScore());

        String name = account.getProfile().getFirstName() + " " + account.getProfile().getLastName();
        if (account.getProfile().getFirstName() == null) {
            holder.name.setText(account.getUserID());
        }
        else {
            holder.name.setText(name);
        }

        holder.menuButton.setOnClickListener(new MenuButtonOnClickListener(account.getQR().get(position).getHash()));
    }

    /**
     * Purpose: Returns the item count of the ViewHolder.
     *
     * @return
     *      Represents the number of items on the ViewHolder.
     */
    @Override
    public int getItemCount() {
        return qrCodes.size();
    }

    @NonNull
    @Override
    public HomeFragmentQRCodeRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * Purpose: Implements OnClickListener for menu button on item.
     */
    // https://www.youtube.com/watch?v=s1fW7CpiB9c
    private class MenuButtonOnClickListener implements View.OnClickListener {
        String hash;
        public MenuButtonOnClickListener(String hash) {
            this.hash = hash;
            Log.d("TAG", hash);
        }
        @Override
        public void onClick(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.home_fragment_qr_view_qr_item:
                        Intent intent = new Intent(view.getContext(), QRCodeActivity.class);
                        intent.putExtra("QR_ID", hash);
                        view.getContext().startActivity(intent);
                        break;
                    case R.id.home_fragment_qr_delete_qr_item:
                        break;
                }
                return false;
            });
            popupMenu.inflate(R.menu.home_fragment_qr_code_menu);
            popupMenu.show();
        }
    }

    public void updateList(Account account) {
        this.account = account;
        this.qrCodes = account.getQrDataList().getQRCodes();
        notifyDataSetChanged();
    }

}