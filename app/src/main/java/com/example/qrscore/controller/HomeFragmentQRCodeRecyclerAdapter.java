package com.example.qrscore.controller;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscore.R;
import com.example.qrscore.activity.QRCodeActivity;
import com.example.qrscore.model.Account;
import com.example.qrscore.model.QRCode;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Purpose: RecyclerAdapter for HomeFragment Players QR Codes.
 *
 * Outstanding Issues:
 *
 * @author: William Liu
 */
public class HomeFragmentQRCodeRecyclerAdapter extends RecyclerView.Adapter<HomeFragmentQRCodeRecyclerAdapter.MyViewHolder>{
    private final String TAG = "HF_QR_ADAPTER";
    private Account account;
    private ArrayList<QRCode> qrCodes;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public HomeFragmentQRCodeRecyclerAdapter(Account account) {
        this.account = account;
        this.qrCodes = (ArrayList<QRCode>) account.getQRCodesList();
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
        holder.score.setText(qrCodes.get(position).getQRScore().toString());
        String name = account.getProfile().getFirstName() + " " + account.getProfile().getLastName();
        if (account.getProfile().getFirstName() == null) {
            holder.name.setText(account.getUserUID());
        }
        else {
            holder.name.setText(name);
        }

        holder.menuButton.setOnClickListener(new MenuButtonOnClickListener(account.getQRCodesList().get(position).getHash()));
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
            AccountController accountController = new AccountController(view.getContext());
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.home_fragment_qr_view_qr_item:
                        Intent intent = new Intent(view.getContext(), QRCodeActivity.class);
                        intent.putExtra("QR_ID", hash);
                        view.getContext().startActivity(intent);
                        break;
                    case R.id.home_fragment_qr_delete_qr_item:
                        deleteQRCode(account.getUserUID(), hash, accountController);
                        updateList(account);
                }
                return false;
            });
            popupMenu.inflate(R.menu.home_fragment_qr_code_menu);
            popupMenu.show();
        }
    }

    /**
     * Purpose: Update the list with of accounts.
     * @param account
     *      An account instance.
     */
    public void updateList(Account account) {
        this.account = account;
        this.qrCodes = (ArrayList<QRCode>) account.getQRCodesList();
        notifyDataSetChanged();
    }

    /**
     * Purpose: Delete QRCodes of user.
     * @param userID
     *      The UniqueUID of user.
     * @param hash
     *      The hash code of the QRCode.
     */
    public void deleteQRCode(String userID, String hash, AccountController accountController) {
        QRCodeController qrController = new QRCodeController();
        qrController.remove(hash, account.getQRByHash(hash), userID, accountController);
        account.removeQR(hash);
    }

}