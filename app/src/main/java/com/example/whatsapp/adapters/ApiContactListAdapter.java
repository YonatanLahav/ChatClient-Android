package com.example.whatsapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.entities.ApiContact;
import com.example.whatsapp.interfaces.ListItemClickListener;
import com.example.whatsapp.localdb.Users;
import com.example.whatsapp.localdb.localDatabase;

import java.util.ArrayList;
import java.util.List;

public class ApiContactListAdapter extends RecyclerView.Adapter<ApiContactListAdapter.ContactsViewHolder> {

    private final LayoutInflater _layoutInflater;
    private List<ApiContact> _contacts;
    final private ListItemClickListener _onClickListener;

    public void setContacts(List<ApiContact> contacts) {
//        this._contacts = new ArrayList<>(contacts);
        this._contacts = contacts;
        notifyDataSetChanged();
    }

    public ApiContactListAdapter(Context context, ListItemClickListener onClickListener) {
        _layoutInflater = LayoutInflater.from(context);
        _onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = _layoutInflater.inflate(R.layout.contact_list_item, parent, false);
        return new ContactsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        if (_contacts != null) {
            final ApiContact contact = _contacts.get(position);
            holder.tvName.setText(contact.getName());
            holder.tvLast.setText(contact.getLast());
            holder.tvLastDate.setText(contact.getLastdate());

            Users user = localDatabase.getInstance().usersDao().getUser(contact.getId());
            String encodedImage;
            if (user == null)
                encodedImage = localDatabase.getInstance().usersDao().getUser("Default").getPicture();
            else
                encodedImage = localDatabase.getInstance().usersDao().getUser(contact.getId()).getPicture();
            byte[] imageByteArray = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap image = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            holder.iv_avatar.setImageBitmap(image);
        }
    }

    @Override
    public int getItemCount() {
        if (_contacts != null)
            return _contacts.size();
        return 0;
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvName;
        private final TextView tvLast;
        private final TextView tvLastDate;
        private final ImageView iv_avatar;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvLast = itemView.findViewById(R.id.tvLast);
            tvLastDate = itemView.findViewById(R.id.tvLastDate);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            _onClickListener.onListItemClick(_contacts.get(position));
        }
    }
}
